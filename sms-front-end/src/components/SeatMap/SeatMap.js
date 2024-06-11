/** @format */

import React, { useRef, useState, useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { IoMdAddCircle } from "react-icons/io";
import { MdOutlineModeEdit, MdModeEdit } from "react-icons/md";
import InputGroup from "react-bootstrap/InputGroup";
import { Layer, Line, Stage, Rect, Group } from "react-konva";
import axios from "axios";
import { useUser } from "../../context/UserContext";
import { getRestaurant } from "../../service/AuthService/RestaurantService";
import { v4 as uuidv4 } from "uuid";

import "./SeatMap.css";
import CircleTable from "../Tables/Circle/CircleTable";
import RectangleTable from "../Tables/Rectangle/RectangleTable";
import { IoPeopleOutline } from "react-icons/io5";
import { MdTableBar } from "react-icons/md";

const SeatMap = () => {
   const [currentTableType, setCurrentTableType] = useState("custom");
   const [currentTable, setCurrentTable] = useState(null);
   const [floorPlan, setFloorPlan] = useState(null);
   const [floorPlans, setFloorPlans] = useState([]);
   const [selectionRect, setSelectionRect] = useState({ isVisible: false, startX: 0, startY: 0, width: 0, height: 0 });
   const [isSelecting, setIsSelecting] = useState(false);
   const [selectedTables, setSelectedTables] = useState([]);
   const [boundaryBox, setBoundaryBox] = useState({ isVisible: false, x: 0, y: 0, width: 0, height: 0 });
   const [isHoveringOverEdit, setIsHoveringOverEdit] = useState(false);
   const [isEditMode, setIsEditMode] = useState(false);
   const layerRef = useRef(null);
   const stageRef = useRef(null);
   const gridRef = useRef(null);
   const { selectedRestaurant, setSelectedRestaurant } = useUser();
   const [draggingTable, setDraggingTable] = useState(null);
   const [toolBoxTables, setToolBoxTables] = useState([]);
   const [draggingItem, setDraggingItem] = useState(null);

   const divRef = useRef(null);
   const [dimensions, setDimensions] = useState({
      width: 0,
      height: 0,
   });

   const [gridDimensions, setGridDimensions] = useState({
      width: 0,
      height: 0,
   });

   const [adjustedWidth, setAdjustedWidth] = useState(0);
   const [adjustedHeight, setAdjustedHeight] = useState(0);

   const outerGridsX = 7;
   const outerGridsY = 7;

   useEffect(() => {
      if (divRef.current?.offsetHeight && divRef.current?.offsetWidth) {
         setDimensions({
            width: divRef.current.offsetWidth,
            height: divRef.current.offsetHeight,
         });
         setGridDimensions({
            width: divRef.current.offsetWidth - 50,
            height: divRef.current.offsetHeight - 50,
         });
      }
   }, [floorPlan]);

   const loadFloorPlans = () => {
      axios
         .get(`${process.env.REACT_APP_API_URL}/restaurant/floormaps`, {
            params: { restaurantId: selectedRestaurant.restaurantId },
         })
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            if (floorPlan != null) {
               setFloorPlan(res.data.find((updatedFloorPlan) => updatedFloorPlan.floorMapId === floorPlan.floorMapId));
            }
            console.log(res.data);
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   };

   useEffect(() => {
      if (selectedRestaurant && selectedRestaurant.serviceTypes) {
         loadFloorPlans();
      }
   }, [selectedRestaurant]);

   useEffect(() => {
      axios
         .get(`${process.env.REACT_APP_API_URL}/restaurant/floormaps`, {
            params: { restaurantId: selectedRestaurant.restaurantId },
         })
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            console.log(res.data);
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   }, []);

   const resetAllEditStates = () => {
      setSelectionRect({ isVisible: false, startX: 0, startY: 0, width: 0, height: 0 });
      setIsSelecting(false);
      setSelectedTables([]);
      setBoundaryBox({ isVisible: false, x: 0, y: 0, width: 0, height: 0 });
      setIsHoveringOverEdit(false);
      setIsEditMode(false);
   };

   useEffect(() => {
      if (floorPlan && selectedTables.length > 0) {
         drawBoundryBox(selectedTables);
      }
   }, [selectedTables]);

   const drawBoundryBox = (tablesWithinSelection) => {
      const minX = Math.min(...tablesWithinSelection.map((table) => table.xPosition - 10));
      const minY = Math.min(...tablesWithinSelection.map((table) => table.yPosition - 10));
      const maxX = Math.max(...tablesWithinSelection.map((table) => table.xPosition + 60));
      const maxY = Math.max(...tablesWithinSelection.map((table) => table.yPosition + 60));

      setBoundaryBox({ x: minX, y: minY, width: maxX - minX, height: maxY - minY, isVisible: true });
   };

   const addFloorPlan = (name) => {
      const floorMapDto = {
         name: name,
         restaurantId: selectedRestaurant.restaurantId,
      };

      return axios
         .post(process.env.REACT_APP_API_URL + "/restaurant/floormap", floorMapDto)
         .then(() => {
            getRestaurant(selectedRestaurant.restaurantId)
               .then((r) => {
                  setSelectedRestaurant(r.data);
               })
               .catch((e) => console.error("Failed to update restaurant", e));
         })
         .catch((err) => {
            console.log("err");
            console.log(err);
         });
   };

   const handleToolboxDrop = (event, table) => {
      if (!draggingTable) return;

      setDraggingTable(null);

      let x = gridRef.current.getRelativePointerPosition().x;
      let y = gridRef.current.getRelativePointerPosition().y;
      console.log(event.target);

      const newShape = {
         xPosition: x,
         yPosition: y,
         tableType: table.type,
         floorMapId: floorPlan.floorMapId,
         chairCount: table.chairCount,
         disableChairsOnTop: table.disableTop,
         disableChairsOnBottom: table.disableBottom,
         disableChairsOnLeft: table.disableLeft,
         disableChairsOnRight: table.disableRight,
      };
      console.log(newShape);
      axios.post(process.env.REACT_APP_API_URL + "/restaurant/floormap/table", newShape).then(() => {
         getRestaurant(selectedRestaurant.restaurantId)
            .then((r) => {
               setSelectedRestaurant(r.data);
            })
            .catch((e) => console.error("Failed to update restaurant", e));
      });
   };

   const handleDragEnd = (id, event) => {
      console.log("drag end", event);
      floorPlan.floorMapItems.forEach((table) => {
         if (table.floorMapItemId === id) {
            table.xPosition = gridRef.current.getRelativePointerPosition().x;
            table.yPosition = gridRef.current.getRelativePointerPosition().y;

            axios.put(process.env.REACT_APP_API_URL + "/restaurant/floormap/table", table).then(() => {
               getRestaurant(selectedRestaurant.restaurantId)
                  .then((r) => {
                     setSelectedRestaurant(r.data);
                  })
                  .catch((e) => console.error("Failed to update restaurant", e));
            });
         }
      });
   };

   const getCenterOfRectangleX = (topLeftCornerX, width) => {
      return topLeftCornerX + width / 2;
   };

   const getCenterOfRectangleY = (topLeftCornerY, height) => {
      return topLeftCornerY + height / 2;
   };

   const generateGridLines = (width, height, xStart, yStart) => {
      const lines = [];

      // Calculate gridSpacingPixels based on the fixed number of outer grids
      const outerGridSizeWidth = width / outerGridsX;
      const outerGridSizeHeight = height / outerGridsY;
      const gridSpacingPixels = Math.min(outerGridSizeWidth, outerGridSizeHeight) / 5;

      const outerGridSize = gridSpacingPixels * 5;

      // Calculate number of complete grids
      const numCompleteGridsWidth = Math.floor((width - xStart + outerGridSize) / outerGridSize);
      const numCompleteGridsHeight = Math.floor((height - yStart + outerGridSize) / outerGridSize);

      // Adjust width and height to fit only complete grids
      const newAdjustedWidth = numCompleteGridsWidth * outerGridSize + xStart;
      const newAdjustedHeight = numCompleteGridsHeight * outerGridSize + yStart;

      const gridBoxSizeWidth = newAdjustedWidth - xStart;
      const gridBoxSizeHeight = newAdjustedHeight - yStart;

      if (gridBoxSizeWidth !== adjustedWidth && gridBoxSizeWidth > 0) {
         setAdjustedWidth(gridBoxSizeWidth);
      }
      if (gridBoxSizeHeight !== adjustedHeight && gridBoxSizeHeight > 0) {
         setAdjustedHeight(gridBoxSizeHeight);
      }

      const thinGridLines = 0.05;
      const thickGridLines = 0.2;

      // Generate small vertical lines
      for (let i = xStart; i <= newAdjustedWidth; i += gridSpacingPixels) {
         lines.push(
            <Line
               points={[i, yStart, i, newAdjustedHeight]}
               stroke='#ddd'
               strokeWidth={thinGridLines}
               key={`v-small-${i}`}
            />
         );
      }

      // Generate small horizontal lines
      for (let j = yStart; j <= newAdjustedHeight; j += gridSpacingPixels) {
         lines.push(
            <Line points={[xStart, j, newAdjustedWidth, j]} stroke='#ddd' strokeWidth={thinGridLines} key={`h-small-${j}`} />
         );
      }

      // Generate large vertical lines
      for (let i = xStart; i <= newAdjustedWidth + gridSpacingPixels; i += outerGridSize) {
         lines.push(
            <Line
               points={[i, yStart, i, newAdjustedHeight]}
               stroke='#ddd'
               strokeWidth={thickGridLines}
               key={`v-large-${i}`}
            />
         );
      }

      // Generate large horizontal lines
      for (let j = yStart; j < newAdjustedHeight + outerGridSize; j += outerGridSize) {
         lines.push(
            <Line
               points={[xStart, j, newAdjustedWidth, j]}
               stroke='#ddd'
               strokeWidth={thickGridLines}
               key={`h-large-${j}`}
            />
         );
      }

      return lines;
   };

   const handleTableClick = (table) => {
      setCurrentTable(table);
   };

   const selectFloorPlan = (selectedFloorPlan) => {
      setFloorPlan(selectedFloorPlan);
      resetAllEditStates();
   };

   const generateDropdownItems = () => {
      return floorPlans.map((curFloorPlan) => (
         <Dropdown.Item
            className='floor-plan-select-dropdown'
            key={curFloorPlan.floorMapId}
            onClick={() => selectFloorPlan(curFloorPlan)}>
            {curFloorPlan.name}
         </Dropdown.Item>
      ));
   };

   const handleEditClick = () => {
      setIsEditMode(true);
   };

   const handleHeaderChange = (e) => {
      const updatedFloorPlan = { ...floorPlan, name: e.target.value };
      setFloorPlan(updatedFloorPlan);
   };

   const handleKeyPress = (e) => {
      if (e.key === "Enter") {
         setIsEditMode(false);
         return axios.put(process.env.REACT_APP_API_URL + "/restaurant/floormap", floorPlan).then(() => {
            getRestaurant(selectedRestaurant.restaurantId)
               .then((r) => {
                  setSelectedRestaurant(r.data);
               })
               .catch((e) => console.error("Failed to update restaurant", e));
         });
      }
   };

   const handleMouseDown = (e) => {
      const stage = e.target.getStage();
      const pointerPos = stage.getPointerPosition();

      if (boundaryBox.isVisible) {
         if (
            isXYWithinBoundry(
               pointerPos.x,
               pointerPos.y,
               boundaryBox.x,
               boundaryBox.width,
               boundaryBox.y,
               boundaryBox.height
            )
         ) {
            return;
         } else {
            boundaryBox.isVisible = false;
            boundaryBox.x = -100;
            boundaryBox.y = -100;
         }
      }

      setSelectionRect({
         ...selectionRect,
         isVisible: true,
         startX: pointerPos.x,
         startY: pointerPos.y,
         width: 0,
         height: 0,
      });
      setIsSelecting(true);
   };

   const isXYWithinBoundry = (x, y, boundaryX, boundaryWidth, boundaryY, boundaryHeight) => {
      return x > boundaryX && x < boundaryX + boundaryWidth && y > boundaryY && y < boundaryY + boundaryHeight;
   };

   const onBoundryBoxMove = (e) => {
      const delta = {
         x: e.target.x() - boundaryBox.x,
         y: e.target.y() - boundaryBox.y,
      };

      const updatedFloorPlan = { ...floorPlan };
      const updatedSelectedTables = selectedTables.map((table) => {
         return {
            ...table,
            xPosition: table.xPosition + delta.x,
            yPosition: table.yPosition + delta.y,
         };
      });
      setSelectedTables(updatedSelectedTables);

      updatedFloorPlan.floorMapItems = updatedFloorPlan.floorMapItems.map((table) => {
         if (selectedTables.some((selectedTable) => selectedTable.floorMapItemId === table.floorMapItemId)) {
            return {
               ...table,
               xPosition: table.xPosition + delta.x,
               yPosition: table.yPosition + delta.y,
            };
         }
         return table;
      });

      setFloorPlan(updatedFloorPlan);
   };

   const onBoundryBoxDragEnd = (e) => {
      const updatedSelectedTables = selectedTables.map((table) => {
         return {
            ...table,
            xPosition: table.xPosition,
            yPosition: table.yPosition,
         };
      });
      setSelectedTables(updatedSelectedTables);
      drawBoundryBox(updatedSelectedTables);

      updatedSelectedTables.forEach((selectedTable) => {
         axios.put(process.env.REACT_APP_API_URL + "/restaurant/floormap/table", selectedTable).then(() => {
            getRestaurant(selectedRestaurant.restaurantId)
               .then((r) => {
                  setSelectedRestaurant(r.data);
               })
               .catch((e) => console.error("Failed to update restaurant", e));
         });
      });
   };

   useEffect(() => {
      setToolBoxTables(generateToolBoxItems());
   }, []); // empty dependency array to run only once on mount

   const handleToolBoxDragEnd = (item, event) => {
      handleToolboxDrop(event, item);
      setToolBoxTables(generateToolBoxItems());
   };

   const handleToolBoxDragStart = (id, event) => {
      const itemToKeep = toolBoxTables.find((item) => item.id === id);
      console.log("Updated Items: ", itemToKeep);
      setDraggingTable(itemToKeep);

      // Generate new toolbox items without the dragging item
      const newToolBoxItems = generateToolBoxItems();

      // Add the dragging item back to the toolbox items
      setToolBoxTables([...newToolBoxItems, itemToKeep]);
   };

   const generateToolBoxItems = () => {
      const items = [];

      items.push({
         id: uuidv4(),
         type: "circle-table",
         chairCount: 1,
         initialRadius: 22,
         xPos: 20,
         yPos: calculateToolBoxItemY(items.length),
      });

      items.push({
         id: uuidv4(),
         type: "circle-table",
         chairCount: 2,
         initialRadius: 22,
         xPos: 20,
         yPos: calculateToolBoxItemY(items.length),
      });

      items.push({
         id: uuidv4(),
         type: "circle-table",
         chairCount: 4,
         initialRadius: 22,
         xPos: 20,
         yPos: calculateToolBoxItemY(items.length),
      });

      items.push({
         id: uuidv4(),
         type: "circle-table",
         chairCount: 6,
         initialRadius: 22,
         xPos: 20,
         yPos: calculateToolBoxItemY(items.length),
      });

      items.push({
         id: uuidv4(),
         type: "rectangle-table",
         chairCount: 2,
         width: 50,
         height: 50,
         xPos: -20,
         yPos: calculateToolBoxItemY(items.length) + 15,
         disableLeft: true,
         disableRight: true,
      });

      items.push({
         id: uuidv4(),
         type: "rectangle-table",
         chairCount: 4,
         width: 50,
         height: 50,
         xPos: -20,
         yPos: calculateToolBoxItemY(items.length) + 15,
      });

      items.push({
         id: uuidv4(),
         type: "rectangle-table",
         chairCount: 4,
         width: 50,
         height: 50,
         xPos: -20,
         yPos: calculateToolBoxItemY(items.length) + 15,
         disableTop: true,
         disableBottom: true,
      });

      items.push({
         id: uuidv4(),
         type: "rectangle-table",
         chairCount: 6,
         width: 50,
         height: 50,
         xPos: -20,
         yPos: calculateToolBoxItemY(items.length) + 25,
         disableTop: true,
         disableBottom: true,
      });

      return items;
   };

   const calculateToolBoxItemY = (n) => {
      const padding = 10;
      const toolBoxItemHeight = 60;
      return (toolBoxItemHeight + padding) * n + 20;
   };

   return (
      <div className='seat-map-container'>
         <div className='seat-map-content'>
            <div className='seat-map-header-2 d-flex align-items-center justify-content-between'>
               <Dropdown className='floor-plan-select-dropdown'>
                  {console.log(floorPlan)}
                  <Dropdown.Toggle variant='secondary' className='floor-plan-select-dropdown'>
                     {floorPlan == null ? "Select Floor Plan" : floorPlan.name}
                  </Dropdown.Toggle>
                  <Dropdown.Menu>{generateDropdownItems()}</Dropdown.Menu>
               </Dropdown>
               <div className='seat-map-header-controls d-flex'>
                  <Button className='add-floor-map-button' onClick={() => addFloorPlan("Change It")}>
                     <IoMdAddCircle className='add-floor-map-icon' /> Create Floor Plan
                  </Button>
               </div>
            </div>

            {floorPlan && (
               <div className='seat-map-editor-container'>
                  <div className='seat-map-editor'>
                     <div id='table-editor-panel' className='seat-map-container'>
                        <div className='seat-map-grid' ref={divRef}>
                           <Stage width={adjustedWidth + 160} height={adjustedHeight + 45} ref={stageRef}>
                              <Layer ref={layerRef}>
                                 <Rect
                                    fill='rgba(15, 15, 15)'
                                    stroke='#ddd'
                                    strokeWidth={0.5}
                                    x={130}
                                    y={10}
                                    height={adjustedHeight + 20}
                                    width={adjustedWidth + 20}
                                    cornerRadius={10}
                                 />

                                 <Group x={10} y={10}>
                                    <Rect
                                       stroke='#ddd'
                                       fill='rgba(15, 15, 15)'
                                       strokeWidth={0.5}
                                       y={0}
                                       height={620}
                                       width={100}
                                       cornerRadius={10}
                                    />
                                    {toolBoxTables &&
                                       toolBoxTables.map((item, index) => {
                                          switch (item.type) {
                                             case "circle-table":
                                                return (
                                                   <CircleTable
                                                      key={item.id}
                                                      initialRadius={item.initialRadius}
                                                      xPos={item.xPos + 30}
                                                      yPos={item.yPos + 20}
                                                      chairCount={item.chairCount}
                                                      onDragStart={() => handleToolBoxDragStart(item.id)}
                                                      onDragEnd={(event) => handleToolBoxDragEnd(item, event)}
                                                      onClick={() => handleTableClick()}
                                                   />
                                                );
                                             case "rectangle-table":
                                                return (
                                                   <RectangleTable
                                                      key={item.id}
                                                      minHeight={40}
                                                      minWidth={40}
                                                      chairCount={item.chairCount}
                                                      xPos={item.xPos + 50}
                                                      yPos={item.yPos - 10}
                                                      onDragStart={() => handleToolBoxDragStart(item.id)}
                                                      onDragEnd={(event) => handleToolBoxDragEnd(item, event)}
                                                      onClick={() => handleTableClick()}
                                                      disableTop={item.disableTop}
                                                      disableBottom={item.disableBottom}
                                                      disableRight={item.disableRight}
                                                      disableLeft={item.disableLeft}
                                                   />
                                                );
                                          }
                                       })}
                                 </Group>

                                 <Group ref={gridRef} x={140} y={20}>
                                    {generateGridLines(gridDimensions.width, gridDimensions.height, 0, 0)}

                                    {floorPlan &&
                                       floorPlan.floorMapItems &&
                                       floorPlan.floorMapItems.map((shape) => {
                                          switch (shape.tableType) {
                                             case "circle-table":
                                                return (
                                                   <CircleTable
                                                      key={shape.floorMapItemId}
                                                      xPos={shape.xPosition}
                                                      yPos={shape.yPosition}
                                                      initialRadius={22}
                                                      onDragEnd={(e) => handleDragEnd(shape.floorMapItemId, e)}
                                                      onClick={(e) => handleTableClick(shape)}
                                                      chairCount={shape.chairCount}
                                                   />
                                                );
                                             case "rectangle-table":
                                                return (
                                                   <RectangleTable
                                                      key={shape.floorMapItemId}
                                                      minHeight={40}
                                                      minWidth={40}
                                                      chairCount={shape.chairCount}
                                                      xPos={shape.xPosition}
                                                      yPos={shape.yPosition}
                                                      onDragEnd={(e) => handleDragEnd(shape.floorMapItemId, e)}
                                                      onClick={(e) => handleTableClick(shape)}
                                                      disableTop={shape.disableChairsOnTop}
                                                      disableBottom={shape.disableChairsOnBottom}
                                                      disableRight={shape.disableChairsOnRight}
                                                      disableLeft={shape.disableChairsOnLeft}
                                                   />
                                                );
                                             default:
                                                return null;
                                          }
                                       })}
                                 </Group>

                                 {selectionRect.isVisible && (
                                    <Rect
                                       x={selectionRect.startX}
                                       y={selectionRect.startY}
                                       width={selectionRect.width}
                                       height={selectionRect.height}
                                       fill='rgba(0,0,255,0.1)'
                                       stroke='black'
                                       strokeWidth={1}
                                    />
                                 )}
                                 {boundaryBox.isVisible && (
                                    <Rect
                                       x={boundaryBox.x}
                                       y={boundaryBox.y}
                                       width={boundaryBox.width}
                                       height={boundaryBox.height}
                                       stroke='blue'
                                       fill='rgba(0,0,255,0.1)'
                                       strokeWidth={2}
                                       draggable
                                       onClick={(e) => {
                                          console.log("on click");
                                          console.log(e);
                                       }}
                                       onMouseDown={(e) => {
                                          console.log("on mouse down");
                                       }}
                                       onMouseUp={(e) => {
                                          console.log("on mouse down");
                                       }}
                                       onDragStart={(e) => {
                                          console.log("drag start");
                                       }}
                                       onDragMove={(e) => onBoundryBoxMove(e)}
                                       onDragEnd={(e) => {
                                          onBoundryBoxDragEnd(e);
                                       }}
                                    />
                                 )}
                              </Layer>
                           </Stage>
                        </div>
                     </div>
                     <div className='property-panels-container'>
                        <div className='seat-map-content-header'>
                           {floorPlan && (
                              <div className='table-header-container'>
                                 <div className='d-flex align-items-center'>
                                    {isEditMode ? (
                                       <input
                                          type='text'
                                          className='seat-map-header-editable me-2'
                                          value={floorPlan.name}
                                          onChange={handleHeaderChange}
                                          onKeyPress={handleKeyPress}
                                          onBlur={() => setIsEditMode(false)}
                                          autoFocus
                                       />
                                    ) : (
                                       <span onClick={handleEditClick} style={{ cursor: "pointer" }}>
                                          {floorPlan.name}
                                       </span>
                                    )}
                                    <div
                                       className='edit-name ms-2'
                                       onMouseEnter={() => setIsHoveringOverEdit(true)}
                                       onMouseLeave={() => setIsHoveringOverEdit(false)}
                                       onClick={handleEditClick}
                                       style={{ display: "flex", alignItems: "center" }}>
                                       <div>{isHoveringOverEdit ? <MdModeEdit /> : <MdOutlineModeEdit />}</div>
                                    </div>
                                    <span className='people-count ms-3'>
                                       <IoPeopleOutline /> 99
                                    </span>
                                    <span className='table-count ms-3'>
                                       <MdTableBar /> 5
                                    </span>
                                 </div>
                              </div>
                           )}
                        </div>
                        {currentTable && (
                           <div className='table-properties-container'>
                              <div className='table-properties-header'>Customize Table</div>
                              <InputGroup id='table-properties-input-group' className='table-property-input-group'>
                                 <Form.Control id='table-input' placeholder='Table Name' />
                                 <Form.Control id='table-input' placeholder='Min Party' />
                                 <Form.Control id='table-input' placeholder='Max Party' />
                                 <Form.Control id='table-input' placeholder='Number of Seats' />
                                 <Button variant='primary'>Duplicate</Button> <Button variant='danger'>Delete</Button>{" "}
                              </InputGroup>
                           </div>
                        )}
                     </div>
                  </div>
               </div>
            )}
         </div>
      </div>
   );
};

export default SeatMap;

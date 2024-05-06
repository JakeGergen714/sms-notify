/** @format */

import React, { useRef, useState, useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import Button from "react-bootstrap/Button";

import Form from "react-bootstrap/Form";
import { IoMdAddCircle } from "react-icons/io";
import { MdOutlineModeEdit, MdModeEdit } from "react-icons/md";

import InputGroup from "react-bootstrap/InputGroup";
import { Layer, Line, Stage, Rect } from "react-konva";
import CircleTable from "../Tables/Circle/CircleTable";
import CustomShape from "../Tables/CustomShape/CustomShape";
import RectangleTable from "../Tables/Rectangle/RectangleTable";
import axios from "axios";

import "./SeatMap.css";
import { emphasize } from "@mui/material";

const SeatMap = () => {
   const [currentTableType, setCurrentTableType] = useState("circle"); // Default to circle
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

   const gridSpacingPixels = 40;
   const stageWidth = window.innerWidth;
   const stageHeight = window.innerHeight;

   const resetAllEditStates = () => {
      setSelectionRect({ isVisible: false, startX: 0, startY: 0, width: 0, height: 0 });
      setIsSelecting(false);
      setSelectedTables([]);
      setBoundaryBox({ isVisible: false, x: 0, y: 0, width: 0, height: 0 });
      setIsHoveringOverEdit(false);
      setIsEditMode(false);
   };

   const loadFloorPlans = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/floorMaps")
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            if (floorPlan != null) {
               //If a floor plan is currently selected, reload it with the latest data
               setFloorPlan(res.data.filter((updatedFloorPlan) => updatedFloorPlan.id == floorPlan.id)[0]);
            }
         })
         .catch((err) => {
            console.log("load floor plans failed");
            console.error(err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   };

   useEffect(() => {
      loadFloorPlans();
   }, []); // Empty dependency array means this effect runs once after initial render

   useEffect(() => {
      console.log("Selected tables changed");
      console.log(selectedTables);

      if (floorPlan && selectedTables) {
         // Calculate boundary box for selected tables
         if (selectedTables.length > 0) {
            drawBoundryBox(selectedTables);
         }
      }
   }, [selectedTables]);

   const drawBoundryBox = (tablesWithinSelection) => {
      console.log("drawing new boundry box");
      const minX = Math.min(...tablesWithinSelection.map((table) => table.xPosition - 10));
      const minY = Math.min(...tablesWithinSelection.map((table) => table.yPosition - 10));
      const maxX = Math.max(...tablesWithinSelection.map((table) => table.xPosition + 60)); // Assuming tables have a width property
      const maxY = Math.max(...tablesWithinSelection.map((table) => table.yPosition + 60)); // Assuming tables have a height property

      // Update boundary box state here (you need to add state for this)
      console.log({ x: minX, y: minY, width: maxX - minX, height: maxY - minY, isVisible: true });
      setBoundaryBox({ x: minX, y: minY, width: maxX - minX, height: maxY - minY, isVisible: true });
   };

   const addFloorPlan = (name) => {
      var floorMapDto = {
         name: name,
         serviceTimeStart: "09:00",
         serviceTimeEnd: "21:00",
      };
      console.log(floorMapDto);
      return axios
         .post(process.env.REACT_APP_API_URL + "/floorMap", floorMapDto)
         .then(() => {
            loadFloorPlans();
         })
         .catch((err) => {
            console.log("err");
            console.log(err);
         });
   };

   const handleDragStartToolbox = (event, shapeType) => {
      setCurrentTableType(shapeType);
   };

   const allowDrop = (event) => {
      event.preventDefault();
   };

   const calculateClosestIntersection = (pos) => {
      return Math.round(pos / gridSpacingPixels) * gridSpacingPixels;
   };

   const handleDragEnd = (id, event) => {
      setFloorPlan((currentFloorPlan) => {
         // Create a deep copy of the floorMapItems to ensure immutability
         const updatedFloorMapItems = currentFloorPlan.floorMapItems.map((table) => {
            if (table.id === id && table.tableType === "custom") {
               // Assuming the rectangle is 40x40 pixels
               var x = calculateClosestIntersection(getCenterOfRectangleX(event.target.x(), 40)) - 20; // Adjust back to top-left
               var y = calculateClosestIntersection(getCenterOfRectangleY(event.target.y(), 40)) - 20; // Adjust back to top-left

               table.xPosition = x;
               table.yPosition = y;

               console.log("updating table", table);

               axios.put(process.env.REACT_APP_API_URL + "/floorMapItem", table).then(() => {
                  loadFloorPlans();
               });

               return { ...table, xPosition: x, yPosition: y }; // Update positions and return the updated table
            }
            return table; // Return the table unchanged if it's not the one being dragged
         });

         // Return a new floorPlan object with the updated floorMapItems
         return { ...currentFloorPlan, floorMapItems: updatedFloorMapItems };
      });

      layerRef.current.draw(); // Redraw the layer to reflect changes
   };

   const handleDragEndToolBox = (event) => {
      event.preventDefault();

      // Assuming the `stageRef` correctly references your Konva Stage
      const stage = stageRef.current.getStage();
      stage.setPointersPositions(event);

      const pointerPosition = stage.getPointerPosition();

      // If there's no pointerPosition, exit the function
      if (!pointerPosition) {
         return;
      }

      let x = calculateClosestIntersection(pointerPosition.x);
      let y = calculateClosestIntersection(pointerPosition.y);

      // Adjust for rectangle to ensure its center snaps to the grid
      if (currentTableType === "rectangle") {
         x = getCenterOfRectangleX(x, 40) - 40; // Adjust assuming the rectangle is 40x40
         y = getCenterOfRectangleY(y, 40) - 40; // Adjust assuming the rectangle is 40x40
      }

      // Construct the new table object
      const newShape = { xPosition: x, yPosition: y, tableType: currentTableType, floorMapId: floorPlan.id };

      console.log("adding new table", newShape);
      axios.post(process.env.REACT_APP_API_URL + "/floorMapItem", newShape).then(() => {
         loadFloorPlans();
      });
   };

   const getCenterOfRectangleX = function (topLeftCornerX, width) {
      return topLeftCornerX + width / 2;
   };

   const getCenterOfRectangleY = function (topLeftCornerY, height) {
      return topLeftCornerY + height / 2;
   };

   const generateGridLines = () => {
      let lines = [];
      for (let i = 0; i < stageWidth; i += gridSpacingPixels) {
         lines.push(<Line points={[i, 0, i, stageHeight]} stroke='#ddd' strokeWidth={1} key={`v-${i}`} />);
      }
      for (let j = 0; j < stageHeight; j += gridSpacingPixels) {
         lines.push(<Line points={[0, j, stageWidth, j]} stroke='#ddd' strokeWidth={1} key={`h-${j}`} />);
      }
      return lines;
   };

   const handleTableClick = (table) => {
      console.log(table);
      setCurrentTable(table);
   };

   const selectFloorPlan = (selectedFloorPlan) => {
      console.log(selectedFloorPlan);
      setFloorPlan(selectedFloorPlan);
      resetAllEditStates();
   };

   const generateDropdownItems = () => {
      return floorPlans.map((curFloorPlan) => (
         <Dropdown.Item key={curFloorPlan.id} onClick={() => selectFloorPlan(curFloorPlan)}>
            {curFloorPlan.name}
         </Dropdown.Item>
      ));
   };

   const handleEditClick = () => {
      setIsEditMode(true);
   };

   const handleHeaderChange = (e) => {
      const updatedFloorPlan = { ...floorPlan, name: e.target.value }; // Create a new object with updated name
      setFloorPlan(updatedFloorPlan); // Set the new floor plan object as the new state
   };

   const handleKeyPress = (e) => {
      if (e.key === "Enter") {
         // Exit edit mode when Enter is pressed
         setIsEditMode(false);
         console.log(floorPlan);
         return axios.put(process.env.REACT_APP_API_URL + "/floorMap", floorPlan).then(() => {
            loadFloorPlans();
            console.log("relod");
         });
      }
   };

   const handleMouseDown = (e) => {
      console.log("handleMouseDown");
      const stage = e.target.getStage();
      const pointerPos = stage.getPointerPosition();
      console.log(e);
      console.log(boundaryBox);

      console.log(pointerPos);
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
            console.log("click in boundry box");
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

   const isXYWithinBoundry = (x, y, boundryX, boundryWidth, boundryY, boundryHeight) => {
      return x > boundryX && x < boundryX + boundryWidth && y > boundryY && y < boundryY + boundryHeight;
   };

   const handleMouseMove = (e) => {
      if (!isSelecting) return;
      const stage = e.target.getStage();
      const pointerPos = stage.getPointerPosition();
      const width = pointerPos.x - selectionRect.startX;
      const height = pointerPos.y - selectionRect.startY;
      setSelectionRect({ ...selectionRect, width, height });
   };

   const handleMouseUp = () => {
      console.log("handleMouseUp");
      setIsSelecting(false);
      // Adjust start and end points based on the drag direction
      if (boundaryBox.isVisible == false) {
         const startX = selectionRect.width > 0 ? selectionRect.startX : selectionRect.startX + selectionRect.width;
         const endX = selectionRect.width > 0 ? selectionRect.startX + selectionRect.width : selectionRect.startX;
         const startY = selectionRect.height > 0 ? selectionRect.startY : selectionRect.startY + selectionRect.height;
         const endY = selectionRect.height > 0 ? selectionRect.startY + selectionRect.height : selectionRect.startY;

         const tablesWithinSelection = floorPlan.floorMapItems.filter((table) => {
            // Now the logic checks the table position against the corrected start and end points
            return (
               table.xPosition >= startX && table.xPosition <= endX && table.yPosition >= startY && table.yPosition <= endY
            );
         });

         console.log("Selected tables", tablesWithinSelection);
         setSelectedTables(tablesWithinSelection);
         setSelectionRect({ ...selectionRect, isVisible: false }); // Hide selection rect
      }
   };

   const onBoundryBoxMove = (e) => {
      console.log("Boundry box dragging ", selectedTables);
      const delta = {
         x: e.target.x() - boundaryBox.x,
         y: e.target.y() - boundaryBox.y,
      };

      // Create a copy of the floorPlan object
      const updatedFloorPlan = { ...floorPlan };
      const updatedSelectedTables = selectedTables.map((table) => {
         return {
            ...table,
            xPosition: table.xPosition + delta.x,
            yPosition: table.yPosition + delta.y,
         };
      });
      setSelectedTables(updatedSelectedTables);

      // Update the positions of the selected tables in the floorPlan object
      updatedFloorPlan.floorMapItems = updatedFloorPlan.floorMapItems.map((table) => {
         if (selectedTables.some((selectedTable) => selectedTable.id === table.id)) {
            return {
               ...table,
               xPosition: table.xPosition + delta.x,
               yPosition: table.yPosition + delta.y,
            };
         }
         return table;
      });

      // Update the floorPlan state with the updated object
      setFloorPlan(updatedFloorPlan);

      console.log("new boundry box", boundaryBox);
   };

   const onBoundryBoxDragEnd = (e) => {
      console.log("boundry box drag end", selectedTables);
      //snap the tables into place
      const updatedSelectedTables = selectedTables.map((table) => {
         return {
            ...table,
            xPosition: calculateClosestIntersection(table.xPosition),
            yPosition: calculateClosestIntersection(table.yPosition),
         };
      });
      setSelectedTables(updatedSelectedTables);
      drawBoundryBox(updatedSelectedTables);

      updatedSelectedTables.forEach((selectedTable) => {
         axios.put(process.env.REACT_APP_API_URL + "/floorMapItem", selectedTable).then(() => {
            loadFloorPlans();
         });
      });
   };

   return (
      <div className='seat-map-content'>
         <div className='seat-map-editor-container'>
            <div className='d-flex align-items-center'>
               {floorPlan && ( // Check if floorPlan is not null
                  <>
                     {isEditMode ? (
                        <input
                           type='text'
                           className='seat-map-header-editable me-2' // Add some right margin
                           value={floorPlan.name}
                           onChange={handleHeaderChange}
                           onKeyPress={handleKeyPress}
                           autoFocus // Automatically focus the input
                        />
                     ) : (
                        <h1 className='seat-map-header me-2'>{floorPlan.name}</h1> // Add some right margin
                     )}

                     <div
                        className='edit-name me-2' // Add some right margin
                        onMouseEnter={() => setIsHoveringOverEdit(true)}
                        onMouseLeave={() => setIsHoveringOverEdit(false)}
                        onClick={handleEditClick} // Add click handler to enter edit mode
                     >
                        {isHoveringOverEdit ? <MdModeEdit /> : <MdOutlineModeEdit />}
                     </div>
                  </>
               )}

               <Dropdown>
                  <Dropdown.Toggle variant='secondary' id='dropdown-basic'>
                     {floorPlan == null ? "Select a Floor Plan" : null}
                  </Dropdown.Toggle>

                  <Dropdown.Menu>
                     {generateDropdownItems()}
                     <Dropdown.Item onClick={() => addFloorPlan("Unnamed Floor Plan")}>
                        Create a new Floor Plan <IoMdAddCircle />
                     </Dropdown.Item>
                  </Dropdown.Menu>
               </Dropdown>
               <div>
                  <button
                     onClick={(e) => {
                        console.log("click");
                        var copies = [];
                        selectedTables.map((table) => {
                           var copy = { ...table, id: null };

                           axios.post(process.env.REACT_APP_API_URL + "/floorMapItem", copy).then((res) => {
                              loadFloorPlans();
                              copies.push(res.date);
                              setSelectedTables(copies);
                           });
                        });
                        var updatedFloorPlan = { ...floorPlan };
                        console.log("copies", copies);
                        console.log(updatedFloorPlan.floorMapItems);
                        updatedFloorPlan.floorMapItems.push(copies);
                        console.log(updatedFloorPlan.floorMapItems);

                        setSelectedTables(copies);
                     }}>
                     duplicate
                  </button>
               </div>
            </div>
            <div className='seat-map-editor'>
               <div id='table-editor-panel' className='table-type-selector'>
                  <h1>Tables</h1>
                  <img
                     src={`${process.env.PUBLIC_URL}/icons/circle-table.png`}
                     alt='Circle Table'
                     draggable='true'
                     onDragStart={(e) => handleDragStartToolbox(e, "custom")}
                     onDragEnd={(e) => handleDragEndToolBox(e)}
                     className='toolbox-item'
                  />
                  {/* Add more toolbox items as needed */}
               </div>
               <div id='table-editor-panel' className='seat-map-container'>
                  <div className='seat-map-grid'>
                     <Stage
                        width={stageWidth}
                        height={stageHeight}
                        ref={stageRef}
                        onDragOver={allowDrop}
                        onClick={() => console.log("stage click")}
                        onMouseDown={handleMouseDown}
                        onMouseMove={handleMouseMove}
                        onMouseUp={handleMouseUp}>
                        <Layer ref={layerRef}>
                           {generateGridLines()}

                           {floorPlan &&
                              floorPlan.floorMapItems &&
                              floorPlan.floorMapItems.map((shape) => {
                                 switch (shape.tableType) {
                                    case "custom":
                                       return (
                                          <CustomShape
                                             key={shape.id}
                                             xPos={shape.xPosition}
                                             yPos={shape.yPosition}
                                             width={50}
                                             height={50}
                                             onDragEnd={(e) => handleDragEnd(shape.id, e)}
                                             onClick={(e) => handleTableClick(shape)}
                                          />
                                       );
                                    default:
                                       return null;
                                 }
                              })}
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
               {currentTable && (
                  <div id='table-editor-panel' className='table-properties-container'>
                     <div className='table-properties-header'>Customize Table</div>
                     <InputGroup id='table-properties-input-group' className='table-property-input-group'>
                        <Form.Control id='table-input' placeholder='Table name' />
                        <Form.Control id='table-input' placeholder='Table size' />
                        <Form.Control id='table-input' placeholder='Min party' />
                        <Form.Control id='table-input' placeholder='Max party' />
                        <Button variant='success'>Save</Button>{" "}
                     </InputGroup>
                  </div>
               )}
            </div>
         </div>
      </div>
   );
};

export default SeatMap;

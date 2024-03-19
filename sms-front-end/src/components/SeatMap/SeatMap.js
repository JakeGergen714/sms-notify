/** @format */

import React, { useRef, useState, useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import Button from "react-bootstrap/Button";

import Form from "react-bootstrap/Form";
import { IoMdAddCircle } from "react-icons/io";
import { MdOutlineModeEdit, MdModeEdit } from "react-icons/md";

import InputGroup from "react-bootstrap/InputGroup";
import { Layer, Line, Stage } from "react-konva";
import { v4 as uuidv4 } from "uuid";
import CircleTable from "../Tables/Circle/CircleTable";
import CustomShape from "../Tables/CustomShape/CustomShape";
import RectangleTable from "../Tables/Rectangle/RectangleTable";
import axios from "axios";

import "./SeatMap.css";

const SeatMap = () => {
   const [currentTableType, setCurrentTableType] = useState("circle"); // Default to circle
   const [currentTable, setCurrentTable] = useState(null);
   const [floorPlan, setFloorPlan] = useState(null);
   const [floorPlans, setFloorPlans] = useState([]);
   const [shapes, setShapes] = useState([]);
   const [isHoveringOverEdit, setIsHoveringOverEdit] = useState(false);
   const [isEditMode, setIsEditMode] = useState(false);
   const layerRef = useRef(null);
   const stageRef = useRef(null);

   const gridSpacingPixels = 40;
   const stageWidth = window.innerWidth;
   const stageHeight = window.innerHeight;

   const loadFloorPlans = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/floorMap")
         .then((res) => {
            console.log(res.data);
            setFloorPlans(res.data); // Set state here after fetching
         })
         .catch((err) => {
            console.error(err);
            setFloorPlans([{ name: "some name" }]); // Set to an empty array in case of error
         });
   };

   useEffect(() => {
      loadFloorPlans();
   }, []); // Empty dependency array means this effect runs once after initial render

   const addFloorPlan = (name) => {
      var floorMapDto = {
         name: name,
      };
      console.log(floorMapDto);
      return axios.post(process.env.REACT_APP_API_URL + "/floorMap", floorMapDto).then(() => {
         loadFloorPlans();
         console.log("relod");
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
      console.log("drag");
      setShapes((shapes) =>
         shapes.map((shape) => {
            if (shape.id === id) {
               if (shape.tableType === "circle") {
                  var x = calculateClosestIntersection(event.target.x());
                  var y = calculateClosestIntersection(event.target.y());

                  return { ...shape, x, y }; // Preserve other shape properties
               } else if (shape.tableType === "rectangle" || shape.tableType === "custom") {
                  // Assuming the rectangle is 40x40 pixels
                  var x = calculateClosestIntersection(getCenterOfRectangleX(event.target.x(), 40)) - 20; // Adjust back to top-left
                  var y = calculateClosestIntersection(getCenterOfRectangleY(event.target.y(), 40)) - 20; // Adjust back to top-left

                  return { ...shape, id: uuidv4(), x, y }; // Preserve other shape properties
               }
            }
            return shape;
         })
      );

      layerRef.current.draw();
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

      // Construct the new shape object
      const newShape = { xPosition: x, yPosition: y, tableType: currentTableType };
      floorPlan.floorMapItems.add(newShape);
      
      return axios.put(process.env.REACT_APP_API_URL + "/floorMap", floorPlan).then(() => {
         loadFloorPlans();
         console.log("relod");
      });

      // Add the new shape to the shapes array
      setShapes([...shapes, newShape]);
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
                     <Stage width={stageWidth} height={stageHeight} ref={stageRef} onDragOver={allowDrop}>
                        <Layer ref={layerRef}>
                           {generateGridLines()}

                           {floorPlan.floorMapItems.map((shape) => {
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

/** @format */

import React, { useRef, useState } from "react";
import { Dropdown } from "react-bootstrap";
import Button from "react-bootstrap/Button";

import Form from "react-bootstrap/Form";
import { IoMdAddCircle } from "react-icons/io";

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
   const [shapes, setShapes] = useState([]);
   const layerRef = useRef(null);
   const stageRef = useRef(null);

   const gridSpacingPixels = 40;
   const stageWidth = window.innerWidth;
   const stageHeight = window.innerHeight;

   const loadFloorPlans = () => {
      return axios
         .get(process.env.REACT_APP_API_URL + "/floorMap")
         .then((res) => {
            console.log(res.data);
            return res.data;
         })
         .catch((err) => {
            console.error(err);
            return [];
         });
   };

   var floorPlans = loadFloorPlans();
   console.log(floorPlans);

   const addFloorPlan = () => {
      var floorMapDto = {
         name: "Unnamed Floor Plan",
      };
      return axios.post(process.env.REACT_APP_API_URL + "/floorMap", floorMapDto);
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
      const newShape = { id: uuidv4(), x, y, tableType: currentTableType };

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

   const getFloorPlanTitle = () => {
      return floorPlan == null ? "Select a Floor Plan" : floorPlan;
   };

   const handleTableClick = (table) => {
      console.log(table);
      setCurrentTable(table);
   };

   return (
      <div className='seat-map-content'>
         <div className='seat-map-editor-container'>
            <div className='d-flex align-items-center'>
               <h1 className='seat-map-header'> {getFloorPlanTitle()}</h1>
               <Dropdown>
                  <Dropdown.Toggle variant='secondary' id='dropdown-basic'></Dropdown.Toggle>

                  <Dropdown.Menu>
                     <Dropdown.Item onClick={() => setFloorPlan("Main Floor")}>Action</Dropdown.Item>
                     <Dropdown.Item>Another action</Dropdown.Item>
                     <Dropdown.Divider style={{ backgroundColor: "black" }} />

                     <Dropdown.Item>
                        Create a new Floor Plan <IoMdAddCircle />
                     </Dropdown.Item>
                  </Dropdown.Menu>
               </Dropdown>
            </div>

            <div className='seat-map-editor'>
               <div id='table-editor-panel' className='table-type-selector'>
                  <h1>Tables</h1>
                  <img
                     src={`${process.env.PUBLIC_URL}/circle-table.png`}
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

                           {shapes.map((shape) => {
                              switch (shape.tableType) {
                                 case "circle":
                                    return (
                                       <CircleTable
                                          key={shape.id}
                                          xPos={shape.x}
                                          yPos={shape.y}
                                          radius={20}
                                          onDragEnd={(e) => handleDragEnd(shape.id, e)}
                                          onClick={(e) => handleTableClick(shape)}
                                       />
                                    );
                                 case "rectangle":
                                    return (
                                       <RectangleTable
                                          key={shape.id}
                                          xPos={shape.x}
                                          yPos={shape.y}
                                          width={40}
                                          height={40}
                                          onDragEnd={(e) => handleDragEnd(shape.id, e)}
                                       />
                                    );
                                 case "custom":
                                    return (
                                       <CustomShape
                                          key={shape.id}
                                          xPos={shape.x}
                                          yPos={shape.y}
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

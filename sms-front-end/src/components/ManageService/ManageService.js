/** @format */

import React, { useRef, useState, useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import Button from "react-bootstrap/Button";

import Form from "react-bootstrap/Form";
import { IoMdAddCircle } from "react-icons/io";
import { MdOutlineModeEdit, MdModeEdit } from "react-icons/md";
import { GoClockFill } from "react-icons/go";

import { IoMdStopwatch } from "react-icons/io";

import { AiFillNotification } from "react-icons/ai";
import { GrGroup } from "react-icons/gr";

import InputGroup from "react-bootstrap/InputGroup";
import { Layer, Line, Stage, Rect } from "react-konva";
import CircleTable from "../Tables/Circle/CircleTable";
import CustomShape from "../Tables/CustomShape/CustomShape";
import RectangleTable from "../Tables/Rectangle/RectangleTable";
import axios from "axios";

import "./ManageService.css";
import { emphasize } from "@mui/material";

const ManageService = () => {
   const [floorPlan, setFloorPlan] = useState(null);
   const [floorPlans, setFloorPlans] = useState([]);
   const layerRef = useRef(null);
   const stageRef = useRef(null);

   const stageWidth = window.innerWidth;
   const stageHeight = window.innerHeight;

   const loadFloorPlans = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/floorMap")
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

   return (
      <div className='seat-map-content'>
         <div className='seat-map-editor-container'>
            <div className='d-flex align-items-center'>
               {floorPlan && ( // Check if floorPlan is not null
                  <h1 className='seat-map-header me-2'>{floorPlan.name}</h1> // Add some right margin
               )}

               <Dropdown>
                  <Dropdown.Toggle variant='secondary' id='dropdown-basic'>
                     {floorPlan == null ? "Select a Floor Plan" : null}
                  </Dropdown.Toggle>

                  <Dropdown.Menu>{generateDropdownItems()}</Dropdown.Menu>
               </Dropdown>
            </div>
            <div className='seat-map-editor'>
               <div className='waitlist-container'>
                  <h1>The Wait List</h1>
                  <div className='waitlist-card'>
                     <div className='waitlist-card-header-row'>
                        <div className='waitlist-card-party-name'>Pepper</div>
                     </div>

                     <div className='waitlist-card-info-row'>
                        <div className='waitlist-party-size'>
                           <GrGroup />2
                        </div>
                        <div className='waitlist-card-wait-time'>
                           <IoMdStopwatch /> 15 min
                        </div>
                        <div className='waitlist-card-notification-row'>
                           <button
                              className='waitlist-card-notify-guest btn btn-primary'
                              onClick={() => alert("Notify via text!")}>
                              <AiFillNotification />
                           </button>
                        </div>
                     </div>
                  </div>
               </div>

               <div id='table-editor-panel' className='seat-map-container'>
                  <div className='seat-map-grid'>
                     <Stage
                        width={stageWidth}
                        height={stageHeight}
                        ref={stageRef}
                        onClick={() => console.log("stage click")}
                        onMouseDown={() => console.log("mouse down")}
                        onMouseMove={() => console.log("mouse move")}
                        onMouseUp={() => console.log("mouse up")}>
                        <Layer ref={layerRef}>
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
               <div className='reservation-container'>
                  <h1>The Reservations</h1>
                  <div className='reservation-card'>
                     <div className='reservation-card-header-row'>
                        <div className='reservation-card-party-name'>Pepper</div>
                     </div>

                     <div className='reservation-card-info-row'>
                        <div className='reservation-party-size'>
                           <GrGroup />2
                        </div>
                        <div className='reservation-card-wait-time'>
                           <GoClockFill /> 5:00 pm
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
   );
};

export default ManageService;

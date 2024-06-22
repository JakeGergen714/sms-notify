/** @format */

import React, { useRef, useState, useEffect } from "react";
import { Dropdown } from "react-bootstrap";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import axios from "axios";
import { GoClockFill } from "react-icons/go";
import { IoMdStopwatch } from "react-icons/io";
import { AiFillNotification } from "react-icons/ai";
import { GrGroup } from "react-icons/gr";
import { Layer, Line, Stage, Rect } from "react-konva";
import CircleTable from "../Tables/Circle/CircleTable";
import RectangleTable from "../Tables/Rectangle/RectangleTable";
import { useUser } from "../../context/UserContext";

import "./ManageService.css";

const ManageService = () => {
   const [floorPlan, setFloorPlan] = useState(null);
   const [floorPlans, setFloorPlans] = useState([]);
   const [activeService, setActiveService] = useState(null);
   const [activeServiceSchedule, setActiveServiceSchedule] = useState(null);
   const [servers, setServers] = useState(null);

   const layerRef = useRef(null);
   const stageRef = useRef(null);
   const { selectedRestaurant } = useUser(); // Get the current restaurant from context
   const divRef = useRef(null);
   const [dimensions, setDimensions] = useState({
      width: 0,
      height: 0,
   });

   const [gridDimensions, setGridDimensions] = useState({
      width: 0,
      height: 0,
   });

   useEffect(() => {
      getActiveService();
      loadServers();
   }, [selectedRestaurant]); // Empty dependency array means this effect runs once after initial render

   const selectFloorPlan = (floorPlan) => {
      setFloorPlan(floorPlan);
   };

   useEffect(() => {
      if (divRef.current?.offsetHeight && divRef.current?.offsetWidth) {
         setDimensions({
            width: divRef.current.offsetWidth,
            height: divRef.current.offsetHeight,
         });
      }
   }, [divRef]);

   const getNextMondayAt9AM = () => {
      const now = new Date();
      const dayOfWeek = now.getDay();
      const nextMonday = new Date(now);

      // Calculate the number of days to add to get to the next Monday
      const daysToAdd = (dayOfWeek === 0 ? 1 : 8) - dayOfWeek;
      nextMonday.setDate(now.getDate() + daysToAdd);

      // Set the time to 9 AM
      nextMonday.setHours(9, 0, 0, 0);

      return nextMonday;
   };

   const getActiveService = () => {
      const now = getNextMondayAt9AM();
      const currentDay = now.toLocaleDateString("en-US", { weekday: "long" }).toUpperCase();
      const currentTime = now.toTimeString().split(" ")[0];

      for (const serviceType of selectedRestaurant.serviceTypes) {
         for (const schedule of serviceType.serviceSchedules) {
            if (schedule.dayOfWeek === currentDay) {
               if (currentTime >= schedule.startTime && currentTime <= schedule.endTime) {
                  console.log("active service: ", serviceType);
                  setActiveService(serviceType);
                  setActiveServiceSchedule(schedule);
                  setFloorPlan(serviceType.floorMaps[0]);
                  setFloorPlans(serviceType.floorMaps);
               }
            }
         }
      }
   };

   const loadServers = () => {
      axios
         .get(`${process.env.REACT_APP_API_URL}/restaurant/servers`, {
            params: { restaurantId: selectedRestaurant.restaurantId },
         })
         .then((res) => {
            setServers(res.data); // Set state here after fetching
            console.log(res.data);
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setServers([]); // Set to an empty array in case of error
         });
   };

   return (
      <div className='manage-service-content'>
         <div className='manage-service-header'>
            <div className='manage-service-service-info-header'>
               <h2> {activeService != null ? activeService.name : ""} </h2>
               <div>
                  {activeServiceSchedule != null ? activeServiceSchedule.startTime : ""} -{" "}
                  {activeServiceSchedule != null ? activeServiceSchedule.endTime : ""}
               </div>
            </div>
         </div>

         <div className='manage-service-container'>
            <div className='list-container'>
               <div className='waitlist-container'>
                  <div className='container-title'>Waitlist</div>
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
                     </div>
                  </div>
               </div>
               <div className='reservation-container'>
                  <div className='container-title'>Reservations</div>

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

            <div className='manage-service-floor-map-container'>
               <div className='manage-service-floor-map' ref={divRef}>
                  <Stage width={dimensions.width} height={dimensions.height} ref={stageRef}>
                     <Layer ref={layerRef}>
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
                     </Layer>
                  </Stage>
               </div>
            </div>
            <div className='floors-and-services-container'>
               <div className='reservation-container'>
                  <div className='container-title'>Floor Plans</div>
                  {floorPlans.map((seatMap) => {
                     return (
                        <div
                           className={`seat-map-card ${seatMap === floorPlan ? "selected" : ""}`}
                           onClick={() => selectFloorPlan(seatMap)}>
                           <div className='seat-map-name'>{seatMap.name}</div>
                           <div className='table-count'>{seatMap.floorMapItems.length}</div>
                        </div>
                     );
                  })}
               </div>
               <div className='reservation-container'>
                  <div className='container-title'>Servers</div>
                  {servers &&
                     servers.map((server) => {
                        return (
                           <div className='server-panel'>
                              <div className='reservation-card-header-row'>
                                 <div className='reservation-card-party-name'>{server.name}</div>
                              </div>
                           </div>
                        );
                     })}
               </div>
            </div>
         </div>
      </div>
   );
};

export default ManageService;

/** @format */

import React, { useState, useRef } from "react";
import "./WeeklySchedule.css"; // Custom CSS for additional styling
import chroma from "chroma-js";

const WeeklySchedule = ({ serviceTypes }) => {
   const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

   function generateDistinctColors(n) {
      const scale = chroma.scale("Spectral").mode("lch").colors(n);
      return scale;
   }

   const distinctColors = [
      "#e6194b",
      "#3cb44b",
      "#ffe119",
      "#4363d8",
      "#f58231",
      "#911eb4",
      "#46f0f0",
      "#f032e6",
      "#bcf60c",
      "#fabebe",
      "#008080",
      "#e6beff",
      "#9a6324",
      "#fffac8",
      "#800000",
      "#aaffc3",
      "#808000",
      "#ffd8b1",
      "#000075",
      "#808080",
   ];

   // Example usage:

   const createDate = (day, time) => {
      const [hours, minutes, seconds] = time.split(":").map(Number);
      const date = new Date();
      date.setHours(hours, minutes, 0, 0);

      const currentDay = date.getDay();
      const targetDay = daysOfWeek.findIndex((dayOfWeek) => dayOfWeek.toUpperCase() === day.toUpperCase());

      if (targetDay === -1) {
         throw new Error("Invalid day of the week");
      }

      const dayDifference = (targetDay - currentDay + 7) % 7;
      date.setDate(date.getDate() + dayDifference);

      return date;
   };

   const createServiceSchedules = (serviceTypes) => {
      const scheduleItems = [];
      var colorIndex = 0;
      serviceTypes.forEach((serviceType) => {
         var colorForService = distinctColors[colorIndex++];
         serviceType.serviceSchedules.forEach((serviceSchedule) => {
            scheduleItems.push({
               name: serviceType.name,
               day: daysOfWeek.find((day) => day.toUpperCase() === serviceSchedule.dayOfWeek),
               startTime: createDate(serviceSchedule.dayOfWeek, serviceSchedule.startTime),
               endTime: createDate(serviceSchedule.dayOfWeek, serviceSchedule.endTime),
               color: colorForService,
            });
         });
      });
      return scheduleItems;
   };

   const [scheduleItems, setScheduleItems] = useState(createServiceSchedules(serviceTypes));

   const generateTimeSlots = () => {
      const times = [];
      const startTime = new Date();
      startTime.setHours(6, 0, 0, 0); // Start at 6:00 AM

      for (let hour = 6; hour < 24 + 5; hour++) {
         for (let minutes = 0; minutes < 60; minutes += 60) {
            // Create a new Date object for each time slot
            let slotTime = new Date(startTime.getTime());
            slotTime.setHours(hour % 24, minutes);

            // Get 24-hour format
            const hour24 = slotTime.getHours();
            const minuteStr = slotTime.getMinutes().toString().padStart(2, "0");

            // Get 12-hour format
            const hour12 = hour24 % 12 === 0 ? 12 : hour24 % 12;
            const ampm = hour24 < 12 ? "AM" : "PM";
            const time12 = `${hour12}:${minuteStr} ${ampm}`;

            times.push({ display: time12, date: slotTime });
         }
      }
      return times;
   };

   const timeSlots = generateTimeSlots();

   const calculateHeight = (startTime, endTime) => {
      const startIndex = timeSlots.findIndex((slot) => startTime.getHours() === slot.date.getHours());
      const endIndex = timeSlots.findIndex((slot) => endTime.getHours() === slot.date.getHours());
      const numberOfCells = endIndex - startIndex;

      // Need to remove the start offset amount, and add on the end offset
      const endOffset = endTime.getMinutes() / 60;
      const startOffset = startTime.getMinutes() / 60;
      return `calc(var(--cell-height) * ${numberOfCells} + ${endOffset} * var(--cell-height) - ${startOffset} * var(--cell-height))`;
   };

   const calculateOffset = (minutes) => {
      return `calc(var(--cell-height) * ${minutes / 60})`;
   };

   const handleDragTopEdgeStart = (event, serviceIndex) => {
      console.log("drag start");
      event.preventDefault();
      const service = scheduleItems[serviceIndex];
      const initialY = event.clientY;

      const handleMouseMove = (moveEvent) => {
         const deltaY = moveEvent.clientY - initialY;
         let newStartTime = new Date(service.startTime.getTime());
         newStartTime.setMinutes(newStartTime.getMinutes() + Math.round(deltaY / 2)); // Adjusting based on 2 pixels per minute

         const minStartTime = new Date(service.endTime.getTime());
         minStartTime.setMinutes(minStartTime.getMinutes() - 60); // 15 minutes before the end time

         // Check for overlap with other services
         let isOverlap = false;
         scheduleItems.forEach((item, index) => {
            if (index !== serviceIndex && item.day === service.day) {
               if (newStartTime <= item.endTime && service.endTime > item.startTime) {
                  isOverlap = true;
                  newStartTime = new Date(item.endTime.getTime());
                  newStartTime.setMinutes(newStartTime.getMinutes() + 1); // Set to end time of previous service + 1 minute
               }
            }
         });

         // Ensure newStartTime is within minStartTime constraint
         if (newStartTime > minStartTime) {
            newStartTime = minStartTime;
         }

         const updatedScheduleItems = [...scheduleItems];
         updatedScheduleItems[serviceIndex] = { ...service, startTime: newStartTime };
         setScheduleItems(updatedScheduleItems);

         moveEvent.preventDefault();
      };

      const handleMouseUp = () => {
         document.removeEventListener("mousemove", handleMouseMove);
         document.removeEventListener("mouseup", handleMouseUp);
      };

      document.addEventListener("mousemove", handleMouseMove);
      document.addEventListener("mouseup", handleMouseUp);
   };

   const handleDragBottomEdgeStart = (event, serviceIndex) => {
      console.log("drag start");
      event.preventDefault();
      const service = scheduleItems[serviceIndex];
      const initialY = event.clientY;

      const handleMouseMove = (moveEvent) => {
         const deltaY = moveEvent.clientY - initialY;
         let newEndTime = new Date(service.endTime.getTime());
         newEndTime.setMinutes(newEndTime.getMinutes() + Math.round(deltaY / 2)); // Adjusting based on 2 pixels per minute

         const minEndTime = new Date(service.startTime.getTime());
         minEndTime.setMinutes(minEndTime.getMinutes() + 60); // 15 minutes after the start time

         // Check for overlap with other services
         let isOverlap = false;
         scheduleItems.forEach((item, index) => {
            if (index !== serviceIndex && item.day === service.day) {
               if (newEndTime >= item.startTime && service.startTime < item.endTime) {
                  isOverlap = true;
                  newEndTime = new Date(item.startTime.getTime());
                  newEndTime.setMinutes(newEndTime.getMinutes() - 1); // Set to start time of next service - 1 minute
               }
            }
         });

         // Ensure newEndTime is within minEndTime constraint
         if (newEndTime < minEndTime) {
            newEndTime = minEndTime;
         }

         const updatedScheduleItems = [...scheduleItems];
         updatedScheduleItems[serviceIndex] = { ...service, endTime: newEndTime };
         setScheduleItems(updatedScheduleItems);

         moveEvent.preventDefault();
      };

      const handleMouseUp = () => {
         document.removeEventListener("mousemove", handleMouseMove);
         document.removeEventListener("mouseup", handleMouseUp);
      };

      document.addEventListener("mousemove", handleMouseMove);
      document.addEventListener("mouseup", handleMouseUp);
   };

   function formatTimeTo12Hour(date) {
      let hours = date.getHours();
      const minutes = date.getMinutes();
      const ampm = hours >= 12 ? "PM" : "AM";
      hours = hours % 12;
      hours = hours ? hours : 12; // the hour '0' should be '12'
      const minutesStr = minutes < 10 ? "0" + minutes : minutes;
      return `${hours}:${minutesStr} ${ampm}`;
   }

   return (
      <div className='grid-container-wrapper'>
         <div className='grid-container'>
            <div className='grid'>
               <div className='grid-header'>
                  <div className='grid-header-cell first-header'></div>
                  {daysOfWeek.map((day, index) => (
                     <div key={index} className='grid-header-cell'>
                        {day}
                     </div>
                  ))}
               </div>
               <div className='grid-body'>
                  <div className='grid-times'>
                     {timeSlots.map((slot, index) => (
                        <div key={index} className='grid-time-cell'>
                           {index === timeSlots.length - 1 ? "" : slot.display}
                        </div>
                     ))}
                  </div>
                  {daysOfWeek.map((day, dayIndex) => (
                     <div key={dayIndex} className='grid-column'>
                        {timeSlots.map((slot, timeIndex) => {
                           // Find the service item and its index
                           const serviceIndex = scheduleItems.findIndex(
                              // Offset by one because the time cells are padded down by 1 cell height so that the times line up with the grid cell lines
                              (service) => service.day === day && service.startTime.getHours() === slot.date.getHours() - 1
                           );

                           const service = serviceIndex !== -1 ? scheduleItems[serviceIndex] : null;
                           if (service) {
                              const height = calculateHeight(service.startTime, service.endTime);
                              const offset = calculateOffset(service.startTime.getMinutes());
                              return (
                                 <div key={serviceIndex} className='grid-cell'>
                                    <div
                                       className='service-schedule'
                                       style={{ height, top: offset, backgroundColor: service.color }}>
                                       {service.name}
                                       <div className='time-container'>
                                          <div className='start-time'>{formatTimeTo12Hour(service.startTime)}</div>
                                          <div className='time-separator'> - </div>
                                          <div className='end-time'>{formatTimeTo12Hour(service.endTime)}</div>
                                       </div>

                                       <div
                                          className='top-edge'
                                          onMouseDown={(e) => handleDragTopEdgeStart(e, serviceIndex)}></div>
                                       <div
                                          className='bottom-edge'
                                          onMouseDown={(e) => handleDragBottomEdgeStart(e, serviceIndex)}></div>
                                    </div>
                                 </div>
                              );
                           } else {
                              return <div key={`${dayIndex}-${timeIndex}`} className='grid-cell' />;
                           }
                        })}
                     </div>
                  ))}
               </div>
            </div>
         </div>
      </div>
   );
};

export default WeeklySchedule;

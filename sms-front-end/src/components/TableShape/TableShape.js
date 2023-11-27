/** @format */

import React, { useState, useEffect } from "react";
import { IoIosResize } from "react-icons/io";
import "./TableShape.css";

const TableShape = ({ shape, setShapes, setActionInProgress }) => {
   const [resizing, setResizing] = useState(false);
   const [rotating, setRotating] = useState(false);
   const [rotation, setRotation] = useState(0);
   const [dragging, setDragging] = useState(false);
   const [initialMousePosition, setInitialMousePosition] = useState({ x: 0, y: 0 });

   const handleMouseDown = (e) => {
      e.stopPropagation();
      console.log("Mouse Down Detected!"); // Debugging
      setDragging(true);
      setInitialMousePosition({ x: e.clientX, y: e.clientY });
   };

   const handleResizeMouseDown = (e) => {
      e.stopPropagation();
      setResizing(true);
      setActionInProgress(true);

      const centerX = shape.x + (shape.width || 50) / 2;
      const centerY = shape.y + (shape.height || 50) / 2;
      const initialDistance = Math.sqrt((e.clientX - centerX) ** 2 + (e.clientY - centerY) ** 2);

      // Set initial distance
      setInitialMousePosition({ x: e.clientX, y: e.clientY, initialDistance });
   };

   const handleRotateMouseDown = (e) => {
      e.stopPropagation();
      setRotating(true);
      setActionInProgress(true);
   };

   useEffect(() => {
      const handleMouseUp = () => {
         if (resizing || rotating || dragging) {
            setResizing(false);
            setRotating(false);
            setDragging(false);
         }
         // Delay the reset of the actionInProgress state
         setTimeout(() => {
            setActionInProgress(false);
         }, 0);
      };

      const handleMouseMove = (e) => {
         if (resizing) {
            const deltaX = e.clientX - initialMousePosition.x;
            const deltaY = e.clientY - initialMousePosition.y;

            if (shape.tableType === "circle") {
               const centerX = shape.x + (shape.width || 50) / 2;
               const centerY = shape.y + (shape.height || 50) / 2;

               // Calculate current distance from center
               const currentDistance = Math.sqrt((e.clientX - centerX) ** 2 + (e.clientY - centerY) ** 2);

               // Determine the change in distance
               const changeInDistance = currentDistance - initialMousePosition.initialDistance;

               // Adjust the diameter based on change in distance
               const newDiameter = (shape.width || 50) + changeInDistance;

               const updatedShape = {
                  ...shape,
                  x: centerX - newDiameter / 2,
                  y: centerY - newDiameter / 2,
                  width: newDiameter,
                  height: newDiameter,
               };

               setShapes((prevShapes) => prevShapes.map((s) => (s === shape ? updatedShape : s)));

               // Update the initial distance for the next frame
               setInitialMousePosition({ ...initialMousePosition, initialDistance: currentDistance });
            } else {
               const updatedShape = {
                  ...shape,
                  width: (shape.width || 50) + deltaX,
                  height: (shape.height || 50) + deltaY,
               };

               setShapes((prevShapes) => prevShapes.map((s) => (s === shape ? updatedShape : s)));
               setInitialMousePosition({
                  x: e.clientX,
                  y: e.clientY,
               });
            }
         } else if (rotating) {
            const centerY = shape.y + (shape.height || 50) / 2;
            const centerX = shape.x + (shape.width || 50) / 2;
            const radians = Math.atan2(e.clientY - centerY, e.clientX - centerX);
            const degree = radians * (180 / Math.PI);
            setRotation(degree);
         } else if (dragging) {
            const deltaX = e.clientX - initialMousePosition.x;
            const deltaY = e.clientY - initialMousePosition.y;

            const updatedShape = {
               ...shape,
               x: shape.x + deltaX,
               y: shape.y + deltaY,
            };
            setShapes((prevShapes) => prevShapes.map((s) => (s === shape ? updatedShape : s)));
            console.log(shape);
            // Update initial mouse position for the next frame
            setInitialMousePosition({ x: e.clientX, y: e.clientY });
         }
      };

      document.addEventListener("mouseup", handleMouseUp);
      document.addEventListener("mousemove", handleMouseMove);

      return () => {
         document.removeEventListener("mouseup", handleMouseUp);
         document.removeEventListener("mousemove", handleMouseMove);
      };
   }, [resizing, rotating, shape, dragging]);

   return (
      <div
         className={shape.tableType}
         onMouseDown={handleMouseDown}
         style={{
            top: shape.y,
            left: shape.x,
            width: shape.width || 50,
            height: shape.height || 50,
            position: "absolute",
            transform: `rotate(${rotation}deg)`,
         }}>
         <IoIosResize className='resize-handle' onMouseDown={handleResizeMouseDown} />
         <div className='rotate-handle' onMouseDown={handleRotateMouseDown}></div>
      </div>
   );
};

export default TableShape;

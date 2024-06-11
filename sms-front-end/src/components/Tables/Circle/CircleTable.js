/** @format */

// CircleShape.js
import React from "react";
import { Circle, Group } from "react-konva";
import Chair from "../Chair/Chair";

const CircleTable = ({ xPos, yPos, initialRadius, onDragEnd = null, onDragStart = null, onClick = null, chairCount }) => {
   // Ensure initialRadius and chairCount have valid values
   if (!initialRadius || !chairCount) {
      return null;
   }

   // Chair dimensions (assuming elliptical chairs)
   const chairWidth = initialRadius * 0.4;
   const chairHeight = initialRadius * 0.8;

   // Calculate the required radius to fit all chairs
   const chairCircumference = chairCount * chairWidth * 3;
   const requiredRadius = chairCircumference / (2 * Math.PI);
   const radius = Math.max(initialRadius, requiredRadius);

   const chairs = [];

   for (let i = 0; i < chairCount; i++) {
      const angle = (i * 2 * Math.PI) / chairCount;
      const x = radius * Math.cos(angle);
      const y = radius * Math.sin(angle);

      // Calculate rotation angle in degrees to make chairs face away from the center
      const rotation = (angle * 180) / Math.PI;

      chairs.push(
         <Chair key={i} x={x} y={y} radiusX={chairWidth / 2} radiusY={chairHeight / 2} fill='white' rotation={rotation} />
      );
   }

   return (
      <Group
         className='circle-table'
         draggable
         onDragEnd={onDragEnd}
         onDragStart={onDragStart}
         onClick={onClick}
         x={xPos}
         y={yPos}
         onMouseEnter={(e) => {
            const container = e.target.getStage().container();
            container.style.cursor = "grab";
         }}
         onMouseDown={(e) => {
            const container = e.target.getStage().container();
            container.style.cursor = "grabbing";
         }}
         onMouseUp={(e) => {
            const container = e.target.getStage().container();
            container.style.cursor = "grab";
         }}
         onMouseLeave={(e) => {
            const container = e.target.getStage().container();
            container.style.cursor = "auto";
         }}>
         {chairs}

         <Circle x={0} y={0} radius={radius} fill='grey' stroke='black' strokeWidth={1} />
      </Group>
   );
};

export default CircleTable;

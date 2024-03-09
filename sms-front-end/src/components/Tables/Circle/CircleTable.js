/** @format */

// CircleShape.js
import React from "react";
import { Circle } from "react-konva";

const CircleTable = ({ xPos, yPos, radius, onDragEnd, onClick }) => {
   return (
      <Circle
         x={xPos}
         y={yPos}
         radius={radius}
         fill='red'
         stroke='black'
         strokeWidth={4}
         draggable
         onDragEnd={onDragEnd}
         onClick={onClick}
      />
   );
};

export default CircleTable;

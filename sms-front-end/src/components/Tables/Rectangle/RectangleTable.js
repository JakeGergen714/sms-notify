/** @format */

// RectangleShape.js
import React from "react";
import { Rect } from "react-konva";

const RectangleTable = ({ xPos, yPos, width, height, onDragEnd, onClick }) => {
   return (
      <Rect
         x={xPos}
         y={yPos}
         width={width}
         height={height}
         fill='red'
         stroke='black'
         strokeWidth={4}
         draggable
         onDragEnd={onDragEnd}
         onClick={onClick}
      />
   );
};

export default RectangleTable;

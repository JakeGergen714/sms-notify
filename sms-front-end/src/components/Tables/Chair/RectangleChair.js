/** @format */

import React from "react";
import { Ellipse, Rect } from "react-konva";

const RectangleChair = ({ x, y, width, height, rotation }) => {
   return (
      <Rect
         x={x}
         y={y}
         width={width}
         height={height}
         rotation={rotation}
         cornerRadius={6}
         fill='darkgrey'
         stroke='black'
         strokeWidth={1}
      />
   );
};

export default RectangleChair;

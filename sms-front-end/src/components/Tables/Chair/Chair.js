/** @format */

import React from "react";
import { Ellipse } from "react-konva";

const Chair = ({ x, y, radiusX, radiusY, rotation }) => {
   return (
      <Ellipse
         x={x}
         y={y}
         radiusX={radiusX}
         radiusY={radiusY}
         rotation={rotation}
         fill='darkgrey'
         stroke='black'
         strokeWidth={1}
      />
   );
};

export default Chair;

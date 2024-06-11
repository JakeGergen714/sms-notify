/** @format */

import React, { useState, useEffect, useRef } from "react";
import { Image } from "react-konva";

const CustomShape = ({ xPos, yPos, width, height, onDragEnd, onDragStart = null, onClick }) => {
   const [image, setImage] = useState(null);

   useEffect(() => {
      const img = new window.Image();
      img.src = `${process.env.PUBLIC_URL}/icons/circle-table.png`;
      img.onload = () => {
         setImage(img);
      };
   }, []);

   return (
      <Image
         image={image}
         x={xPos}
         y={yPos}
         width={width}
         height={height}
         draggable
         onDragEnd={onDragEnd}
         onDragStart={onDragStart}
         onClick={onClick}
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
         }}
      />
   );
};

export default CustomShape;

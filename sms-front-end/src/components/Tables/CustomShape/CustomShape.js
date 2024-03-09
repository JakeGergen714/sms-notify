/** @format */

import React, { useState, useEffect } from "react";
import { Image } from "react-konva";

const CustomShape = ({ xPos, yPos, width, height, onDragEnd, onClick }) => {
   const [image, setImage] = useState(null);

   useEffect(() => {
      const img = new window.Image();
      img.src = `${process.env.PUBLIC_URL}/circle-table.png`;
      img.onload = () => {
         setImage(img);
      };
   }, []);

   return (
      <Image
         image={image}
         x={xPos}
         y={yPos}
         // Note: Konva's Image component doesn't have a radius prop. If you're trying to make it circular, you might need to use a mask or crop.
         width={width} // Assuming you want the diameter for width
         height={height} // Assuming you want the diameter for height
         draggable
         onDragEnd={onDragEnd}
         onClick={onClick}
      />
   );
};

export default CustomShape;

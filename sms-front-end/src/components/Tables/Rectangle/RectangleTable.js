/** @format */
import React from "react";
import { Rect, Group } from "react-konva";
import RectangleChair from "../Chair/RectangleChair";

const distributeChairs = (minWidth, minHeight, chairCount, disableTop, disableBottom, disableLeft, disableRight) => {
   const chairs = [];
   const chairDistribution = [0, 0, 0, 0];
   const chairSize = 21;

   // Define which sides are enabled
   const enabledSides = [];
   if (!disableRight) enabledSides.push(0);
   if (!disableLeft) enabledSides.push(1);
   if (!disableTop) enabledSides.push(2);
   if (!disableBottom) enabledSides.push(3);

   // Distribute chairs across enabled sides
   for (let i = 0; i < chairCount; i++) {
      const side = enabledSides[i % enabledSides.length];
      chairDistribution[side] += 1;
   }

   // Calculate additional width and height based on the number of chairs
   const additionalWidthTop = disableTop
      ? 0
      : Math.max(0, chairDistribution[2] - Math.floor(minWidth / chairSize)) * chairSize;
   const additionalWidthBottom = disableBottom
      ? 0
      : Math.max(0, chairDistribution[3] - Math.floor(minWidth / chairSize)) * chairSize;
   const additionalHeightRight = disableRight
      ? 0
      : Math.max(0, chairDistribution[0] - Math.floor(minHeight / chairSize)) * chairSize;
   const additionalHeightLeft = disableLeft
      ? 0
      : Math.max(0, chairDistribution[1] - Math.floor(minHeight / chairSize)) * chairSize;

   // Use the maximum of the additional widths and heights
   const width = minWidth + Math.max(additionalWidthTop, additionalWidthBottom);
   const height = minHeight + Math.max(additionalHeightRight, additionalHeightLeft);

   const chairWidth = 20;
   const chairHeight = 10;

   // Distribute chairs around the rectangle
   for (let side = 0; side < 4; side++) {
      const count = chairDistribution[side];
      let spacing, offset;

      switch (side) {
         case 0: // Right side
            if (!disableRight) {
               spacing = height / (count + 1);
               for (let i = 0; i < count; i++) {
                  offset = spacing * (i + 1);
                  chairs.push({
                     x: width - chairHeight / 2,
                     y: offset - chairWidth / 2,
                     width: chairHeight,
                     height: chairWidth,
                  });
               }
            }
            break;

         case 1: // Left side
            if (!disableLeft) {
               spacing = height / (count + 1);
               for (let i = 0; i < count; i++) {
                  offset = spacing * (i + 1);
                  chairs.push({
                     x: -chairHeight / 2,
                     y: height - offset - chairWidth / 2,
                     width: chairHeight,
                     height: chairWidth,
                  });
               }
            }
            break;

         case 2: // Top side
            if (!disableTop) {
               spacing = width / (count + 1);
               for (let i = 0; i < count; i++) {
                  offset = spacing * (i + 1);
                  chairs.push({
                     x: offset - chairWidth / 2,
                     y: -chairHeight / 2,
                     width: chairWidth,
                     height: chairHeight,
                  });
               }
            }
            break;

         case 3: // Bottom side
            if (!disableBottom) {
               spacing = width / (count + 1);
               for (let i = 0; i < count; i++) {
                  offset = spacing * (i + 1);
                  chairs.push({
                     x: width - offset - chairWidth / 2,
                     y: height - chairHeight / 2,
                     width: chairWidth,
                     height: chairHeight,
                  });
               }
            }
            break;
      }
   }

   return { chairs, width, height };
};

const RectangleTable = ({
   xPos,
   yPos,
   minWidth,
   minHeight,
   onDragEnd,
   onDragStart = null,
   onClick,
   chairCount,
   disableTop = false,
   disableBottom = false,
   disableLeft = false,
   disableRight = false,
}) => {
   const { chairs, width, height } = distributeChairs(
      minWidth,
      minHeight,
      chairCount,
      disableTop,
      disableBottom,
      disableLeft,
      disableRight
   );

   const chairComponents = chairs.map((chair, index) => (
      <RectangleChair
         key={index}
         x={chair.x}
         y={chair.y}
         width={chair.width}
         height={chair.height}
         fill='white'
         rotation={0}
      />
   ));

   return (
      <Group
         className='rectangle-table'
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
         {chairComponents}
         <Rect x={0} y={0} height={height} width={width} fill='gray' stroke='black' strokeWidth={1} />
      </Group>
   );
};

export default RectangleTable;

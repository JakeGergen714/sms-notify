/** @format */

import React, { useState } from "react";
import "./SeatMap.css";
import { v4 as uuidv4 } from "uuid";

import TableShape from "../TableShape/TableShape";

const SeatMap = () => {
   const tableType = ["rectangle", "circle"];
   const [currentTableType, setCurrentTableType] = useState(tableType[0]);
   const [shapes, setShapes] = useState([]);
   const [actionInProgress, setActionInProgress] = useState(false);

   const handleCanvasClick = (event) => {
      if (event.target !== event.currentTarget || actionInProgress) return;
      const rect = event.target.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      const newShape = { id: uuidv4(), x, y, tableType: currentTableType };

      setShapes([...shapes, newShape]);
   };

   return (
      <div className='seat-map-container'>
         <div className='table-type-selector'>
            <button onClick={() => setCurrentTableType("circle")}>Circle Table</button>
            <button onClick={() => setCurrentTableType("rectangle")}>Rectangle Table</button>
         </div>
         <div className='seat-map' onClick={handleCanvasClick}>
            {shapes.map((shape) => (
               <TableShape key={shape.id} shape={shape} setShapes={setShapes} setActionInProgress={setActionInProgress} />
            ))}
         </div>
      </div>
   );
};

export default SeatMap;

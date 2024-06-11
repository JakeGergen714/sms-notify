/** @format */

import React, { useRef, useState, useEffect } from "react";
import axios from "axios";

import "./RestaurantPortal.css";

const RestaurantPortal = () => {
   const [restaurants, setRestaurants] = useState({});
   const [selectedRestaurant, setSelectedRestaurant] = userState(null);
   
   const fetchRestaurants = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/floorMaps")
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            if (floorPlan != null) {
               //If a floor plan is currently selected, reload it with the latest data
               setFloorPlan(res.data.filter((updatedFloorPlan) => updatedFloorPlan.floorMapId == floorPlan.floorMapId)[0]);
            }
         })
         .catch((err) => {
            console.log("load floor plans failed");
            console.error(err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   };

   return <div className='page-header row'></div>;
};

export default RestaurantPortal;

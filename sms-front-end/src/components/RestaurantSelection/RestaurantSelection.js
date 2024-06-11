/** @format */

import React, { useState, useEffect } from "react";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const RestaurantSelection = () => {
   const { setSelectedRestaurant } = useUser();
   const [restaurants, setRestaurants] = useState([]);
   const navigate = useNavigate();

   const loadRestaurants = () => {
      console.log(process.env.REACT_APP_API_URL + "/restaurants");
      axios
         .get(process.env.REACT_APP_API_URL + "/restaurants")
         .then((res) => {
            setRestaurants(res.data); // Set state here after fetching
            console.log(res.data);
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setRestaurants([]); // Set to an empty array in case of error
         });
   };

   useEffect(() => {
      loadRestaurants();
   }, []); // Empty dependency array means this effect runs once after initial render

   function getUniqueFloorMaps(restaurant) {
      const allFloorMaps = restaurant.serviceTypes.flatMap((serviceType) => serviceType.floorMaps);

      const uniqueFloorMaps = [];
      const mapIds = new Set();

      allFloorMaps.forEach((floorMap) => {
         if (!mapIds.has(floorMap.floorMapId)) {
            uniqueFloorMaps.push(floorMap);
            mapIds.add(floorMap.floorMapId);
         }
      });

      console.log(uniqueFloorMaps);

      return uniqueFloorMaps;
   }

   const handleSelect = (restaurant) => {
      restaurant.floorMaps = getUniqueFloorMaps(restaurant);
      console.log("Selected restaurant", restaurant);

      setSelectedRestaurant(restaurant);
      navigate("/home");
   };

   return (
      <div>
         <h1>Select a Restaurant</h1>
         <ul>
            {restaurants.map((restaurant, index) => (
               <li key={index} onClick={() => handleSelect(restaurant)}>
                  {restaurant.name}
               </li>
            ))}
         </ul>
      </div>
   );
};

export default RestaurantSelection;

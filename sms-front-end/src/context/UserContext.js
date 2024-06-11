/** @format */

import React, { createContext, useState, useContext, useEffect } from "react";

// Create the context
const UserContext = createContext();

// Define the expiration time in milliseconds (e.g., 1 hour = 3600000 milliseconds)
const EXPIRATION_TIME = 360000; // 1 hour

// Provider component
export const UserProvider = ({ children }) => {
   const [selectedRestaurant, setSelectedRestaurant] = useState(() => {
      const savedData = localStorage.getItem("selectedRestaurant");
      if (savedData) {
         const { restaurant, timestamp } = JSON.parse(savedData);
         const currentTime = new Date().getTime();
         if (currentTime - timestamp < EXPIRATION_TIME) {
            return restaurant;
         } else {
            localStorage.removeItem("selectedRestaurant");
            return null;
         }
      }
      return null;
   });

   useEffect(() => {
      if (selectedRestaurant) {
         const dataToStore = {
            restaurant: selectedRestaurant,
            timestamp: new Date().getTime(),
         };
         localStorage.setItem("selectedRestaurant", JSON.stringify(dataToStore));
      } else {
         localStorage.removeItem("selectedRestaurant");
      }
   }, [selectedRestaurant]);

   return <UserContext.Provider value={{ selectedRestaurant, setSelectedRestaurant }}>{children}</UserContext.Provider>;
};

// Hook to use the context
export const useUser = () => useContext(UserContext);

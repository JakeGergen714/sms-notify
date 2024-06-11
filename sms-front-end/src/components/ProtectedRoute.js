/** @format */

// components/ProtectedRoute.js
import React from "react";
import { Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

const ProtectedRoute = ({ children }) => {
   const { selectedRestaurant } = useUser();

   if (!selectedRestaurant) {
      // Redirect to restaurant selection if no restaurant is selected
      console.log("Protection activated");
      return <Navigate to='/restaurant-selection' />;
   }

   return children;
};

export default ProtectedRoute;

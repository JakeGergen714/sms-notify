/** @format */

import React, { useState, useEffect } from "react";
import { Routes, Route, useLocation, Link } from "react-router-dom";
import "./ManageRestaurant.css"; // Custom CSS for additional styling

import { useUser } from "../../context/UserContext";
import { getRestaurant } from "../../service/AuthService/RestaurantService";
import axios from "axios";
import ManageRestaurantNavBar from "../NavBars/ManageRestaurantNavBar";
import ServiceSchedule from "./ServiceSchedule";
import SeatMap from "../SeatMap/SeatMap";

const ManageRestaurant = () => {
   const { selectedRestaurant, setSelectedRestaurant } = useUser(); // Get the current restaurant from context
   const [showModal, setShowModal] = useState(false);

   useEffect(() => {
      console.log("Updated service types:", selectedRestaurant.serviceTypes);
   }, [selectedRestaurant.serviceTypes]);

   return (
      <>
         <ManageRestaurantNavBar /> {/* Include the Navbar component */}
         <div className='manage-restaurant-content'>
            <Routes>
               <Route path='schedule' element={<ServiceSchedule></ServiceSchedule>} />
               <Route path='seating-plans' element={<SeatMap />} />
            </Routes>
         </div>
      </>
   );
};

export default ManageRestaurant;

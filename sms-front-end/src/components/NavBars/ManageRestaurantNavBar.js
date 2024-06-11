/** @format */

import React from "react";
import { Link, useLocation } from "react-router-dom";
import "./ManageRestaurantNavBar.css"; // Custom CSS for the Navbar

const ManageRestaurantNavBar = () => {
   const location = useLocation();

   return (
      <nav className='navbar'>
         <div className='navbar-container'>
            <ul className='nav-menu'>
               <li className={`nav-item ${location.pathname.includes("schedule") ? "active" : ""}`}>
                  <Link to='/home/restaurant/schedule' className='nav-links'>
                     Manage Schedule
                  </Link>
               </li>
               <li className={`nav-item ${location.pathname.includes("floormap") ? "active" : ""}`}>
                  <Link to='/home/restaurant/seating-plans' className='nav-links'>
                     Manage Seating Plans
                  </Link>
               </li>
            </ul>
         </div>
      </nav>
   );
};

export default ManageRestaurantNavBar;

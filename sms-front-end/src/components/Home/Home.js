/** @format */

import React, { useState } from "react";
import { Route, Routes, Link, useLocation } from "react-router-dom";
import WaitList from "../WaitList/WaitList";
import Reservations from "../Reservations/Reservations";
import Header from "../Header/Header";
import { PiUserListFill, PiUserList } from "react-icons/pi";
import { BsClockFill, BsClock } from "react-icons/bs";
import { PiMapTrifold, PiMapTrifoldBold } from "react-icons/pi";
import { IoSettingsOutline, IoSettingsSharp } from "react-icons/io5";
import { BsCalendar2, BsCalendar2Fill } from "react-icons/bs";
import "./Home.css";
import SideBarItem from "../SideBar/SiderBarItem";
import SeatMap from "../SeatMap/SeatMap";
import ManageService from "../ManageService/ManageService";
import ManageRestaurant from "../Restaurant/ManageRestaurant";

const Home = () => {
   const [activeItem, setActiveItem] = useState(0);

   return (
      <div className='home'>
         <div className='sidebar'>
            <Link to='/home/manage-service'>
               <SideBarItem
                  onClick={() => {
                     setActiveItem(0);
                  }}
                  isActive={activeItem === 0}
                  icon={PiUserList}
                  activeIcon={PiUserListFill}
               />
            </Link>
            <Link to='/home/reservations'>
               <SideBarItem
                  onClick={() => {
                     setActiveItem(1);
                  }}
                  isActive={activeItem === 1}
                  icon={BsClock}
                  activeIcon={BsClockFill}
               />
            </Link>
            <Link to='/home/restaurant/schedule'>
               <SideBarItem
                  onClick={() => {
                     setActiveItem(3);
                  }}
                  isActive={activeItem === 3}
                  icon={BsCalendar2}
                  activeIcon={BsCalendar2Fill}
               />
            </Link>
            <Link to='/home/settings'>
               <SideBarItem
                  onClick={() => {
                     setActiveItem(4);
                  }}
                  isActive={activeItem === 4}
                  icon={IoSettingsOutline}
                  activeIcon={IoSettingsSharp}
               />
            </Link>
         </div>
         <div className='main-content'>
            <Routes>
               <Route path='manage-service' element={<ManageService />} />
               <Route path='reservations' element={<Reservations />} />
               <Route path='restaurant/*' element={<ManageRestaurant />} />
               <Route path='/' element={<ManageService />} /> {/* Default route */}
            </Routes>
         </div>
      </div>
   );
};

export default Home;

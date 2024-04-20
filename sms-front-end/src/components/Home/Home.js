/** @format */

import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import WaitList from "../WaitList/WaitList";
import Reservations from "../Reservations/Reservations";
import Header from "../Header/Header";
import { PiUserListFill, PiUserList } from "react-icons/pi";
import { BsClockFill, BsClock } from "react-icons/bs";
import { PiMapTrifold, PiMapTrifoldBold } from "react-icons/pi";

import "./Home.css";
import SideBarItem from "../SideBar/SiderBarItem";
import SeatMap from "../SeatMap/SeatMap";
import ManageService from "../ManageService/ManageService";

const Home = (content) => {
   const [activePage, setActivePage] = useState(0);

   return (
      <div className='home'>
         <div className='sidebar'>
            <SideBarItem
               onClick={() => {
                  setActivePage(0);
               }}
               isActive={activePage == 0}
               text='Wait List'
               icon={PiUserList}
               activeIcon={PiUserListFill}
            />
            <SideBarItem
               onClick={() => {
                  setActivePage(1);
               }}
               isActive={activePage == 1}
               text='Reservations'
               icon={BsClock}
               activeIcon={BsClockFill}
            />
            <SideBarItem
               onClick={() => {
                  setActivePage(2);
               }}
               isActive={activePage == 2}
               text='Reservations'
               icon={PiMapTrifold}
               activeIcon={PiMapTrifoldBold}
            />
         </div>
         <div className='main-content'>
            <Header></Header>
            {activePage === 0 && <ManageService></ManageService>}
            {activePage === 1 && <Reservations />}
            {activePage === 2 && <SeatMap />}
         </div>
      </div>
   );
};

export default Home;

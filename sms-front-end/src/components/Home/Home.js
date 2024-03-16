/** @format */

import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import WaitList from "../WaitList/WaitList";
import Reservations from "../Reservations/Reservations";
import Header from "../Header/Header";
import { PiUserListFill, PiUserList } from "react-icons/pi";
import { BsClockFill, BsClock } from "react-icons/bs";
import "./Home.css";
import SideBarItem from "../SideBar/SiderBarItem";

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
         </div>
         <div className='main-content'>
            <Header></Header>
            {activePage === 0 && <WaitList></WaitList>}
            {activePage === 1 && <Reservations />}
         </div>
      </div>
   );
};

export default Home;

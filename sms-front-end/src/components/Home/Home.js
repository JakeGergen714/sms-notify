/** @format */

import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import SideBar from "../SideBar/SideBar";
import WaitList from "../WaitList/WaitList";
import Reservations from "../Reservations/Reservations";
import Header from "../Header/Header";

import "./Home.css";

const Home = (content) => {
   const [searchParams, setSearchParams] = useSearchParams();

   function getActivePage() {
      const activePage = searchParams.get("active");
      if (activePage === "waitlist") {
         return 0;
      }
      if (activePage === "reservations") {
         return 1;
      }
      return 0;
   }

   return (
      <div className='home'>
         <SideBar activePage={getActivePage()} />
         <div className='main-content'>
            <Header></Header>
            {getActivePage() === 0 && <WaitList></WaitList>}
            {getActivePage() === 1 && <Reservations />}
         </div>
      </div>
   );
};

export default Home;

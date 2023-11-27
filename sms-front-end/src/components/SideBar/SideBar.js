/** @format */

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { SlGraph } from "react-icons/sl";
import { PiUserListFill, PiUserList } from "react-icons/pi";
import { AuthContext } from "../../AuthContext"; // Update the path as necessary

import { BsClockFill, BsClock } from "react-icons/bs";

import "./SideBar.css";
const SideBar = ({ activePage }) => {
   const [waitListActive, setWaitListActive] = useState(activePage == 0 ? true : false);
   const [reservationsActive, setReservationsActive] = useState(activePage == 1 ? true : false);
   const navigate = useNavigate();
   const { recheckAuthentication } = useContext(AuthContext);

   return (
      <div className='sidebar'>
         <div
            className={"sidebar-item " + (waitListActive ? "active" : "not-active")}
            onClick={() => {
               recheckAuthentication().then((isAuthed) => {
                  if (isAuthed) {
                     console.log(isAuthed);
                     setWaitListActive(true);
                     setReservationsActive(false);
                     navigate("/home?active=waitlist");
                  } else {
                     navigate("/login");
                  }
               });
            }}>
            {waitListActive ? <PiUserListFill /> : <PiUserList />}
            <span className='icon-label'>Waitlist</span>
         </div>
         <div
            className={"sidebar-item " + (reservationsActive ? "active" : "not-active")}
            onClick={() => {
               setWaitListActive(false);
               setReservationsActive(true);
               navigate("/home?active=reservations");
            }}>
            {reservationsActive ? <BsClockFill /> : <BsClock />}
            <span className='icon-label'>Reservations</span>
         </div>
         <div className='sidebar-item'>
            <SlGraph />
            <span className='icon-label'>Analytics</span>
         </div>
      </div>
   );
};

export default SideBar;

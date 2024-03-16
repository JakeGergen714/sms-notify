/** @format */

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { SlGraph } from "react-icons/sl";
import { PiUserListFill, PiUserList } from "react-icons/pi";

import { BsClockFill, BsClock } from "react-icons/bs";

import "./SideBar.css";
const SideBar = ({ activePage }) => {
   const [waitListActive, setWaitListActive] = useState(activePage == 0 ? true : false);
   const [reservationsActive, setReservationsActive] = useState(activePage == 1 ? true : false);
   const navigate = useNavigate();

   return <div className='sidebar'></div>;
};

export default SideBar;

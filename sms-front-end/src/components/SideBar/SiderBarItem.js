/** @format */

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { SlGraph } from "react-icons/sl";
import { PiUserListFill, PiUserList } from "react-icons/pi";
import { AuthContext } from "../../AuthContext"; // Update the path as necessary

import { BsClockFill, BsClock } from "react-icons/bs";

import "./SideBar.css";
const SideBarItem = ({ isActive, text, icon: Icon, activeIcon: ActiveIcon, onClick }) => {
   return (
      <div className={"sidebar-item " + (isActive ? "active" : "not-active")} onClick={onClick}>
         {isActive ? <ActiveIcon /> : <Icon />}
         <span className='icon-label'>{text}</span>
      </div>
   );
};

export default SideBarItem;

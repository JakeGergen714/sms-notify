/** @format */

import React from "react";
import WaitListAddForm from "../WaitListAddForm/WaitListAddForm";

import "./Header.css";
const Header = () => {
   return (
      <div className='page-header row'>
         <div className='business-name col-lg-10'>Pepper's Diner</div>
         <div className='col-md-1'>
            <WaitListAddForm />
         </div>
      </div>
   );
};

export default Header;

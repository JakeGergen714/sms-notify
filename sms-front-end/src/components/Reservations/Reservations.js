/** @format */

import React from "react";
import axios from "axios";

import "./Reservations.css";

const Reservations = () => {
   axios.defaults.withCredentials = true;

   const fetchData = () => {
      return axios
         .get(process.env.REACT_APP_API_URL + "/available", {
            reservationDay: "2024-04-26", // Ensure this is formatted as a date string if necessary
            partySize: 2,
         })
         .then((res) => {
            console.log(res.data);
            return res.data;
         })
         .catch((err) => {
            console.error(err);
            return [];
         });
   };

   fetchData();

   return (
      <div id='reservations-container'>
         <div className='header'>
            <h1 className='date-header'>September 23, 2023</h1>
            <table className='table table-dark'>
               <thead>
                  <tr>
                     <th scope='table-col'>Time</th>
                     <th scope='table-col'>Size</th>
                     <th scope='table-col'>Party</th>
                     <th scope='table-col'>Notes</th>
                     <th scope='table-col'>Actions</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td scope='table-row'>7:30 pm</td>
                     <td>8</td>
                     <td>
                        <div className='party-name'>Jake Gergen</div>
                        <div className='party-phone'>714905 4056</div>
                        <div className='party-date-added'>Added 9/23/23, 7:13pm</div>
                     </td>
                     <td>inside</td>
                     <td>
                        <input type='submit' id='table-action' className='btn btn-success btn-block' value='Seated' />
                        <input type='submit' id='table-action' className='btn btn-danger btn-block' value='Cancel' />
                        <input type='submit' id='table-action' className='btn btn-primary btn-block' value='Edit' />
                     </td>
                  </tr>
               </tbody>
            </table>
         </div>
      </div>
   );
};

export default Reservations;

/** @format */

import React, { useState, useEffect } from "react";
import { IoMdText } from "react-icons/io";

import axios from "axios";
import "./WaitList.css";

axios.defaults.withCredentials = true;

const fetchData = () => {
   return axios
      .get("http://localhost:8082/waitList")
      .then((res) => {
         console.log(res.data);
         return res.data;
      })
      .catch((err) => {
         console.error(err);
         return [];
      });
};

function formatTime(timeToFormat) {
   const date = new Date(timeToFormat);

   let hours = date.getHours();
   const ampm = hours >= 12 ? "PM" : "AM";

   // Convert to 12-hour format
   hours = hours % 12;
   // 12:00 is represented as 12, not 0
   hours = hours ? hours : 12;

   const minutes = String(date.getMinutes()).padStart(2, "0");

   return `${hours}:${minutes} ${ampm}`;
}

function calculateWaitTimeMinutes(time) {
   // Parse the given time
   const givenTime = new Date(time);
   // Get the current time
   const currentTime = new Date();
   // Calculate the difference in milliseconds
   const differenceMilliseconds = currentTime - givenTime;
   // Convert the difference to minutes
   const differenceMinutes = Math.floor(differenceMilliseconds / (1000 * 60));
   return differenceMinutes;
}

const WaitList = () => {
   const [waitListItems, setWaitListItems] = useState([]);

   useEffect(() => {
      fetchData().then((data) => setWaitListItems(data));
   }, []);

   return (
      <div id='WaitListContainer'>
         <div className='header'>
            <table className='table table-dark table-striped waitlist-table'>
               <thead>
                  <tr>
                     <th className='waitlist-index' scope='table-col'>
                        #
                     </th>
                     <th className='waitlist-th waitlist-small-col' scope='table-col'>
                        Customer
                     </th>
                     <th className='waitlist-th waitlist-extra-small-col' scope='table-col'>
                        Size
                     </th>
                     <th className='waitlist-th waitlist-extra-small-col' scope='table-col'>
                        Wait Time
                     </th>
                     <th className='waitlist-th waitlist-extra-small-col center' scope='table-col'>
                        Check-In Time
                     </th>
                     <th className='waitlist-th' scope='table-col'>
                        Notes
                     </th>
                     <th className='waitlist-th' scope='table-col'>
                        Notify
                     </th>
                     <th className='waitlist-th' scope='table-col'>
                        Actions
                     </th>
                  </tr>
               </thead>
               <tbody className='waitlist-tbody'>
                  {waitListItems.map((item, index) => (
                     <tr key={index}>
                        <td className='waitlist-td waitlist-index' scope='table-row'>
                           {index + 1}
                        </td>
                        <td className='waitlist-small-col'>{item.customerName}</td>
                        <td className='td-party-size waitlist-extra-small-col'>{item.partySize}</td>
                        <td className='waitlist-extra-small-col'>
                           {calculateWaitTimeMinutes(item.checkInTime) + " minutes"}
                        </td>
                        <td className='waitlist-extra-small-col center'>{formatTime(item.checkInTime)}</td>
                        <td className='waitlist-td'>{item.notes.join(", ")}</td>
                        <td className='waitlist-td'>
                           <IoMdText
                              className='sms-icon'
                              onClick={() => {
                                 console.log("hello world");
                              }}
                           />
                        </td>
                        <td className='waitlist-td'>
                           <input type='submit' className='btn btn-success btn-block' value='Seated' />
                           <input type='submit' className='btn btn-danger btn-block' value='Cancel' />
                           <input type='submit' className='btn btn-primary btn-block' value='Edit' />
                        </td>
                     </tr>
                  ))}
               </tbody>
            </table>
         </div>
      </div>
   );
};

function fetchWaitListItems() {}

export default WaitList;

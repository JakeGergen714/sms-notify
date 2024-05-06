/** @format */

import React, { useState } from "react";
import axios from "axios";

const Restaurant = () => {
   const baseUrl = "http://192.168.1.241:8090";

   const [restaurant, setRestaurant] = useState({
      restaurantId: null,
      name: "",
      address: "",
      serviceTypes: [],
   });

   const handleInputChange = (event) => {
      const { name, value } = event.target;
      setRestaurant((prev) => ({ ...prev, [name]: value }));
   };

   const handleServiceTypeChange = (index, event) => {
      const { name, value } = event.target;
      const updatedServiceTypes = [...restaurant.serviceTypes];
      updatedServiceTypes[index] = { ...updatedServiceTypes[index], [name]: value };
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const handleServiceScheduleChange = (typeIndex, scheduleIndex, event) => {
      const { name, value } = event.target;
      const updatedServiceTypes = [...restaurant.serviceTypes];
      const updatedSchedules = [...updatedServiceTypes[typeIndex].serviceSchedules];
      updatedSchedules[scheduleIndex] = { ...updatedSchedules[scheduleIndex], [name]: value };
      updatedServiceTypes[typeIndex].serviceSchedules = updatedSchedules;
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const handleSubmit = async (event) => {
      event.preventDefault();
      try {
         const response = await axios.post(`${baseUrl}/restaurant`, restaurant);
         console.log("Restaurant and associated data added:", response.data);
      } catch (error) {
         console.error("Failed to add restaurant data", error);
      }
   };

   const fetchMyRestaurnt = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/restaurant")
         .then((res) => {
            console.log(res);
         })
         .catch((err) => {
            console.log("fetch my restaurant");
            console.error(err);
         });
   };

   return (
      <div>
         <button onClick={() => fetchMyRestaurnt()}> test </button>

         <h1>Restaurant Component</h1>
         <form onSubmit={handleSubmit}>
            <h2>Restaurant Details</h2>
            <input name='name' value={restaurant.name} onChange={handleInputChange} placeholder='Name' />
            <input name='address' value={restaurant.address} onChange={handleInputChange} placeholder='Address' />
            {/* Dynamically add service types and their schedules here */}
            {restaurant.serviceTypes.map((serviceType, index) => (
               <div key={index}>
                  <h3>Service Type {index + 1}</h3>
                  <input
                     name='name'
                     value={serviceType.name}
                     onChange={(e) => handleServiceTypeChange(index, e)}
                     placeholder='Service Type Name'
                  />
                  {serviceType.serviceSchedules.map((schedule, sIndex) => (
                     <div key={sIndex}>
                        <input
                           name='dayOfWeek'
                           value={schedule.dayOfWeek}
                           onChange={(e) => handleServiceScheduleChange(index, sIndex, e)}
                           placeholder='Day of Week'
                        />
                        <input
                           name='startTime'
                           value={schedule.startTime}
                           onChange={(e) => handleServiceScheduleChange(index, sIndex, e)}
                           placeholder='Start Time'
                        />
                        <input
                           name='endTime'
                           value={schedule.endTime}
                           onChange={(e) => handleServiceScheduleChange(index, sIndex, e)}
                           placeholder='End Time'
                        />
                     </div>
                  ))}
               </div>
            ))}
            <button type='submit'>Submit All Data</button>
         </form>
      </div>
   );
};

export default Restaurant;

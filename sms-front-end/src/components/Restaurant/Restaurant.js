/** @format */

import React, { useState } from "react";
import axios from "axios";

const Restaurant = () => {
   const baseUrl = "http://192.168.1.241:8090";

   const [restaurant, setRestaurant] = useState({
      businessId: "",
      name: "",
      address: "",
      serviceTypes: [],
   });

   const [serviceType, setServiceType] = useState({
      serviceTypeId: "",
      restaurantId: "",
      name: "",
      serviceSchedules: [],
   });

   const handleRestaurantChange = (e) => {
      const { name, value } = e.target;
      setRestaurant((prev) => ({ ...prev, [name]: value }));
   };

   const handleServiceTypeChange = (e) => {
      const { name, value } = e.target;
      setServiceType((prev) => ({ ...prev, [name]: value }));
   };

   const addServiceSchedule = () => {
      const newSchedule = {
         serviceScheduleId: "",
         dayOfWeek: "",
         startTime: "",
         endTime: "",
      };
      setServiceType((prev) => ({
         ...prev,
         serviceSchedules: [...prev.serviceSchedules, newSchedule],
      }));
   };

   const handleServiceScheduleChange = (index, e) => {
      const updatedSchedules = serviceType.serviceSchedules.map((schedule, idx) => {
         if (idx === index) {
            return { ...schedule, [e.target.name]: e.target.value };
         }
         return schedule;
      });
      setServiceType((prev) => ({ ...prev, serviceSchedules: updatedSchedules }));
   };

   const logRestaurants = async () => {
      try {
         const response = await axios.get(`${baseUrl}/restaurant`, { params: { businessId: restaurant.businessId } });
         console.log(response.data);
      } catch (error) {
         console.error("Failed to fetch restaurant", error);
      }
   };

   const addRestaurant = async (event) => {
      event.preventDefault();
      try {
         const response = await axios.post(`${baseUrl}/restaurant`, restaurant);
         console.log("Added restaurant:", response.data);
      } catch (error) {
         console.error("Failed to add restaurant", error);
      }
   };

   const updateServiceType = async (event) => {
      event.preventDefault();
      try {
         const response = await axios.put(`${baseUrl}/servicetype`, serviceType);
         console.log("Updated service type:", response.data);
      } catch (error) {
         console.error("Failed to update service type", error);
      }
   };

   return (
      <div>
         <h1>Restaurant Component</h1>
         <button onClick={logRestaurants}>Log Restaurants</button>
         <form onSubmit={addRestaurant}>
            <h2>Add Restaurant</h2>
            <input
               name='businessId'
               value={restaurant.businessId}
               onChange={handleRestaurantChange}
               placeholder='Business ID'
            />
            <input name='name' value={restaurant.name} onChange={handleRestaurantChange} placeholder='Name' />
            <input name='address' value={restaurant.address} onChange={handleRestaurantChange} placeholder='Address' />
            <button type='submit'>Add Restaurant</button>
         </form>
         <form onSubmit={updateServiceType}>
            <h2>Update Service Type</h2>
            <input
               name='serviceTypeId'
               value={serviceType.serviceTypeId}
               onChange={handleServiceTypeChange}
               placeholder='Service Type ID'
            />
            <input
               name='restaurantId'
               value={serviceType.restaurantId}
               onChange={handleServiceTypeChange}
               placeholder='Restaurant ID'
            />
            <input name='name' value={serviceType.name} onChange={handleServiceTypeChange} placeholder='Service Type Name' />
            {serviceType.serviceSchedules.map((schedule, index) => (
               <div key={index}>
                  <input
                     name='dayOfWeek'
                     value={schedule.dayOfWeek}
                     onChange={(e) => handleServiceScheduleChange(index, e)}
                     placeholder='Day of Week'
                  />
                  <input
                     name='startTime'
                     value={schedule.startTime}
                     onChange={(e) => handleServiceScheduleChange(index, e)}
                     placeholder='Start Time'
                  />
                  <input
                     name='endTime'
                     value={schedule.endTime}
                     onChange={(e) => handleServiceScheduleChange(index, e)}
                     placeholder='End Time'
                  />
               </div>
            ))}
            <button type='button' onClick={addServiceSchedule}>
               Add Service Schedule
            </button>
            <button type='submit'>Update Service Type</button>
         </form>
      </div>
   );
};

export default Restaurant;

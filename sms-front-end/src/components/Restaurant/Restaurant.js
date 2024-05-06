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

   const [serviceSchedule, setServiceSchedule] = useState({
      serviceScheduleId: "",
      dayOfWeek: "",
      startTime: "",
      endTime: "",
   });

   const handleInputChange = (event, setter) => {
      const { name, value } = event.target;
      setter((prev) => ({ ...prev, [name]: value }));
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

   const handleServiceScheduleSubmit = async (event) => {
      event.preventDefault();
      // Assuming the endpoint to handle service schedule is available
      try {
         const response = await axios.post(`${baseUrl}/serviceschedule`, serviceSchedule);
         console.log("Service Schedule Updated:", response.data);
      } catch (error) {
         console.error("Failed to update service schedule", error);
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
               onChange={(e) => handleInputChange(e, setRestaurant)}
               placeholder='Business ID'
            />
            <input
               name='name'
               value={restaurant.name}
               onChange={(e) => handleInputChange(e, setRestaurant)}
               placeholder='Name'
            />
            <input
               name='address'
               value={restaurant.address}
               onChange={(e) => handleInputChange(e, setRestaurant)}
               placeholder='Address'
            />
            <button type='submit'>Add Restaurant</button>
         </form>
         <form onSubmit={updateServiceType}>
            <h2>Update Service Type</h2>
            <input
               name='serviceTypeId'
               value={serviceType.serviceTypeId}
               onChange={(e) => handleInputChange(e, setServiceType)}
               placeholder='Service Type ID'
            />
            <input
               name='restaurantId'
               value={serviceType.restaurantId}
               onChange={(e) => handleInputChange(e, setServiceType)}
               placeholder='Restaurant ID'
            />
            <input
               name='name'
               value={serviceType.name}
               onChange={(e) => handleInputChange(e, setServiceType)}
               placeholder='Service Type Name'
            />
            <button type='submit'>Update Service Type</button>
         </form>
         <form onSubmit={handleServiceScheduleSubmit}>
            <h2>Service Schedule</h2>
            <input
               name='serviceScheduleId'
               value={serviceSchedule.serviceScheduleId}
               onChange={(e) => handleInputChange(e, setServiceSchedule)}
               placeholder='Schedule ID'
            />
            <input
               name='dayOfWeek'
               value={serviceSchedule.dayOfWeek}
               onChange={(e) => handleInputChange(e, setServiceSchedule)}
               placeholder='Day of Week'
            />
            <input
               name='startTime'
               value={serviceSchedule.startTime}
               onChange={(e) => handleInputChange(e, setServiceSchedule)}
               placeholder='Start Time'
            />
            <input
               name='endTime'
               value={serviceSchedule.endTime}
               onChange={(e) => handleInputChange(e, setServiceSchedule)}
               placeholder='End Time'
            />
            <button type='submit'>Update Schedule</button>
         </form>
      </div>
   );
};

export default Restaurant;

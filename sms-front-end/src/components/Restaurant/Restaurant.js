/** @format */

import React, { useState } from "react";
import axios from "axios";
import { Dropdown } from "react-bootstrap"; // Make sure you have this import if using React-Bootstrap

const Restaurant = () => {
   const [restaurant, setRestaurant] = useState({
      restaurantId: null,
      businessId: null,
      name: "",
      address: "",
      serviceTypes: [],
   });

   const [floorPlans, setFloorPlans] = useState([]); // This should hold all floor plans

   useEffect(() => {
      loadFloorPlans();
   }, []); // Empty dependency array means this effect runs once after initial render

   const handleInputChange = (e) => {
      const { name, value } = e.target;
      setRestaurant((prev) => ({ ...prev, [name]: value }));
   };

   const handleServiceTypeChange = (index, e) => {
      const { name, value } = e.target;
      const updatedServiceTypes = [...restaurant.serviceTypes];
      updatedServiceTypes[index] = { ...updatedServiceTypes[index], [name]: value };
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const handleServiceScheduleChange = (typeIndex, scheduleIndex, e) => {
      const { name, value } = e.target;
      const updatedServiceTypes = [...restaurant.serviceTypes];
      const updatedSchedules = [...updatedServiceTypes[typeIndex].serviceSchedules];
      updatedSchedules[scheduleIndex] = { ...updatedSchedules[scheduleIndex], [name]: value };
      updatedServiceTypes[typeIndex].serviceSchedules = updatedSchedules;
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const addServiceType = () => {
      const newServiceType = {
         serviceTypeId: null,
         restaurantId: restaurant.restaurantId,
         name: "",
         serviceSchedules: [],
         floorPlan: null, // Initialize without a specific floor plan
      };
      setRestaurant((prev) => ({ ...prev, serviceTypes: [...prev.serviceTypes, newServiceType] }));
   };

   const addServiceSchedule = (typeIndex) => {
      const newSchedule = { serviceScheduleId: null, dayOfWeek: "", startTime: "", endTime: "" };
      const updatedServiceTypes = [...restaurant.serviceTypes];
      updatedServiceTypes[typeIndex].serviceSchedules.push(newSchedule);
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const selectFloorPlan = (typeIndex, floorPlan) => {
      const updatedServiceTypes = [...restaurant.serviceTypes];
      updatedServiceTypes[typeIndex].floorPlan = floorPlan;
      setRestaurant((prev) => ({ ...prev, serviceTypes: updatedServiceTypes }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      try {
         const response = await axios.post("http://192.168.1.241:8090/restaurant", restaurant);
         console.log("Restaurant and all associated data added:", response.data);
      } catch (error) {
         console.error("Failed to submit restaurant data", error);
      }
   };

   return (
      <div>
         <h1>Restaurant Form</h1>
         <form onSubmit={handleSubmit}>
            <input
               type='text'
               name='businessId'
               value={restaurant.businessId || ""}
               onChange={handleInputChange}
               placeholder='Business ID'
            />
            <input
               type='text'
               name='name'
               value={restaurant.name}
               onChange={handleInputChange}
               placeholder='Restaurant Name'
            />
            <input
               type='text'
               name='address'
               value={restaurant.address}
               onChange={handleInputChange}
               placeholder='Address'
            />
            <button type='button' onClick={addServiceType}>
               Add Service Type
            </button>
            {restaurant.serviceTypes.map((type, tIndex) => (
               <div key={tIndex}>
                  <input
                     type='text'
                     name='name'
                     value={type.name}
                     onChange={(e) => handleServiceTypeChange(tIndex, e)}
                     placeholder='Service Type Name'
                  />
                  <Dropdown>
                     <Dropdown.Toggle variant='secondary' id='dropdown-basic'>
                        {type.floorPlan ? type.floorPlan.name : "Select a Floor Plan"}
                     </Dropdown.Toggle>
                     <Dropdown.Menu>
                        {floorPlans.map((fp) => (
                           <Dropdown.Item key={fp.id} onClick={() => selectFloorPlan(tIndex, fp)}>
                              {fp.name}
                           </Dropdown.Item>
                        ))}
                     </Dropdown.Menu>
                  </Dropdown>
                  <button type='button' onClick={() => addServiceSchedule(tIndex)}>
                     Add Service Schedule
                  </button>
                  {type.serviceSchedules.map((schedule, sIndex) => (
                     <div key={sIndex}>
                        <input
                           type='text'
                           name='dayOfWeek'
                           value={schedule.dayOfWeek}
                           onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
                           placeholder='Day of Week'
                        />
                        <input
                           type='text'
                           name='startTime'
                           value={schedule.startTime}
                           onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
                           placeholder='Start Time'
                        />
                        <input
                           type='text'
                           name='endTime'
                           value={schedule.endTime}
                           onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
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

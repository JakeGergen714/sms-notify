/** @format */

/** @format */

import React, { useState, useEffect } from "react";
import { BsCalendar2Plus } from "react-icons/bs";
import "./ServiceSchedule.css"; // Custom CSS for additional styling
import WeeklySchedule from "../WeeklySchedule/WeeklySchedule";
import AddServiceModal from "../modals/AddServiceModal";
import { useUser } from "../../context/UserContext";
import { getRestaurant } from "../../service/AuthService/RestaurantService";
import axios from "axios";

const ServiceSchedule = () => {
   const { selectedRestaurant, setSelectedRestaurant } = useUser(); // Get the current restaurant from context
   const [showModal, setShowModal] = useState(false);
   const [floorPlans, setFloorPlans] = useState([]);

   const loadFloorPlans = () => {
      axios
         .get(`${process.env.REACT_APP_API_URL}/restaurant/floormaps`, {
            params: { restaurantId: selectedRestaurant.restaurantId },
         })
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            console.log(res.data);
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   };

   useEffect(() => {
      if (selectedRestaurant && selectedRestaurant.serviceTypes) {
         loadFloorPlans();
      }
   }, []);

   const handleSaveService = (service) => {
      console.log(service);

      const serviceSchedules = [];

      service.selectedDaysOfWeek.forEach((day) => {
         serviceSchedules.push({
            dayOfWeek: day.toUpperCase(),
            startTime: service.startTime,
            endTime: service.endTime,
         });
      });

      const serviceTypeDTO = {
         restaurantId: selectedRestaurant.restaurantId,
         name: service.serviceName,
         floorMapIds: service.floorMapIds,
         serviceSchedules: serviceSchedules,
      };

      console.log(serviceTypeDTO);

      axios
         .post(process.env.REACT_APP_API_URL + "/restaurant/servicetype", serviceTypeDTO)
         .then(() => {
            getRestaurant(selectedRestaurant.restaurantId)
               .then((response) => {
                  console.log(response.data);
                  setSelectedRestaurant(response.data);
               })
               .catch((e) => console.error("Failed to update restaurant", e));
         })
         .catch((err) => {
            console.log("err");
            console.log(err);
         });

      setShowModal(false);
   };

   useEffect(() => {
      console.log("Updated service types:", selectedRestaurant.serviceTypes);
   }, [selectedRestaurant.serviceTypes]);

   return (
      <div className='service-schedule-container'>
         <div className='header'>
            <div className='add-service-button-container'>
               <div className='add-service-button' onClick={() => setShowModal(true)}>
                  <div className='add-service-button-content'>
                     <div className='add-service-icon'>
                        <BsCalendar2Plus />
                     </div>
                     <div className='add-service-text'>Add Service</div>
                  </div>
               </div>
            </div>
         </div>

         <div className='weekly-schedule-container'>
            <WeeklySchedule serviceTypes={selectedRestaurant.serviceTypes}></WeeklySchedule>
         </div>
         <AddServiceModal
            floorPlans={floorPlans}
            show={showModal}
            onHide={() => setShowModal(false)}
            onSave={handleSaveService}
         />
      </div>
   );
};

export default ServiceSchedule;

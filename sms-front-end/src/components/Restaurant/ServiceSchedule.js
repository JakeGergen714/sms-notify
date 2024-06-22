/** @format */

import React, { useState, useEffect } from "react";
import { BsCalendar2Plus } from "react-icons/bs";
import "./ServiceSchedule.css"; // Custom CSS for additional styling
import WeeklySchedule from "../WeeklySchedule/WeeklySchedule";
import AddServiceModal from "../modals/AddServiceModal";
import { useUser } from "../../context/UserContext";
import { getRestaurant } from "../../service/AuthService/RestaurantService";
import { Button, Form } from "react-bootstrap";
import { IoAdd } from "react-icons/io5";

import axios from "axios";
import AddServerModal from "../modals/AddServerModal";

const ServiceSchedule = () => {
   const { selectedRestaurant, setSelectedRestaurant } = useUser(); // Get the current restaurant from context
   const [showModal, setShowModal] = useState(false);
   const [showServerModal, setShowServerModal] = useState(false); // State to control server modal visibility
   const [floorPlans, setFloorPlans] = useState([]);

   const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
   const [servers, setServers] = useState([]);
   const [serviceName, setServiceName] = useState("");
   const [startTime, setStartTime] = useState("");
   const [endTime, setEndTime] = useState("");
   const [day, setDay] = useState("Sunday");
   const [showAddServer, setShowAddServer] = useState(false);
   const [newServerName, setNewServerName] = useState("");
   const [serverToEdit, setServerToEdit] = useState(null); // State to store server being edited

   const loadServers = () => {
      axios
         .get(`${process.env.REACT_APP_API_URL}/restaurant/servers`, {
            params: { restaurantId: selectedRestaurant.restaurantId },
         })
         .then((res) => {
            console.log(res.data);
            setServers(res.data.sort((a, b) => a.name.localeCompare(b.name))); // Sort alphabetically
         })
         .catch((err) => {
            console.log("load restaurants failed", err);
            setServers([]); // Set to an empty array in case of error
         });
   };

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
         loadServers();
      }
   }, []);

   const handleSaveService = (service) => {
      console.log(service);

      const serviceSchedules = [];
      console.log(service);

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

   const handleAddServer = (serverName) => {
      console.log(serverName);
      const newServer = {
         name: serverName,
         restaurantId: selectedRestaurant.restaurantId,
      };
      axios
         .post(`${process.env.REACT_APP_API_URL}/restaurant/server`, newServer)
         .then((res) => {
            console.log("Server added successfully:", res.data);
            setShowAddServer(false);
            setNewServerName("");
            setServers((prevServers) => [...prevServers, res.data].sort((a, b) => a.name.localeCompare(b.name)));
            // Update the servers list
         })
         .catch((err) => {
            console.error("Failed to add server", err);
         });
   };

   const handleEditServer = (newServerName) => {
      console.log(newServerName);
      const newServer = {
         serverId: serverToEdit.serverId,
         name: newServerName,
         restaurantId: selectedRestaurant.restaurantId,
      };

      console.log(newServer);

      axios
         .put(`${process.env.REACT_APP_API_URL}/restaurant/server`, newServer)
         .then((res) => {
            console.log("Server updated successfully:", res.data);
            setShowAddServer(false);
            setNewServerName("");

            // Update the servers list
            setServers((prevServers) =>
               prevServers
                  .map((server) => (server.serverId === newServer.serverId ? newServer : server))
                  .sort((a, b) => a.name.localeCompare(b.name))
            );
         })
         .catch((err) => {
            console.error("Failed to update server", err);
         });
   };

   const handleServerClick = (server) => {
      setServerToEdit(server);
      setShowServerModal(true);
   };

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
         <div className='service-schedule-content'>
            <div className='weekly-schedule-container'>
               <WeeklySchedule serviceTypes={selectedRestaurant.serviceTypes} />
            </div>
            <div className='edit-schedule-container'>
               <div className='servers-container'>
                  <div className='servers-container-content'>
                     <div>Servers</div>
                     <div className='server-panel add-server' onClick={() => setShowServerModal(true)}>
                        <div className='add-text'>
                           <IoAdd />
                        </div>
                     </div>
                     {servers &&
                        servers.map((server) => (
                           <div className='server-panel' key={server.serverId} onClick={() => handleServerClick(server)}>
                              <div className='server-name'>{server.name}</div>
                           </div>
                        ))}
                  </div>
               </div>
            </div>
         </div>

         <AddServiceModal
            floorPlans={floorPlans}
            show={showModal}
            onHide={() => setShowModal(false)}
            onSave={handleSaveService}
         />
         <AddServerModal
            show={showServerModal}
            onHide={() => {
               setShowServerModal(false);
               setServerToEdit(null);
            }}
            onSave={handleAddServer}
            onEdit={handleEditServer}
            serverToEdit={serverToEdit}
         />
      </div>
   );
};

export default ServiceSchedule;

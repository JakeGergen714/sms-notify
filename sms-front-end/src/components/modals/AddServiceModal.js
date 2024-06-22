/** @format */

import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const AddServiceModal = ({ show, onHide, onSave, floorPlans }) => {
   const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
   const servers = ["Jake", "Pepper", "Askim"];

   const [serviceName, setServiceName] = useState("");
   const [startTime, setStartTime] = useState("");
   const [endTime, setEndTime] = useState("");
   const [day, setDay] = useState("Sunday");
   const [selectedFloorPlans, setSelectedFloorPlans] = useState([]);
   const [selectedDaysOfWeek, setSelectedDaysOfWeek] = useState([]);
   const [selectedServers, setSelectedServers] = useState([]);

   const handleCheckboxChange = (event) => {
      const { value, checked } = event.target;
      setSelectedFloorPlans((prevSelected) =>
         checked ? [...prevSelected, value] : prevSelected.filter((id) => id !== value)
      );
   };

   const handleDayCheckboxChange = (event) => {
      const { value, checked } = event.target;
      setSelectedDaysOfWeek((prevSelected) =>
         checked ? [...prevSelected, value] : prevSelected.filter((id) => id !== value)
      );
   };

   const handleServerCheckBoxChange = (event) => {
      const { value, checked } = event.target;
      setSelectedServers((prevSelected) => (checked ? [...prevSelected, value] : prevSelected.filter((id) => id !== value)));
   };

   const handleSave = () => {
      onSave({
         serviceName,
         startTime,
         endTime,
         selectedDaysOfWeek,
         floorMapIds: selectedFloorPlans,
         servers: selectedServers,
      });
   };

   return (
      <Modal show={show} onHide={onHide}>
         <Modal.Header closeButton>
            <Modal.Title>Add Service Schedule</Modal.Title>
         </Modal.Header>
         <Modal.Body>
            <Form>
               <Form.Group controlId='serviceName'>
                  <Form.Label>Service Name</Form.Label>
                  <Form.Control
                     type='text'
                     placeholder='Enter service name'
                     value={serviceName}
                     onChange={(e) => setServiceName(e.target.value)}
                  />
               </Form.Group>
               <Form.Group controlId='startTime'>
                  <Form.Label>Start Time</Form.Label>
                  <Form.Control type='time' value={startTime} onChange={(e) => setStartTime(e.target.value)} />
               </Form.Group>
               <Form.Group controlId='endTime'>
                  <Form.Label>End Time</Form.Label>
                  <Form.Control type='time' value={endTime} onChange={(e) => setEndTime(e.target.value)} />
               </Form.Group>
               <Form.Group controlId='day'>
                  <Form.Label>Days of the Week</Form.Label>
                  {daysOfWeek.map((day, index) => (
                     <Form.Check type='checkbox' label={day} value={day} key={day} onChange={handleDayCheckboxChange} />
                  ))}
               </Form.Group>
               <Form.Group controlId='floorPlans'>
                  <Form.Label>Floor plans</Form.Label>
                  {floorPlans.map((floorPlan) => (
                     <Form.Check
                        type='checkbox'
                        label={floorPlan.name}
                        value={floorPlan.floorMapId}
                        key={floorPlan.floorMapId}
                        onChange={handleCheckboxChange}
                     />
                  ))}
               </Form.Group>
               <Form.Group controlId='servers'>
                  <Form.Label>Servers</Form.Label>
                  {servers.map((day, index) => (
                     <Form.Check type='checkbox' label={day} value={day} key={day} onChange={handleServerCheckBoxChange} />
                  ))}
               </Form.Group>
            </Form>
         </Modal.Body>
         <Modal.Footer>
            <Button variant='secondary' onClick={onHide}>
               Close
            </Button>
            <Button variant='primary' onClick={handleSave}>
               Save
            </Button>
         </Modal.Footer>
      </Modal>
   );
};

export default AddServiceModal;

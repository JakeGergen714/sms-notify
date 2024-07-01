/** @format */

import React, { useState, useEffect } from "react";
import axios from "axios";
import { Modal, Button, Form, Row, Col, Dropdown, Card, ListGroup, Container } from "react-bootstrap";

const AddRestaurantModal = () => {
   const [restaurant, setRestaurant] = useState({
      restaurantId: null,
      businessId: null,
      name: "",
      address: "",
      serviceTypes: [
         {
            serviceTypeId: null,
            restaurantId: null,
            name: "",
            serviceSchedules: [{ serviceScheduleId: null, dayOfWeek: "", startTime: "", endTime: "" }],
            floorPlan: null,
         },
      ],
   });

   const [floorPlan, setFloorPlan] = useState(null);
   const [floorPlans, setFloorPlans] = useState([]);
   const [showModal, setShowModal] = useState(false);

   const loadFloorPlans = () => {
      axios
         .get(process.env.REACT_APP_API_URL + "/floorMaps")
         .then((res) => {
            setFloorPlans(res.data); // Set state here after fetching
            if (floorPlan != null) {
               //If a floor plan is currently selected, reload it with the latest data
               setFloorPlan(res.data.filter((updatedFloorPlan) => updatedFloorPlan.id == floorPlan.id)[0]);
            }
         })
         .catch((err) => {
            console.log("load floor plans failed");
            console.error(err);
            setFloorPlans([]); // Set to an empty array in case of error
         });
   };

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
         setShowModal(false);
      } catch (error) {
         console.error("Failed to submit restaurant data", error);
      }
   };

   const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

   return (
      <Modal show={showModal} onHide={() => setShowModal(false)}>
         <Modal.Header closeButton>
            <Modal.Title>Add Service</Modal.Title>
         </Modal.Header>
         <Modal.Body>
            <Form onSubmit={handleSubmit}>
               <Form.Group as={Row} controlId='formRestaurantName'>
                  <Form.Label column sm={2}>
                     Name
                  </Form.Label>
                  <Col sm={10}>
                     <Form.Control
                        type='text'
                        name='name'
                        value={restaurant.name}
                        onChange={handleInputChange}
                        placeholder='Restaurant Name'
                     />
                  </Col>
               </Form.Group>

               <Button variant='primary' type='submit'>
                  Submit
               </Button>
            </Form>
         </Modal.Body>
      </Modal>
   );
};

export default AddRestaurantModal;

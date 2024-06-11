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

               <Form.Group as={Row} controlId='formAddress'>
                  <Form.Label column sm={2}>
                     Address
                  </Form.Label>
                  <Col sm={10}>
                     <Form.Control
                        type='text'
                        name='address'
                        value={restaurant.address}
                        onChange={handleInputChange}
                        placeholder='Address'
                     />
                  </Col>
               </Form.Group>

               {restaurant.serviceTypes.map((type, tIndex) => (
                  <div key={tIndex}>
                     <Form.Group as={Row} controlId={`formServiceName${tIndex}`}>
                        <Form.Label column sm={2}>
                           Service Name
                        </Form.Label>
                        <Col sm={10}>
                           <Form.Control
                              type='text'
                              name='name'
                              value={type.name}
                              onChange={(e) => handleServiceTypeChange(tIndex, e)}
                              placeholder='Service Name'
                           />
                        </Col>
                     </Form.Group>

                     <Form.Group as={Row} controlId={`formFloorPlan${tIndex}`}>
                        <Form.Label column sm={2}>
                           Floor Plan
                        </Form.Label>
                        <Col sm={10}>
                           <Dropdown
                              onSelect={(eventKey) =>
                                 selectFloorPlan(
                                    tIndex,
                                    floorPlans.find((fp) => fp.id === parseInt(eventKey))
                                 )
                              }>
                              <Dropdown.Toggle variant='secondary' id='dropdown-basic'>
                                 {type.floorPlan ? type.floorPlan.name : "Select a Floor Plan"}
                              </Dropdown.Toggle>
                              <Dropdown.Menu>
                                 {floorPlans.map((fp) => (
                                    <Dropdown.Item key={fp.id} eventKey={fp.id.toString()}>
                                       {fp.name}
                                    </Dropdown.Item>
                                 ))}
                              </Dropdown.Menu>
                           </Dropdown>
                        </Col>
                     </Form.Group>

                     {type.serviceSchedules.map((schedule, sIndex) => (
                        <div key={sIndex}>
                           <Form.Group as={Row} controlId={`formDayOfWeek${tIndex}${sIndex}`}>
                              <Form.Label column sm={2}>
                                 Day of Week
                              </Form.Label>
                              <Col sm={10}>
                                 <Form.Control
                                    type='text'
                                    name='dayOfWeek'
                                    value={schedule.dayOfWeek}
                                    onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
                                    placeholder='Day of Week'
                                 />
                              </Col>
                           </Form.Group>

                           <Form.Group as={Row} controlId={`formStartTime${tIndex}${sIndex}`}>
                              <Form.Label column sm={2}>
                                 Start Time
                              </Form.Label>
                              <Col sm={10}>
                                 <Form.Control
                                    type='text'
                                    name='startTime'
                                    value={schedule.startTime}
                                    onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
                                    placeholder='Start Time'
                                 />
                              </Col>
                           </Form.Group>

                           <Form.Group as={Row} controlId={`formEndTime${tIndex}${sIndex}`}>
                              <Form.Label column sm={2}>
                                 End Time
                              </Form.Label>
                              <Col sm={10}>
                                 <Form.Control
                                    type='text'
                                    name='endTime'
                                    value={schedule.endTime}
                                    onChange={(e) => handleServiceScheduleChange(tIndex, sIndex, e)}
                                    placeholder='End Time'
                                 />
                              </Col>
                           </Form.Group>
                        </div>
                     ))}
                  </div>
               ))}
               <Button variant='primary' type='submit'>
                  Submit
               </Button>
            </Form>
         </Modal.Body>
      </Modal>
   );
};

export default AddRestaurantModal;

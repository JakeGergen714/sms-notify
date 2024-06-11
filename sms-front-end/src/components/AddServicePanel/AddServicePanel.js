/** @format */

import React from "react";
import { Modal, Button, Form } from "react-bootstrap";

const AddServicePanel = () => {
   return (
      <div className='service-panel-container'>
         <h2>Shift Details</h2>
         <Form>
            <Form.Group controlId='shiftName'>
               <Form.Label>Name</Form.Label>
               <Form.Control type='text' placeholder='Breakfast' />
            </Form.Group>
            <Form.Group controlId='floorPlans'>
               <Form.Label>Floor plans</Form.Label>
               <div>
                  <Form.Check type='checkbox' label='Main' />
                  <Form.Check type='checkbox' label='Buyout' />
                  <Form.Check type='checkbox' label='Wine Dinner' />
                  <Form.Check type='checkbox' label='Main Dining NYE' />
                  <Form.Check type='checkbox' label='Special Floor Plan' />
                  <Form.Check type='checkbox' label='Patio' />
                  <Form.Check type='checkbox' label='Training' />
                  <Form.Check type='checkbox' label='Training-No Combos' />
               </div>
            </Form.Group>
            <Button variant='primary' type='submit'>
               Shift Settings
            </Button>
         </Form>
      </div>
   );
};

export default AddServicePanel;

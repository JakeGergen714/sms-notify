/** @format */

import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const AddServerModal = ({ show, onHide, onSave, serverToEdit, onEdit }) => {
   const [serverName, setServerName] = useState("");

   useEffect(() => {
      if (serverToEdit) {
         setServerName(serverToEdit.name);
      } else {
         setServerName("");
      }
   }, [serverToEdit]);

   const handleInputChange = (e) => {
      setServerName(e.target.value);
   };

   const handleSave = async (e) => {
      e.preventDefault();
      if (serverToEdit == null) {
         try {
            onSave(serverName);
            setServerName("");
            onHide();
         } catch (error) {
            console.error("Failed to add server", error);
         }
      } else {
         try {
            onEdit(serverName);
            setServerName("");
            onHide();
         } catch (error) {
            console.error("Failed to add server", error);
         }
      }
   };

   const handleDelete = async (e) => {
      e.preventDefault();
      try {
         onSave(null, serverToEdit.serverId); // Pass null to indicate deletion
         setServerName("");
         onHide();
      } catch (error) {
         console.error("Failed to delete server", error);
      }
   };

   return (
      <Modal show={show} onHide={onHide}>
         <Modal.Header closeButton>
            <Modal.Title>{serverToEdit ? "Edit Server" : "Add Server"}</Modal.Title>
         </Modal.Header>
         <Modal.Body>
            <Form onSubmit={handleSave}>
               <Form.Group controlId='formServerName'>
                  <Form.Label>Server Name</Form.Label>
                  <Form.Control
                     type='text'
                     value={serverName}
                     onChange={handleInputChange}
                     placeholder='Enter server name'
                  />
               </Form.Group>
               <Button variant='primary' type='submit'>
                  {serverToEdit ? "Save Changes" : "Add"}
               </Button>
               {serverToEdit && (
                  <Button variant='danger' onClick={handleDelete} style={{ marginLeft: "10px" }}>
                     Delete
                  </Button>
               )}
            </Form>
         </Modal.Body>
      </Modal>
   );
};

export default AddServerModal;

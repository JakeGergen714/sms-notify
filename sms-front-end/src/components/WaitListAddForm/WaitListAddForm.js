/** @format */

import React, { useState } from "react";
import axios from "axios";
import { BsThreeDots } from "react-icons/bs";
import { BsFillPersonPlusFill, BsPersonPlus } from "react-icons/bs";
import Modal from "react-modal";
import "./WaitListAddForm.css";

const WaitListAddForm = () => {
   const [formData, setFormData] = useState({
      phoneNumber: "",
      customerName: "",
      partySize: "",
      notes: [],
   });
   const [showExtraInput, setShowExtraInput] = useState(false);
   const [addButtonSelected, setAddButtonSelected] = useState(false);
   const [modalOpen, setModalOpen] = useState(false);

   const handleInputChange = (e) => {
      const { name, value } = e.target;
      setFormData((prevState) => ({
         ...prevState,
         [name]: value,
      }));
   };

   const setPartySize = (size) => {
      setFormData((prevState) => ({
         ...prevState,
         partySize: size,
      }));
   };

   const handleSubmit = async (e) => {
      console.log("handle submit");
      e.preventDefault();
      console.log(formData);
      setModalOpen(false);
      try {
         const response = await axios.post(process.env.API_URL + "/waitList", formData);
         console.log(response.data);
         window.location.reload();
      } catch (error) {
         console.error("Error:", error.response.data);
      }
   };

   const notesList = ["inside", "outside", "bar", "first available"];

   const handleNoteToggle = (note) => {
      const currentNotes = formData.notes;
      const isNoteActive = currentNotes.includes(note);
      if (isNoteActive) {
         setFormData((prevState) => ({
            ...prevState,
            notes: currentNotes.filter((n) => n !== note),
         }));
      } else {
         setFormData((prevState) => ({
            ...prevState,
            notes: [...currentNotes, note],
         }));
      }
   };

   return (
      <div className='waitlist-add-form'>
         <div
            className='add-button'
            onClick={() => {
               setModalOpen(true);
               setAddButtonSelected(true);
            }}
            onMouseEnter={() => {
               setAddButtonSelected(true);
            }}
            onMouseLeave={() => {
               if (!modalOpen) {
                  setAddButtonSelected(false);
               }
            }}>
            {addButtonSelected ? <BsFillPersonPlusFill /> : <BsPersonPlus />}
         </div>
         <div>
            <Modal
               className='Modal'
               isOpen={modalOpen}
               overlayClassName='customOverlay'
               onRequestClose={() => {
                  setModalOpen(false);
                  setAddButtonSelected(false);
               }}>
               <form onSubmit={handleSubmit}>
                  <div className='row phone-name justify-content-center'>
                     <div className='col-md-6'>
                        <div className='label'>Phone Number:</div>
                        <input
                           type='tel'
                           id='phoneNumber'
                           name='phoneNumber'
                           className='form-control custom-bg-color'
                           value={formData.phoneNumber}
                           onChange={handleInputChange}
                           required
                        />
                     </div>
                     <div className='col-md-6'>
                        <div className='label'>Name:</div>
                        <input
                           type='text'
                           id='customerName'
                           name='customerName'
                           className='form-control custom-bg-color'
                           value={formData.customerName}
                           onChange={handleInputChange}
                           required
                        />
                     </div>
                  </div>

                  <div className='mt-3 justify-content-center'>
                     <label>Party Size:</label>
                     <div>
                        {[...Array(9)].map((_, i) => (
                           <button
                              type='button'
                              key={i}
                              className={`btn btn-secondary m-1 party-button ${
                                 formData.partySize === i + 1 && !showExtraInput ? "active" : ""
                              } ${showExtraInput ? "btn-clear" : ""}`}
                              onClick={() => {
                                 setShowExtraInput(false);
                                 setPartySize(i + 1);
                              }}>
                              {i + 1}
                           </button>
                        ))}
                        <button
                           type='button'
                           className={`btn btn-secondary m-1 ${showExtraInput ? "btn-deactivated" : ""}`}
                           onClick={() => setShowExtraInput(!showExtraInput)}>
                           <BsThreeDots />
                        </button>
                        {showExtraInput && (
                           <input
                              type='number'
                              placeholder='9+'
                              className={`form-control d-inline-block custom-bg-color ${
                                 formData.partySize ? "extra-input-active" : ""
                              }`}
                              value={formData.partySize}
                              onChange={(e) => setPartySize(e.target.value)}
                           />
                        )}
                     </div>
                  </div>

                  <div className='notes-center-container mt-3'>
                     <label>Notes:</label>
                     <div className='notes-btn-group'>
                        {notesList.map((note, index) => (
                           <button
                              type='button'
                              key={index}
                              className={`btn btn-secondary ${formData.notes.includes(note) ? "active" : ""}`}
                              onClick={() => handleNoteToggle(note)}>
                              {note}
                           </button>
                        ))}
                     </div>
                  </div>

                  <div className='mt-3 text-center'>
                     <button className='btn btn-primary' type='submit'>
                        Add to Waitlist
                     </button>
                  </div>
               </form>
            </Modal>
         </div>
      </div>
   );
};

export default WaitListAddForm;

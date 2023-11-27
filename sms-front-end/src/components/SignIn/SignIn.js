/** @format */

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";

import "./SignIn.css";
import { login, signUp, isAuthenticated } from "../../service/AuthService/AuthService";
import { AuthContext } from "../../AuthContext"; // Update the path as necessary

const SignIn = () => {
   const [formData, setFormData] = useState({
      email: "",
      password: "",
   });

   const { recheckAuthentication } = useContext(AuthContext);
   const navigate = useNavigate();

   async function handleLogin() {
      await login(formData.email, formData.password);

      recheckAuthentication().then((isAuthed) => {
         if (isAuthed) {
            navigate("/home");
         } else {
            console.log("not logged in");
         }
      });
   }

   function handleSignUp() {
      console.log("Signing up");
      signUp(formData.email, formData.password);
      console.log("Signing done");
   }

   function handleInputChange(event) {
      const { name, value } = event.target;
      setFormData({
         ...formData,
         [name]: value,
      });
   }

   return (
      <div className='container'>
         <div className='row justify-content-center'>
            <div className='col-12 text-center'>
               <img className='logo img-fluid' src='/notifyd_logo.png' alt='Logo' />
            </div>
         </div>
         <div className='row justify-content-center'>
            <div className='col'>
               <div className='form'>
                  <div className='form-group'>
                     <input
                        type='text'
                        name='email'
                        className='form-control'
                        placeholder='Email Address'
                        value={formData.email}
                        onChange={handleInputChange}
                        required
                     />
                  </div>
                  <div className='form-group'>
                     <input
                        type='password'
                        name='password'
                        className='form-control'
                        placeholder='Password'
                        value={formData.password}
                        onChange={handleInputChange}
                        required
                     />
                  </div>
                  <div className='form-group' id='Forgot-Pass'>
                     <a className='forgot-password' href='#'>
                        Forgot Password?
                     </a>
                  </div>
                  <div className='d-grid' id='Sign-In'>
                     <button type='button' onClick={handleLogin} className='btn btn-primary btn-block'>
                        Sign In
                     </button>
                  </div>
                  <div className='d-grid'>
                     <button type='button' onClick={handleSignUp} className='btn btn-primary btn-block'>
                        Create Account
                     </button>
                  </div>
               </div>
            </div>
         </div>
      </div>
   );
};

export default SignIn;

/** @format */

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { login, signUp } from "../../service/AuthService/AuthService";
import { AuthContext } from "../../AuthContext";
import "./SignIn.css";

const SignIn = () => {
   const [formData, setFormData] = useState({ email: "", password: "" });
   const { recheckAuthentication } = useContext(AuthContext);
   const navigate = useNavigate();

   const handleLogin = async (event) => {
      event.preventDefault();
      await login(formData.email, formData.password);
      recheckAuthentication().then((isAuthed) => {
         if (isAuthed) navigate("/home");
      });
   };

   const handleInputChange = (event) => {
      const { name, value } = event.target;
      setFormData({ ...formData, [name]: value });
   };

   return (
      <div className='signin-background d-flex align-items-center justify-content-center'>
         <div className='signin-card p-3'>
            <h2 className='text-center mb-4'>Log In to The Waitlist</h2>
            <form onSubmit={handleLogin}>
               <div className='mb-3'>
                  <input
                     type='email'
                     className='form-control'
                     name='email'
                     placeholder='Email address or phone number'
                     value={formData.email}
                     onChange={handleInputChange}
                     required
                  />
               </div>
               <div className='mb-3'>
                  <input
                     type='password'
                     className='form-control'
                     name='password'
                     placeholder='Password'
                     value={formData.password}
                     onChange={handleInputChange}
                     required
                  />
               </div>
               <div className='d-grid mb-2'>
                  <button type='submit' className='btn btn-primary btn-lg'>
                     Log In
                  </button>
               </div>
               <div className='text-center mb-4'>
                  <a href='#!' className='forgot-password'>
                     Forgot account?
                  </a>
               </div>
               <hr />
               <div className='d-grid'>
                  <button
                     type='button'
                     className='btn btn-success btn-lg'
                     onClick={() => signUp(formData.email, formData.password)}>
                     Create New Account
                  </button>
               </div>
            </form>
         </div>
      </div>
   );
};

export default SignIn;

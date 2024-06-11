/** @format */

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./AuthContext";
import { UserProvider } from "./context/UserContext";
import RestaurantSelection from "./components/RestaurantSelection/RestaurantSelection";
import Home from "./components/Home/Home";
import ProtectedRoute from "./components/ProtectedRoute";
import "bootstrap/dist/css/bootstrap.min.css";

export default function App() {
   return (
      <UserProvider>
         <BrowserRouter basename='/ui'>
            <Routes>
               <Route path='/*' element={<Navigate to='/restaurant-selection' />} />{" "}
               {/* Redirect root to restaurant-selection */}
               <Route path='/restaurant-selection' element={<RestaurantSelection />} />
               <Route
                  path='/home/*'
                  element={
                     <ProtectedRoute>
                        <Home />
                     </ProtectedRoute>
                  }
               />
            </Routes>
         </BrowserRouter>
      </UserProvider>
   );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);

reportWebVitals();

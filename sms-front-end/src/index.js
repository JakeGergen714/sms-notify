/** @format */

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import SignIn from "./components/SignIn/SignIn";
import reportWebVitals from "./reportWebVitals";
import Home from "./components/Home/Home";
import Reservations from "./components/Reservations/Reservations";
import WaitList from "./components/WaitList/WaitList";
import Settings from "./components/Settings/Settings";
import SeatMap from "./components/SeatMap/SeatMap";
import "bootstrap/dist/css/bootstrap.min.css";
import RequireAuth from "./components/RequireAuth/RequireAuth";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./AuthContext";

export default function App() {
   return (
      <AuthProvider>
         <BrowserRouter>
            <Routes>
               <Route path='/login' element={<SignIn />}></Route>
               <Route
                  path='/'
                  element={
                     <RequireAuth>
                        <Home />
                     </RequireAuth>
                  }
               />
               <Route path='/reservations' element={<Reservations />}></Route>
               <Route
                  path='/waitlist'
                  element={
                     <RequireAuth>
                        <WaitList />
                     </RequireAuth>
                  }></Route>
               <Route path='/settings' element={<Settings />}></Route>
               <Route path='/seatmap' element={<SeatMap />}></Route>
            </Routes>
         </BrowserRouter>
      </AuthProvider>
   );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

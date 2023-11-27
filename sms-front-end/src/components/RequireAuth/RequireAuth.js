/** @format */

// RequireAuth.js
import React, { useContext } from "react";
import { AuthContext } from "../../AuthContext"; // Update the path as necessary
import SignIn from "../SignIn/SignIn";

const RequireAuth = ({ children }) => {
   const { isAuthed } = useContext(AuthContext);

   if (isAuthed === null) {
      return <div>Loading...</div>;
   }

   if (!isAuthed) {
      return <SignIn />;
   }

   return children;
};

export default RequireAuth;

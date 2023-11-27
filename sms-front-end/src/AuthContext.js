/** @format */

// AuthContext.js
import React, { createContext, useState, useEffect } from "react";
import { isAuthenticated } from "./service/AuthService/AuthService";

export const AuthContext = createContext({
   isAuthed: false,
   recheckAuthentication: () => {},
});

export const AuthProvider = ({ children }) => {
   const [isAuthed, setIsAuthed] = useState(false);

   const recheckAuthentication = async () => {
      const authStatus = await isAuthenticated();
      setIsAuthed(authStatus);
      return authStatus;
   };

   useEffect(() => {
      recheckAuthentication();
   }, []);

   return <AuthContext.Provider value={{ isAuthed, recheckAuthentication }}>{children}</AuthContext.Provider>;
};

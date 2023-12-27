/** @format */

import Cookies from "js-cookie";
import axios from "axios";
import { UserCredentialDTO } from "../../dto/UserCredentialDTO";

axios.defaults.withCredentials = true;

const validateAuthToken = async () => {
   try {
      const response = await axios.post(process.env.API_URL + "/validate");
      console.log("validate response ", response);
      return true;
   } catch (err) {
      console.log("failed to validate", err);
      return false;
   }
};

const refreshAuthToken = async (authTokenDTO) => {
   try {
      const response = await axios.post(process.env.API_URL + "/refresh");
      return true;
   } catch (err) {
      console.log("failed to validate", err);
      return false;
   }
};

export async function isAuthenticated() {
   if (!(await validateAuthToken())) {
      console.log("Not authed. Attempting to refresh");
      await refreshAuthToken();
      console.log("refresh finished");

      if (!(await validateAuthToken())) {
         console.log("refresh failed");
         return false;
      } else {
         console.log("Refresh successful");
      }
   }

   console.log("Is authed");
   return true;
}

export async function login(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   try {
      console.log("logging in");
      const response = await axios.post(process.env.API_URL + "/signin", userCredentialDTO);
      console.log("Logged in");
   } catch (err) {
      console.log(err);
   }
}

export function signUp(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   axios
      .post(process.env.API_URL + "/signup", userCredentialDTO)
      .then((response) => {
         Cookies.set("accessToken", response.data.accessToken);
         Cookies.set("refreshToken", response.data.refreshToken);
         console.log("signup respnse");
         console.log(response);
      })
      .catch((err) => {
         console.log("sign up failed");
         console.log(err);
      });
}

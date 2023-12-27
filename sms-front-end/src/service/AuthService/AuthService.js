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
   if (validateAuthToken()) {
      return true;
   }
   await refreshAuthToken();

   if (validateAuthToken()) {
      return true;
   }

   return false;
}

export async function login(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   try {
      const response = await axios.post(process.env.API_URL + "/signin", userCredentialDTO);
   } catch (err) {
      console.log("Login failed.", err);
   }
}

export function signUp(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   axios
      .post(process.env.API_URL + "/signup", userCredentialDTO)
      .then((response) => {
         Cookies.set("accessToken", response.data.accessToken);
         Cookies.set("refreshToken", response.data.refreshToken);
      })
      .catch((err) => {
         console.log("Sign up failed.", err);
      });
}

/** @format */

import Cookies from "js-cookie";
import axios from "axios";
import { UserCredentialDTO } from "../../dto/UserCredentialDTO";

axios.defaults.withCredentials = true;

const validateAuthToken = async () => {
   console.log("Validating authtoken");
   try {
      const response = await axios.post(process.env.REACT_APP_API_URL + "/validate");
      console.log("Auth token validated!");
      return true;
   } catch (err) {
      console.log("Failed to validate authtoken", err);
      return false;
   }
};

const refreshAuthToken = async (authTokenDTO) => {
   console.log("Refreshing authtoken");
   try {
      const response = await axios.post(process.env.REACT_APP_API_URL + "/refresh");
      console.log("Refreshed token validated!");
      return true;
   } catch (err) {
      console.log("Failed to refresh auth token.", err);
      return false;
   }
};

export async function isAuthenticated() {
   if (await validateAuthToken()) {
      console.log("isAuthenticated validated auth token");
      return true;
   }
   console.log("Refreshing token");
   await refreshAuthToken();

   if (await validateAuthToken()) {
      return true;
   }

   console.log("Not Authenticated.");
   return false;
}

export async function login(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   try {
      console.log(process.env.REACT_APP_API_URL + "/signin");
      await axios.post(process.env.REACT_APP_API_URL + "/signin", userCredentialDTO);
   } catch (err) {
      console.log("Login failed.", err);
   }
}

export function signUp(username, password) {
   let userCredentialDTO = new UserCredentialDTO(username, password);
   axios
      .post(process.env.REACT_APP_API_URL + "/signup", userCredentialDTO)
      .then((response) => {
         Cookies.set("accessToken", response.data.accessToken);
         Cookies.set("refreshToken", response.data.refreshToken);
      })
      .catch((err) => {
         console.log("Sign up failed.", err);
      });
}

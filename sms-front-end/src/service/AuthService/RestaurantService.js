/** @format */

import axios from "axios";

export function getRestaurant(restaurantId) {
   return axios.get(`${process.env.REACT_APP_API_URL}/restaurant`, {
      params: {
         restaurantId: restaurantId,
      },
   });
}

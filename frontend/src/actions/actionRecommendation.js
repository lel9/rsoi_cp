import {
  POST_RECOMMENDATIONS,
  recommendationsPublic
} from '../constants';

import { handleError } from './actionSession';
import axios from 'axios';
// const url = process.env.URL;
// console.log(process.env);

export const addRecommendation = (data) => {
 const { diagnosis, state } = data;
 return dispatch => {
   dispatch(addRecommendationStarted());
   axios.post(recommendationsPublic, {
     diagnosis: diagnosis,
     state: state,
   })
   .then(res => {
     dispatch(addRecommendationSuccess(res.data));
     // dispatch(getComments({id: id, page: 0, size: 15}));
     // alert('Рекомендация успешно добавлена');
   })
   .catch(err => {
     let error = err.message === 'Network Error' ? err.message : err.response.data
     dispatch(handleError(POST_RECOMMENDATIONS, error, addRecommendation, data))
     dispatch(addRecommendationFailure(err.response));
   })
 }
}

const addRecommendationStarted = () => ({
  type: POST_RECOMMENDATIONS + '_STARTED',
})
const addRecommendationSuccess = (data) => ({
  type: POST_RECOMMENDATIONS + '_SUCCESS',
  payload: {
    ...data
  },
})
const addRecommendationFailure = (error) => ({
  type: POST_RECOMMENDATIONS + '_FAILURE',
  payload: {
    error
  },
})

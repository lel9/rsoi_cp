import {
  GET_PROFILE_BY_ID,
  PUT_PROFILE,
  profileProtected,
 } from  '../constants';

 // export <key>=<value> // env

import axios from 'axios';
import { sessionService } from 'redux-react-session';
import { handleError } from './actionSession';
// const url = process.env.URL;
// console.log(process.env);

// sessionService.loadSession().then(session => console.log(session));


export const updateProfile = (data) => {
  console.log(data);
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(updateProfileStarted());
      axios.put(profileProtected + data.id, {
        ...data
      }, {headers: headers})
      .then(res => {
        dispatch(updateProfileSuccess(res.data));
        alert('Профиль успешно изменился')
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(PUT_PROFILE, error, updateProfile, data))
        dispatch(updateProfileFailure(err.response));
      })
    })
  }
}

export const getProfileProtected = (id) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(getProfileProtectedStarted());
      axios.get(profileProtected + id, {
        headers: headers
      })
      .then(res => {
        dispatch(getProfileProtectedSuccess(res.data));
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(GET_PROFILE_BY_ID, error, getProfileProtected, id))
        dispatch(getProfileProtectedFailure(err.response));
      })
    })
  }
}


////////////////////////////////////////////////////////////////////////////////
const updateProfileStarted = () => ({
  type: PUT_PROFILE + '_STARTED'
})

const updateProfileSuccess = data => ({
  type: PUT_PROFILE + '_SUCCESS',
  payload: {
    data
  }
})

const updateProfileFailure = error => ({
  type: PUT_PROFILE + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getProfileProtectedStarted = () => ({
  type: GET_PROFILE_BY_ID + '_STARTED'
})

const getProfileProtectedSuccess = data => ({
  type: GET_PROFILE_BY_ID + '_SUCCESS',
  payload: {
    ...data
  }
})

const getProfileProtectedFailure = error => ({
  type: GET_PROFILE_BY_ID + '_FAILURE',
  payload: {
    error
  }
})

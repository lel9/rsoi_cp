import {
  ADD_PATIENT,
  GET_PATIENTS,
  GET_PATIENT_BY_ID,
  GET_PATIENT_BY_IDS,
  // UPDATE_PATIENT_FAILURE,
  // DELETE_PATIENT_FAILURE,
  patientsProtected,
} from '../constants';

import { handleError } from './actionSession';
import axios from 'axios';
import { sessionService } from 'redux-react-session';

export const addPatient = (data) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(addPatientStarted());
      axios.post(patientsProtected, {
        ...data
      }, {headers: headers})
      .then(res => {
        dispatch(addPatientSuccess(res.data));
        alert('Пациент успешно добавлен');
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(ADD_PATIENT, error, addPatient, data))
        dispatch(addPatientFailure(err.response));
      })
    })
  }
}

export const getPatients = (data) => {
  const {cardId, page, size} = data;
   return dispatch => {
   sessionService.loadSession().then(session => {
     const headers = {
       Authorization: `Bearer ${session.access_token}`
     }
     dispatch(getPatientsStarted());
       axios.get(`${patientsProtected}?cardId=${cardId}&page=${page}&size=${size}`,
         {headers: headers})
     .then(res => {
       dispatch(getPatientsSuccess(res.data));
     })
     .catch(err => {
       const error = err.message === 'Network Error' ? err.message : err.response.data
       dispatch(handleError(GET_PATIENTS, error, getPatients, data))
       dispatch(getPatientsFailure(err.response));
     })
   })
  }
}

export const getPatientById = (id) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(getPatientStarted(), {headers: headers});
      axios.get(`${patientsProtected}/${id}`, {
        headers: headers
      })
      .then(res => {
        dispatch(getPatientSuccess(res.data));
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(GET_PATIENT_BY_ID, error, getPatientById, id))
        dispatch(getPatientFailure(err.response));
      })
    })
  }
}

export const getPatientsByIds = (ids) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(getPatientsIdsStarted());
      axios.get(`${patientsProtected}/byIds?ids=${ids}`, {
        headers: headers
      })
      .then(res => {
        //res.data
        console.log(res.data);
        dispatch(getPatientsIdsSuccess(res.data));
      })
      .catch(err => {
        const error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(GET_PATIENT_BY_IDS, error, getPatientsByIds, ids))
        dispatch(getPatientsIdsFailure(err.response));
      })
    })
  }
}

////////////////////////////////////////////////////////////////////////////////
const addPatientStarted = () => ({
  type: ADD_PATIENT + '_STARTED',
})

const addPatientSuccess = data => ({
  type: ADD_PATIENT + '_SUCCESS',
  payload: {
    data
  }
})

const addPatientFailure = error => ({
  type: ADD_PATIENT + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getPatientsStarted = () => ({
  type: GET_PATIENTS + '_STARTED',
})

const getPatientsSuccess = data => ({
  type: GET_PATIENTS + '_SUCCESS',
  payload: {
    ...data
  }
})

const getPatientsFailure = error => ({
  type: GET_PATIENTS + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getPatientStarted = () => ({
  type: GET_PATIENT_BY_ID + '_STARTED',
})

const getPatientSuccess = data => ({
  type: GET_PATIENT_BY_ID + '_SUCCESS',
  payload: {
    ...data
  }
})

const getPatientFailure = error => ({
  type: GET_PATIENT_BY_ID + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getPatientsIdsStarted = () => ({
  type: GET_PATIENT_BY_IDS + '_STARTED',
})

const getPatientsIdsSuccess = data => ({
  type: GET_PATIENT_BY_IDS + '_SUCCESS',
  payload: {
    ...data
  },
  entity: 'patient'
})

const getPatientsIdsFailure = error => ({
  type: GET_PATIENT_BY_IDS + '_FAILURE',
  payload: {
    error
  }
})

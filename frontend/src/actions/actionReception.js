import {
  ADD_RECEPTION,
  GET_RECEPTION,
  UPDATE_RECEPTION,
  DELETE_RECEPTION,
  patientsPublic,
  patientsProtected
 } from  '../constants';

 // export <key>=<value> // env

import axios from 'axios';
import { sessionService } from 'redux-react-session';
import { handleError } from './actionSession';
import { getPatientById } from './actionPatient';
// const url = process.env.URL;
// console.log(process.env);

export const addReception = (data) => {
  const {id, date, diagnosis, drugs, state} = data;
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(addReceptionStarted());
      axios.post(`${patientsProtected}/${id}/reception`, {
        date: date,
        diagnosis: diagnosis,
        state: state,
        drugs: drugs,
      }, {
        headers: headers
      })
      .then(res => {
        dispatch(addReceptionSuccess({id: res.data, date: date, state: state,
           diagnosis: diagnosis, drugs: drugs}));
        alert('Осмотр успешно добавлен');
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(ADD_RECEPTION, error, addReception, data))
        dispatch(addReceptionFailure(err.response));
      })
    })
  }
}

export const getReception = (id) => {
  return dispatch => {
    dispatch(getReceptionStarted());
    axios.get(`${patientsPublic}/${id}/reception`)
    .then(res => {
      dispatch(getReceptionSuccess(res.data));
    })
    .catch(err => {
      let error = err.message === 'Network Error' ? err.message : err.response.data
      dispatch(handleError(GET_RECEPTION, error, getReception, id))
      dispatch(getReceptionFailure(err.response));
    })
  }
}

export const updateReception = (data) => {
  const { pid, rid, date, diagnosis, drugs, state } = data;
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(updateReceptionStarted());
      axios.put(`${patientsProtected}/${pid}/reception/${rid}`, {
        date: date,
        diagnosis: diagnosis,
        state: state,
        drugs: drugs,
      }, {
        headers: headers
      })
      .then(res => {
        dispatch(updateReceptionSuccess({date: date, diagnosis: diagnosis, drugs: drugs, state: state}));
        dispatch(getPatientById(pid));
        alert('Осмотр успешно обновлен');
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(UPDATE_RECEPTION, error, updateReception, data))
        dispatch(updateReceptionFailure(err.response));
      })
    })
  }
}

export const deleteReception = (data) => {
  const { pid, rid } = data
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(deleteReceptionStarted());
      axios.delete(`${patientsProtected}/${pid}/reception/${rid}`, {
        headers: headers
      })
      .then(res => {
        dispatch(deleteReceptionSuccess(res.data));
        alert('Осмотр успешно удален');
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(UPDATE_RECEPTION, error, deleteReception, data))
        dispatch(deleteReceptionFailure(err.response));
      })
    })
  }
}

////////////////////////////////////////////////////////////////////////////////
const addReceptionStarted = () => ({
  type: ADD_RECEPTION + '_STARTED'
})

const addReceptionSuccess = data => ({
  type: ADD_RECEPTION + '_SUCCESS',
  payload: {
    ...data
  }
})

const addReceptionFailure = error => ({
  type: ADD_RECEPTION + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getReceptionStarted = () => ({
  type: GET_RECEPTION + '_STARTED'
})

const getReceptionSuccess = data => ({
  type: GET_RECEPTION + '_SUCCESS',
  payload: {
    ...data
  }
})

const getReceptionFailure = error => ({
  type: GET_RECEPTION + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////

const updateReceptionStarted = () => ({
  type: UPDATE_RECEPTION + '_STARTED'
})

const updateReceptionSuccess = (data) => ({
  type: UPDATE_RECEPTION + '_SUCCESS',
  payload: {
    ...data
  }
})

const updateReceptionFailure = error => ({
  type: UPDATE_RECEPTION + '_FAILURE',
  payload: {
    error
  }
})


const deleteReceptionStarted = () => ({
  type: DELETE_RECEPTION + '_STARTED'
})

const deleteReceptionSuccess = data => ({
  type: DELETE_RECEPTION + '_SUCCESS',
  payload: {
    ...data
  }
})

const deleteReceptionFailure = error => ({
  type: DELETE_RECEPTION + '_FAILURE',
  payload: {
    error
  }
})

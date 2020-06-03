import {
  ADD_DRUG,
  GET_DRUGS,
  GET_DRUG_BY_ID,
  GET_DRUG_BY_IDS,
  // UPDATE_DRUG
  drugsPublic,
  drugsProtected
 } from  '../constants';

import { handleError } from './actionSession';

 // export <key>=<value> // env

import axios from 'axios';
import { sessionService } from 'redux-react-session';
//
// const url = process.env.URL;
// console.log(process.env);

// sessionService.loadSession().then(session => console.log(session));


export const addDrug = (data) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(addDrugStarted());
      axios.post(drugsProtected, {
        ...data
      }, {headers: headers})
      .then(res => {
        dispatch(addDrugSuccess(res.data));
        alert('Препарат успешно добавлен');
      })
      .catch(err => {
        const error = err.message === 'Network Error' ? err.message : err.response.data
        console.log(err.message);
        dispatch(handleError(ADD_DRUG, error, addDrug, data))
        dispatch(addDrugFailure(err.response));
      })
    })
  }
}

export const getDrugs = (data) => {
  const { tradeName, page, size } = data;
  return dispatch => {
    dispatch(getDrugsStarted());
    axios.get(`${drugsPublic}?text=${tradeName}&page=${page}&size=${size}`)
    .then(res => {
      dispatch(getDrugsSuccess(res.data));
    })
    .catch(err => {
      const error = err.message === 'Network Error' ? err.message : err.response.data
      dispatch(handleError(GET_DRUGS, error, getDrugs, data));
      dispatch(getDrugsFailure(err.response));
    })
  }
}

export const getDrugById = (id) => {
  return dispatch => {
    dispatch(getDrugStarted());
    axios.get(`${drugsPublic}/${id}`)
    .then(res => {
      dispatch(getDrugSuccess(res.data));
    })
    .catch(err => {
      const error = err.message === 'Network Error' ? err.message : err.response.data
      dispatch(handleError(GET_DRUG_BY_ID, error, getDrugById, id));
      dispatch(getDrugFailure(err.response));
    })
  }
}

export const getDrugsByIds = (ids) => {
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(getDrugsIdsStarted());
      axios.get(`${drugsProtected}/byIds?ids=${ids}`, {
        headers: headers
      })
      .then(res => {
        dispatch(getDrugsIdsSuccess(res.data));
      })
      .catch(err => {
        const error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(GET_DRUG_BY_IDS, error, getDrugsByIds, ids))
        dispatch(getDrugsIdsFailure(err.response));
      })
    })
  }
}

// export const updateDrug = (id) => {
//   return dispatch => {
//     dispatch(addDrugStarted());
//     axios.patch(addDrugPost, {
//       ...data
//     })
//     .then(res => {
//       dispatch(addDrugSuccess(res.data));
//     })
//     .catch(err => {
//       console.log(err.data);
//       dispatch(addDrugFailure(err.response));
//     })
//   }
// }
////////////////////////////////////////////////////////////////////////////////
const addDrugStarted = () => ({
  type: ADD_DRUG + '_STARTED'
})

const addDrugSuccess = data => ({
  type: ADD_DRUG + '_SUCCESS',
  payload: {
    data
  }
})

const addDrugFailure = error => ({
  type: ADD_DRUG + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getDrugsStarted = () => ({
  type: GET_DRUGS + '_STARTED'
})

const getDrugsSuccess = data => ({
  type: GET_DRUGS + '_SUCCESS',
  payload: {
    ...data
  }
})

const getDrugsFailure = error => ({
  type: GET_DRUGS + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getDrugStarted = () => ({
  type: GET_DRUG_BY_ID + '_STARTED'
})

const getDrugSuccess = data => ({
  type: GET_DRUG_BY_ID + '_SUCCESS',
  payload: {
    ...data
  }
})

const getDrugFailure = error => ({
  type: GET_DRUG_BY_ID + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getDrugsIdsStarted = () => ({
  type: GET_DRUG_BY_IDS + '_STARTED'
})

const getDrugsIdsSuccess = data => ({
  type: GET_DRUG_BY_IDS + '_SUCCESS',
  payload: {
    ...data
  },
  entity: 'drug'
})

const getDrugsIdsFailure = error => ({
  type: GET_DRUG_BY_IDS + '_FAILURE',
  payload: {
    error
  }
})

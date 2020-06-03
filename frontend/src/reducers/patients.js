import {
  ADD_PATIENT,
  GET_PATIENTS,
  GET_PATIENT_BY_ID,
  // UPDATE_PATIENT,
  // DELETE_PATIENT,
  ADD_RECEPTION,
  GET_RECEPTION,
  UPDATE_RECEPTION,
  DELETE_RECEPTION,
  RECEPTIONS_CLEAN,
  PATIENTS_CLEAN,
 } from  '../constants';

const initialState = {
  loading: false,
  patients: null,
  patient: null,
  reception: null,
  receptions: [],
  error: null
};

const patients = function reducer(state = initialState, action) {
  // console.log(action.type);
  // console.log(action.payload);
  switch (action.type) {
    case ADD_PATIENT + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case ADD_PATIENT + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        patients: action.payload
      }
    case ADD_PATIENT + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_PATIENTS + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_PATIENTS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        patients: action.payload
      }
    case GET_PATIENTS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_PATIENT_BY_ID + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_PATIENT_BY_ID + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        patient: action.payload,
        receptions: action.payload.receptions
      }
    case GET_PATIENT_BY_ID + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }

    case ADD_RECEPTION + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case ADD_RECEPTION + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        reception: action.payload,
        receptions: [...state.receptions, action.payload]
      }
    case ADD_RECEPTION + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_RECEPTION + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_RECEPTION + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        receptions: action.payload.receptions
      }
    case GET_RECEPTION + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case UPDATE_RECEPTION + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case UPDATE_RECEPTION + '_SUCCESS':
    console.log(state);
      return {
        ...state,
        loading: false,
        error: null,
        reception: action.payload
      }
    case UPDATE_RECEPTION + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case DELETE_RECEPTION + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case DELETE_RECEPTION + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        reception: action.payload
      }
    case DELETE_RECEPTION + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case RECEPTIONS_CLEAN + '_SUCCESS':
      return {
        loading: false,
        reception: null,
        receptions: [],
        error: null
      }
    case PATIENTS_CLEAN + '_SUCCESS':
      return {
        loading: false,
        patients: null,
        patient: null,
        error: null
      }
    default:
      return state;
  }
}
export default patients;

import {
  GET_STATISTICS,
  GET_DRUG_BY_IDS,
  GET_PATIENT_BY_IDS,
  GET_USER_BY_IDS,
  STATISTICS_CLEAN
 } from  '../constants';

const initialState = {
  loading: false,
  statistics: [],
  ids: [],
  error: null,
  entity: '',
  dateStart: 0,
  dateEnd: 0
};

const statistics = function reducer(state = initialState, action) {
  switch (action.type) {
    case GET_STATISTICS + '_STARTED':
      return {
        ...state,
        loading: true,
        dateStart: action.payload.dateStart,
        dateEnd: action.payload.dateEnd,
      }
    case GET_STATISTICS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        ids: action.payload
      }
    case GET_STATISTICS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_DRUG_BY_IDS + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_DRUG_BY_IDS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        statistics: action.payload,
        entity: action.entity,
      }
    case GET_DRUG_BY_IDS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_PATIENT_BY_IDS + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_PATIENT_BY_IDS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        statistics: action.payload,
        entity: action.entity,
      }
    case GET_PATIENT_BY_IDS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case GET_USER_BY_IDS + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_USER_BY_IDS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        statistics: action.payload,
        entity: action.entity,
      }
    case GET_USER_BY_IDS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case STATISTICS_CLEAN + '_SUCCESS':
      return {
        loading: false,
        statistics: [],
        ids: [],
        error: null,
        entity: '',
        dateStart: 0,
        dateEnd: 0
      }
    default:
      return state;
  }
}
export default statistics;

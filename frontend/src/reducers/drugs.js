import {
  GET_DRUGS,
  GET_DRUG_BY_ID,
  GET_DRUG_BY_IDS,
  ADD_DRUG,
  DRUGS_CLEAN
 } from  '../constants';

 
const initialState = {
  loading: false,
  drugs: null,
  drug: null,
  error: null,
  statics: [],
};

const drugs = function reducer(state = initialState, action) {
  switch (action.type) {
    case ADD_DRUG + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case ADD_DRUG + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        drugs: action.payload
      }
    case ADD_DRUG + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
      case GET_DRUGS + '_STARTED':
        return {
          ...state,
          loading: true
        }
      case GET_DRUGS + '_SUCCESS':
        return {
          ...state,
          loading: false,
          error: null,
          drugs: action.payload
        }
      case GET_DRUGS + '_FAILURE':
        return {
          ...state,
          loading: false,
          error: action.payload.error
        }
      case GET_DRUG_BY_ID + '_STARTED':
        return {
          ...state,
          loading: true
        }
      case GET_DRUG_BY_ID + '_SUCCESS':
        return {
          ...state,
          loading: false,
          error: null,
          drug: action.payload
        }
      case GET_DRUG_BY_ID + '_FAILURE':
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
          statics: action.payload
        }
      case GET_DRUG_BY_IDS + '_FAILURE':
        return {
          ...state,
          loading: false,
          error: action.payload.error
        }
      case DRUGS_CLEAN + '_SUCCESS':
        return {
          loading: false,
          drugs: null,
          drug: null,
          error: null,
          statics: [],
        }
    default:
      return state;
  }
}
export default drugs;

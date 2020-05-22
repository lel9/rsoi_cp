import {
  GET_PROFILE_BY_ID,
  PUT_PROFILE
 } from  '../constants';

const initialState = {
  loading: false,
  profile: null,
  error: null
};

const drugs = function reducer(state = initialState, action) {
  switch (action.type) {
    case GET_PROFILE_BY_ID + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case GET_PROFILE_BY_ID + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        profile: action.payload
      }
    case GET_PROFILE_BY_ID + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
      case PUT_PROFILE + '_STARTED':
        return {
          ...state,
          loading: true
        }
      case PUT_PROFILE + '_SUCCESS':
        return {
          ...state,
          loading: false,
          error: null,
          profile: action.payload
        }
      case PUT_PROFILE + '_FAILURE':
        return {
          ...state,
          loading: false,
          error: action.payload.error
        }
    default:
      return state;
  }
}
export default drugs;

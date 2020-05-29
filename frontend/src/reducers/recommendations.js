import {
  POST_RECOMMENDATIONS
} from '../constants';

const initialState = {
  loading: false,
  recommendations: [],
  recommendation: null,
  error: null,
};

const recommendations = function reducer(state = initialState, action) {
  switch (action.type) {
    case POST_RECOMMENDATIONS + '_STARTED':
      return {
        ...state,
        loading: true
      }
    case POST_RECOMMENDATIONS + '_SUCCESS':
      return {
        ...state,
        loading: false,
        error: null,
        recommendation: action.payload
      }
    case POST_RECOMMENDATIONS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    default:
      return state;
  }
}
export default recommendations;

import {
  POST_RECOMMENDATIONS,
  RECOMMENDATION_CLEAN
} from '../constants';

const initialState = {
  loading: false,
  recommendations: [],
  size: 0,
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
        recommendations: action.payload.results,
        size: action.payload.results.length,
      }
    case POST_RECOMMENDATIONS + '_FAILURE':
      return {
        ...state,
        loading: false,
        error: action.payload.error
      }
    case RECOMMENDATION_CLEAN + '_SUCCESS':
      return {
        loading: false,
        recommendations: [],
        recommendation: null,
        error: null,
      }
    default:
      return state;
  }
}
export default recommendations;

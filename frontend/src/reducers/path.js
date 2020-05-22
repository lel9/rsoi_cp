import { CHANGE_PATH } from '../constants';

const initialState = {
  path: '',
};

const path = function reducer(state = initialState, action) {
  switch (action.type) {
    case CHANGE_PATH:
      return {
        ...state,
        path: action.payload
      }
    default:
      return state;
  }
}

export default path;

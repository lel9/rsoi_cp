import { CHANGE_PATH } from '../constants';

// export const changePath = (path) =>  {
//   return dispatch => {
//     dispatch({
//       type: CHANGE_PATH,
//       payload: {
//         path
//       }
//     })
//   }
// }

export const changePath = (path) =>  ({
  type: CHANGE_PATH,
  payload: {
    path
  }
})

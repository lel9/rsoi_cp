import {
  ADD_COMMENT,
  GET_COMMENTS,
  // UPDATE_COMMENT,
  // DELETE_COMMENT,
  commentsPublic,
  commentsProtected
 } from  '../constants';

 // export <key>=<value> // env
import { handleError } from './actionSession';
import axios from 'axios';
import { sessionService } from 'redux-react-session';
//
// const url = process.env.URL;
// console.log(process.env);

export const addComment = (data) => {
  const { id, text } = data;
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(addCommentStarted());
      axios.post(commentsProtected, {
        text: text,
        drugId: id,
      },{
        headers: headers
      })
      .then(res => {
        dispatch(addCommentSuccess(res.data));
        dispatch(getComments({id: id, page: 0, size: 15}));
        alert('Рекомендация успешно добавлена');
      })
      .catch(err => {
        let error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(ADD_COMMENT, error, addComment, data))
        dispatch(addCommentFailure(err.response));
      })
    })
  }
}

export const getComments = (data) => {
  const { id, page, size } = data
  return dispatch => {
    dispatch(getCommentsStarted());
    axios.get(commentsPublic, {
      params: {
        drugId: id,
        page: page,
        size: size
      }
    })
    .then(res => {
      dispatch(getCommentsSuccess(res.data, id));
    })
    .catch(err => {
      let error = err.message === 'Network Error' ? err.message : err.response.data
      dispatch(handleError(ADD_COMMENT, error, getComments, data))
      dispatch(getCommentsFailure(err.response));
    })
  }
}

////////////////////////////////////////////////////////////////////////////////
const addCommentStarted = () => ({
  type: ADD_COMMENT + '_STARTED',
})

const addCommentSuccess = data => ({
  type: ADD_COMMENT + '_SUCCESS',
  payload: {
    data
  }
})

const addCommentFailure = error => ({
  type: ADD_COMMENT + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////
const getCommentsStarted = () => ({
  type: GET_COMMENTS + '_STARTED',
})

const getCommentsSuccess = (data, id) => ({
  type: GET_COMMENTS + '_SUCCESS',
  payload: {
    ...data
  },
  id: id
})

const getCommentsFailure = error => ({
  type: GET_COMMENTS + '_FAILURE',
  payload: {
    error
  }
})

////////////////////////////////////////////////////////////////////////////////

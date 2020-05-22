import {
  // UPDATE_COMMENT,
  // DELETE_COMMENT,
  ADD_COMMENT,
  GET_COMMENTS,
 } from  '../constants';

 const initialState = {
   loading: false,
   comments: null ,
   error: null,
   results: []
 };

 const comments = function reducer(state = initialState, action) {
   switch (action.type) {
     case ADD_COMMENT + '_STARTED':
       return {
         ...state,
         loading: true
       }
     case ADD_COMMENT + '_SUCCESS':
       return {
         ...state,
         loading: false,
         error: null,
         comments: action.payload
       }
     case ADD_COMMENT + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload.error
       }
       case GET_COMMENTS + '_STARTED':
         return {
           ...state,
           loading: true
         }
       case GET_COMMENTS + '_SUCCESS':
         return {
           ...state,
           loading: false,
           error: null,
           comments: action.payload,
           results: action.payload.results
         }
       case GET_COMMENTS + '_FAILURE':
         return {
           ...state,
           loading: false,
           error: action.payload.error
         }
     default:
       return state;
   }
 }
 export default comments;

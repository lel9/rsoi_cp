import {
  // UPDATE_COMMENT,
  // DELETE_COMMENT,
  ADD_COMMENT,
  GET_COMMENTS,
  COMMENTS_CLEAN,
 } from  '../constants';

 const initialState = {
   loading: false,
   comments: null ,
   error: null,
   results: [],
   pid: ''
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
         comments: action.payload,
         pid: ''
       }
     case ADD_COMMENT + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload.error,

       }
       case GET_COMMENTS + '_STARTED':
         return {
           ...state,
           loading: true
         }
       case GET_COMMENTS + '_SUCCESS':
         if (state.pid !== action.id) {
           return {
             ...state,
             loading: false,
             error: null,
             comments: action.payload,
             results: action.payload.results,
             pid: action.id,
           }
         }
         return {
           ...state,
           loading: false,
           error: null,
           comments: action.payload,
           results: [...state.results, ...action.payload.results],
         }

       case GET_COMMENTS + '_FAILURE':
         return {
           ...state,
           loading: false,
           error: action.payload.error
         }

       case COMMENTS_CLEAN + '_SUCCESS':
         return {
           loading: false,
           comments: null ,
           error: null,
           results: [],
           pid: ''
         }
     default:
       return state;
   }
 }
 export default comments;

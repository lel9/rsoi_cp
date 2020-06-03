import {
  SESSION_LOGOUT,
  SESSION_AUTHORIZATION,
  SESSION_REFRESH,
  SESSION_CHECK,
  REGISTRATION,
  NETWORK_ERROR
 } from  '../constants';

 const initialState = {
   loading: false,
   token: [],
   user: [],
   error: null,
   newUser: []
 };

 const session = function reducer(state = initialState, action) {
   switch (action.type) {
     case REGISTRATION + '_STARTED':
       return {
         ...state,
         loading: true
       }
     case REGISTRATION + '_SUCCESS':
       return {
         ...state,
         loading: false,
         error: null,
         newUser: action.payload,
       }
     case REGISTRATION + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload
       }

     case SESSION_LOGOUT + '_SUCCESS':
       return {
         ...state,
         loading: false
       }

     case NETWORK_ERROR:
       return {
         ...state,
         error: action.payload
       }

     case SESSION_CHECK + '_STARTED':
       return {
         ...state,
         loading: true
       }
     case SESSION_CHECK + '_SUCCESS':
       return {
         ...state,
         loading: false,
         error: null,
         user: action.payload
       }
     case SESSION_CHECK + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload.error
       }

     case SESSION_AUTHORIZATION + '_STARTED':
       return {
         ...state,
         loading: true
       }
     case SESSION_AUTHORIZATION + '_SUCCESS':
       return {
         ...state,
         loading: false,
         error: null,
         token: action.payload
       }
     case SESSION_AUTHORIZATION + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload
       }

     case SESSION_REFRESH + '_STARTED':
       return {
         ...state,
         loading: true,
         error: null,
       }
     case SESSION_REFRESH + '_SUCCESS':
       return {
         ...state,
         loading: false,
         error: null,
         token: action.payload
       }
     case SESSION_REFRESH + '_FAILURE':
       return {
         ...state,
         loading: false,
         error: action.payload.error
       }
         default:
       return state;
   }
 }
 export default session;

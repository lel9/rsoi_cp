import {
  SESSION_LOGOUT,
  SESSION_AUTHORIZATION,
  SESSION_REFRESH,
  SESSION_CHECK,
  REGISTRATION,
  NETWORK_ERROR,
  COMMENTS_CLEAN,
  DRUGS_CLEAN,
  PATIENTS_CLEAN,
  RECEPTIONS_CLEAN,
  PATH_CLEAN,
  PROFILE_CLEAN,
  STATISTICS_CLEAN,
  oauthURL,
  registrationURL,
  grant_types
} from '../constants';



import axios from 'axios';
import { sessionService } from 'redux-react-session';

import getHistory from '../modules/history';

const headers = {
  'Authorization': 'Basic Y2xpZW50OnNlY3JldA==',
  'Content-Type': 'multipart/form-data',
}

export const authorization = (username, password) => {
  const bodyFormData = new FormData();
  bodyFormData.set('username', username);
  bodyFormData.set('password', password);
  bodyFormData.set('grant_type', grant_types['password']);
  return dispatch => {
    dispatch(authorizationStarted());
    axios({
      method: 'post',
      url: oauthURL + 'token',
      data: bodyFormData,
      headers: headers
    })
    .then(res => {
      dispatch(authorizationSuccess(res.data));
      console.log('new_token', res.data);
      dispatch(checkToken(res.data.access_token));
      sessionService.saveSession(res.data)
      // sessionService.saveSession({
      //     access_token: "898fc1ee-4af4-4a3d-ac68-7f1bf381579bz",
      //     expires_in: 3297,
      //     refresh_token: "7e9e6077-195c-4018-acc7-56ff254790a4",
      //     scope: "all",
      //     token_type: "bearer",
      // })
      .then (() => {
        getHistory().push('/all-drugs');
      });
    })
    .catch(err => {
      dispatch(authorizationFailure(err.response));
    })
  }
}

export const registration = (username, password) => {
  return dispatch => {
    dispatch(registrationStarted());
    axios.post(registrationURL, {
      username,
      password
    })
    .then(res => {
      dispatch(registrationSuccess({username: username, id: res.data}));
      getHistory().push('/sign-in');
    })
    .catch(err => {
      dispatch(registrationFailure(err.response));
    })
  }
}

export const logout = () => {
  return dispatch => {
    dispatch(logoutSuccess())
    sessionService.deleteSession();
    sessionService.deleteUser();
    getHistory().push('/');
    dispatch(cleanAll());
  }
}

export const refreshToken = (refresh_token, func, data) => {
  const bodyFormData = new FormData();
  bodyFormData.set('refresh_token', refresh_token);
  bodyFormData.set('grant_type', grant_types['refresh_token']);
  return dispatch => {
    dispatch(refreshStarted());
    axios({
      method: 'post',
      url: oauthURL + 'token',
      data: bodyFormData,
      headers: headers
    })
    .then(res => {
      dispatch(refreshSuccess(res.data));
      console.log('refresh_token', res.data);
      sessionService.saveSession(res.data).then(() => {
        dispatch(func(data));
      })
      // sessionService.saveUser(res.data)
    })
    .catch(err => {
      dispatch(refreshFailure(err.response));
      dispatch(logoutSuccess())
      sessionService.deleteSession();
      sessionService.deleteUser();
      getHistory().push('/sign-in');
      dispatch(cleanAll());
    })
  }
}

export const checkToken = (access_token) => {
  const bodyFormData = new FormData();
  bodyFormData.set('token', access_token);
  return dispatch => {
    dispatch(checkStarted());
    axios({
      method: 'post',
      url: oauthURL + 'check_token',
      data: bodyFormData,
      headers: headers
    })
    .then(res => {
      dispatch(checkSuccess(res.data));
      console.log(res.data);
      sessionService.saveUser(res.data)
      .then (() => {
        // getHistory().push('/pewpew');
      });
    })
    .catch(err => {
      dispatch(checkFailure(err.response));
    })
  }
}

export const handleError = (dst, err, func, data) => {
  return dispatch => {
    console.log(err);
    sessionService.loadSession().then(session => {
      if (err === 'Network Error') {
        dispatch(error(dst, err.error));
        alert('Сервис временно недоступен');
      }
      else {
        switch (err.error) {
          case 'invalid_token':
            dispatch(refreshToken(session.refresh_token, func, data));
            break;
          default:
            alert(err.error_description);
            break;
        }
      }
    });
  }
}


export const cleanAll = () => {
  return dispatch => {
    dispatch(commentsClean());
    dispatch(drugsClean());
    dispatch(patientsClean());
    dispatch(receptionsClean());
    dispatch(pathClean());
    dispatch(profileClean());
    dispatch(statisticsClean());
  }
}

////////////////////////////////////////////////////////////////////////////////
const error = (dst, err) => ({
  type: NETWORK_ERROR,
  payload: {
    dst,
    err
  }
})
////////////////////////////////////////////////////////////////////////////////
const registrationStarted = () => ({
  type: REGISTRATION + '_STARTED'
})

const registrationSuccess = data => ({
  type: REGISTRATION + '_SUCCESS',
  payload: {
    ...data
  }
})

const registrationFailure = error => ({
  type: REGISTRATION + '_FAILURE',
  payload: {
    error
  }
})
////////////////////////////////////////////////////////////////////////////////
const checkStarted = () => ({
  type: SESSION_CHECK + '_STARTED'
})

const checkSuccess = data => ({
  type: SESSION_CHECK + '_SUCCESS',
  payload: {
    ...data
  }
})

const checkFailure = error => ({
  type: SESSION_CHECK + '_FAILURE',
  payload: {
    error
  }
})
////////////////////////////////////////////////////////////////////////////////
const refreshStarted = () => ({
  type: SESSION_REFRESH + '_STARTED'
})

const refreshSuccess = data => ({
  type: SESSION_REFRESH + '_SUCCESS',
  payload: {
    ...data
  }
})

const refreshFailure = error => ({
  type: SESSION_REFRESH + '_FAILURE',
  payload: {
    error
  }
})
////////////////////////////////////////////////////////////////////////////////
const logoutSuccess = () => ({
  type: SESSION_LOGOUT + '_SUCCESS'
})
////////////////////////////////////////////////////////////////////////////////
const authorizationStarted = () => ({
  type: SESSION_AUTHORIZATION + '_STARTED',
})

const authorizationSuccess = data => ({
  type: SESSION_AUTHORIZATION + '_SUCCESS',
  payload: {
    ...data
  }
})

const authorizationFailure = error => ({
  type: SESSION_AUTHORIZATION + '_FAILURE',
  payload: {
    error
  }
})


////////////////////////////////////////////////////////////////////////////////
export const commentsClean = () => ({
  type: COMMENTS_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const drugsClean = () => ({
  type: DRUGS_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const patientsClean = () => ({
  type: PATIENTS_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const receptionsClean = () => ({
  type: RECEPTIONS_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const pathClean = () => ({
  type: PATH_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const profileClean = () => ({
  type: PROFILE_CLEAN + '_SUCCESS',
})
////////////////////////////////////////////////////////////////////////////////
const statisticsClean = () => ({
  type: STATISTICS_CLEAN + '_SUCCESS',
})

import {
  GET_STATISTICS,
  GET_USER_BY_IDS,
  statisticsProtected
} from '../constants';

import axios from 'axios';
import { sessionService } from 'redux-react-session';
import { handleError } from './actionSession';
import { getDrugsByIds } from './actionDrug';
import { getPatientsByIds } from './actionPatient';

export const getStatistics = (data) => {
  const { dateStart, dateEnd, entity } = data;
  return dispatch => {
    sessionService.loadSession().then(session => {
      const headers = {
        Authorization: `Bearer ${session.access_token}`
      }
      dispatch(getStatisticsStarted(dateStart, dateEnd));
      dispatch(getUsersIdsStarted());
      axios.get(statisticsProtected, {
        params: {
          dateStart,
          dateEnd,
          entity,
        },
        headers: headers
      })
      .then(res => {
        const ids = res.data.entitiesStatistic.map(el => {return el.id}).join('&ids=');
        dispatch(getStatisticsSuccess(res.data));
        if (entity === 'drug') {
          dispatch(getDrugsByIds(ids))
        } else if (entity === 'patient') {
          dispatch(getPatientsByIds(ids))
        } else if (entity === 'user') {
          dispatch(getUsersIdsSuccess(res.data));
        }
      })
      .catch(err => {
        if(entity !== 'user' ) {
          dispatch(getStatisticsFailure(err.response));
        } else {
          dispatch(getUsersIdsFailure(err.response));
        }
        const error = err.message === 'Network Error' ? err.message : err.response.data
        dispatch(handleError(GET_STATISTICS, error, getStatistics, data))
      })
    })
  }
}



const getStatisticsStarted = (dateStart, dateEnd) => ({
  type: GET_STATISTICS + '_STARTED',
  payload: {
    dateStart,
    dateEnd
  }

})

const getStatisticsSuccess = data => ({
  type: GET_STATISTICS + '_SUCCESS',
  payload: {
    data
  }
})

const getStatisticsFailure = error => ({
  type: GET_STATISTICS + '_FAILURE',
  payload: {
    error
  }
})
////////////////////////////////////////////////////////////////////////////////
const getUsersIdsStarted = () => ({
  type: GET_USER_BY_IDS + '_STARTED'
})

const getUsersIdsSuccess = data => ({
  type: GET_USER_BY_IDS + '_SUCCESS',
  payload: {
    data,
  },
  entity: 'user'
})

const getUsersIdsFailure = error => ({
  type: GET_USER_BY_IDS + '_FAILURE',
  payload: {
    error
  }
})

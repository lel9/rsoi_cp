import { combineReducers } from 'redux';
import { sessionReducer } from 'redux-react-session';

import drugs from './drugs';
import comments from './comments';
import patients from './patients';
import path from './path';
import sessions from './sessions';
import profile from './profile';
import statistics from './statistics';
import recommendations from './recommendations';

const rootReducer = combineReducers({
   drugs, comments, patients, path, sessions, profile, statistics,
   sessionReducer, recommendations
 });

export default rootReducer;

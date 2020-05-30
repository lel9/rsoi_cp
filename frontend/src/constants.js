export const UPDATE_DRUG = 'UPDATE_DRUG';
export const ADD_DRUG = 'ADD_DRUG';
export const GET_DRUGS = 'GET_DRUGS';
export const GET_DRUG_BY_ID = 'GET_DRUG_BY_ID';
export const GET_DRUG_BY_IDS = 'GET_DRUG_BY_IDS';
export const DRUGS_CLEAN = 'DRUGS_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const ADD_COMMENT = 'ADD_COMMENT';
export const GET_COMMENTS = 'GET_COMMENTS';
export const UPDATE_COMMENT = 'UPDATE_COMMENT';
export const DELETE_COMMENT = 'DELETE_COMMENT';
export const COMMENTS_CLEAN = 'COMMENTS_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const ADD_PATIENT = 'ADD_PATIENT';
export const GET_PATIENTS = 'GET_PATIENTS';
export const GET_PATIENT_BY_ID = 'GET_PATIENT_BY_ID';
export const GET_PATIENT_BY_IDS = 'GET_PATIENT_BY_IDS';
export const UPDATE_PATIENT = 'UPDATE_PATIENT';
export const DELETE_PATIENT = 'DELETE_PATIENT';
export const PATIENTS_CLEAN = 'PATIENTS_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const ADD_RECEPTION = 'ADD_RECEPTION';
export const GET_RECEPTION = 'GET_RECEPTION';
export const UPDATE_RECEPTION = 'UPDATE_RECEPTION';
export const DELETE_RECEPTION = 'DELETE_RECEPTION';
export const RECEPTIONS_CLEAN = 'RECEPTIONS_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const CHANGE_PATH = 'CHANGE_PATH';
export const PATH_CLEAN = 'PATH_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const SESSION_LOGOUT = 'SESSION_LOGOUT';
export const SESSION_AUTHORIZATION = 'SESSION_AUTHORIZATION';
export const SESSION_REFRESH = 'SESSION_REFRESH';
export const SESSION_CHECK = 'SESSION_CHECK';
export const REGISTRATION = 'REGISTRATION';
export const NETWORK_ERROR = 'NETWORK_ERROR';
////////////////////////////////////////////////////////////////////////////////
export const GET_PROFILE_BY_ID = 'GET_PROFILE_BY_ID';
export const PUT_PROFILE = 'PUT_PROFILE';
export const PROFILE_CLEAN = 'PROFILE_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const GET_STATISTICS = 'GET_STATISTICS';
export const GET_USER_BY_IDS = 'GET_USER_BY_IDS';
export const STATISTICS_CLEAN = 'STATISTICS_CLEAN';
////////////////////////////////////////////////////////////////////////////////
export const POST_RECOMMENDATIONS = 'POST_RECOMENDATIONS';
////////////////////////////////////////////////////////////////////////////////
export const drugsPublic = 'http://' + window._env_.api_host + ':' + window._env_.drug_port + '/api/1.0/public/drug';
export const drugsProtected = 'http://' + window._env_.api_host + ':' + window._env_.drug_port + '/api/1.0/protected/drug';
export const patientsPublic = 'http://' + window._env_.api_host + ':' + window._env_.patient_port + '/api/1.0/public/patient';
export const patientsProtected = 'http://' + window._env_.api_host + ':' + window._env_.patient_port + '/api/1.0/protected/patient';
export const commentsPublic = 'http://' + window._env_.api_host + ':' + window._env_.recommendation_port + '/api/1.0/public/recommendation/';
export const commentsProtected = 'http://' + window._env_.api_host + ':' + window._env_.recommendation_port + '/api/1.0/protected/recommendation/';
export const profileProtected = 'http://' + window._env_.api_host + ':' + window._env_.profile_port + '/api/1.0/protected/profile/';
export const oauthURL = 'http://' + window._env_.api_host + ':' + window._env_.session_port + '/oauth/';
export const registrationURL = 'http://' + window._env_.api_host + ':' + window._env_.session_port + '/registration/user';
export const statisticsProtected = 'http://' + window._env_.api_host + ':' + window._env_.statistic_port + '/api/1.0/protected/statistic';
export const recommendationsPublic = 'http://' + window._env_.api_host + ':' + window._env_.analyzer_port + '/api/1.0/public/analyzer';

////////////////////////////////////////////////////////////////////////////////
export const grant_types = {
  'password': 'password',
  'refresh_token': 'refresh_token',
};

////////////////////////////////////////////////////////////////////////////////
export const drugEnglToRus  = {
  'activeSubstance':'Действующее вещество',
  'composition':'Описание',
  'description':'Состав',
  'group':'Фармакотерапевтическая группа',
  'atx':'АТХ',
  'indications':'Показания к применению',
  'contraindications':'Противопоказания',
  'sideEffects':'Побочные эффекты',
  'overdose':'Передозировка',
  'releaseFormVSDosage':'Форма выпуска/дозировка',
  'manufacturer':'Производитель',
  'pharmacodynamics':'Фармакодинамика',
  'pharmacokinetics':'Фармакокинетика',
  'withCaution':'С осторожностью',
  'pregnancyAndLactation':'Применение при беременности и в период грудного вскармливания',
  'transportationСonditions':'Условия транспортирования',
  'storageСonditions':'Условия хранения',
  'storageLife':'Срок годности',
  'tradeName':'Торговое наименование',
  'certificateOwner':'Владелец регистрационного удостоверения',
  'interaction':'Взаимодействие с другими лекарственными средствами',
  'form':'Лекарственная форма',
  'vehicleImpact':'Влияние на способность управлять транспортными средствами и механизмами',
  'vacationFromPharmacies':'Условия отпуска',
  'directionForUse':'Способ применения и дозы',
  'specialInstruction':'Особые указания'
};

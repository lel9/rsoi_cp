import React, { Component } from 'react';
import { Switch, Route, Link } from "react-router-dom";
import {  Menu } from 'antd';
import { connect } from 'react-redux';

import Reception from './layouts/reception';
import AddDrug from './layouts/addDrug';
import Instruction from './layouts/instruction';
import Recommendation from './layouts/recommendation';
import DrugSelection from './layouts/drugSelection';
import AllDrugs from './layouts/allDrugs';
import AllPatients from './layouts/allPatients';
import AddPatient from './layouts/addPatient';
import SignIn from './layouts/signIn';
import SignUp from './layouts/signUp';
import Profile from './layouts/profile';
import Statistics from './layouts/statistics';
import PublicReceptions from './layouts/publicReceptions';


import BreadCrumb from './components/breadCrumb';

import { changePath } from './actions/actionPath';
import { logout } from './actions/actionSession';

const Error = () => {
  return (
    <div> 404 NOT FOUND</div>
  )
}

class App extends Component {
  state = {
    current: '',
    user: 'name',
    isDisabledPatients: true,
    isDisabledStatistics: true,
    role: ['ROLE_USER'],
    id: ''
  }

  setPath = (path) => {
  }

  handleOnClickSider = (e) => {
    this.setState({
      current: e.key,
      user: 'name'
    })
  }

  handleOnClickHeader = (e) => {
    this.setState({
      user: e.key,
      current: '',
    })
    if(e.key === 'logout') {
      this.props.logout()
    }
  }

  shouldComponentUpdate = (nextProps, nextState) => {
    if (JSON.stringify(nextProps.user) !== JSON.stringify(this.props.user)) {
      return true;
    }
    if (nextState.isDisabledPatients !== this.state.isDisabledPatients) {
      return true;
    }
    if (nextState.isDisabledStatistics !== this.state.isDisabledStatistics) {
      return true;
    }
    if (nextState.current !== this.state.current) {
      return true;
    }
    if (nextProps.path !== this.props.path) {
      return true;
    }
    return false;
  }

  componentDidUpdate = (prevProps, prevState) => {
    if (this.props.user !== prevProps.user) {
      let isDisabledPatients = true;
      let isDisabledStatistics = true;
      let id = '';
      if(Object.keys(this.props.user).length) {
        isDisabledPatients = !(this.props.user.authorities.includes('ROLE_ADMIN')
        || this.props.user.authorities.includes('ROLE_OPERATOR')
        || this.props.user.authorities.includes('ROLE_EXPERT'));
        isDisabledStatistics = !(this.props.user.authorities.includes('ROLE_ADMIN'));
        id = this.props.user.user_name;
      }
      this.setState({
        isDisabledPatients,
        isDisabledStatistics,
        id
      })
    }
    if (this.props.path !== prevProps.path) {
      if (this.props.path !== '') {
        this.setState({
          current: this.props.path.path.split('/')[1]
        })
      }
    }
  }

  render() {
    const { current, isDisabledPatients, isDisabledStatistics, id } = this.state;
    const { authenticated } = this.props;
    const navigation = [
      {name: "select-drug", title: "Поиск препарата"},
      {name: "all-drugs", title: "Все препараты"},
      {name: "all-patients", title: "Все пациенты", disabled: isDisabledPatients},
      {name: "statistics", title: "Статистика", disabled: isDisabledStatistics},
    ];

    return (
      <div className="App">
        <div className="header">
          {authenticated ?
            <Menu onClick={this.handleOnClickHeader} mode="horizontal"
              selectedKeys={[this.state.user]} theme={"dark"}
              >
              <Menu.Item key="profile">
                <Link to={`/profile/${id}`}>
                  Профиль
                </Link>
              </Menu.Item>
              <Menu.Item key="logout">
                Выйти
              </Menu.Item>
            </Menu>
            :
            <Menu onClick={this.handleOnClickHeader} mode="horizontal"
              theme={"dark"}
              >
              <Menu.Item key="login">
                <Link to="/sign-in">
                  Войти
                </Link>
              </Menu.Item>
            </Menu>
          }
        </div>
        <div className="body">
          <div className="navigation">
            <Menu onClick={this.handleOnClickSider} mode="inline"
              selectedKeys={current}
              >
              {navigation.map((element, index) => (
                <Menu.Item key={element.name} style={{display: element.disabled ? 'none' : 'block'}}>
                  <Link to={`/${element.name}`} key={element.name}>
                    {element.title}
                  </Link>
                </Menu.Item>
              ))}
            </Menu>
          </div>

          <div className="content">
            <div className="content__breadcrumb">
              <BreadCrumb/>
            </div>
            <Switch>
              <Route exact path="/select-drug" component={DrugSelection}/>
              <Route exact path="/all-drugs" component={AllDrugs}/>
              <Route exact path="/all-patients" component={AllPatients}/>
              <Route exact path="/select-drug/recommendation" component={Recommendation}/>
              <Route path="/all-drugs/instruction/:id" component={Instruction}/>
              <Route path="/all-patients/reception/:id" component={Reception}/>
              <Route path="/select-drug/recommendation/reception/:id" component={PublicReceptions}/>
              <Route path="/profile/:id" component={Profile}/>
              <Route path="/all-drugs/add-drug" component={AddDrug}/>
              <Route path="/all-patients/add-patient" component={AddPatient}/>
              <Route path="*/sign-in" component={SignIn}/>
              <Route path="/sign-up" component={SignUp}/>
              <Route path="/statistics" component={Statistics}/>
              <Route path="*" component={Error}/>
            </Switch>
          </div>
        </div>
      </div>
    )
  }
}
  export default connect(state => ({
    authenticated: state.sessionReducer.authenticated,
    path: state.path.path,
    user: state.sessionReducer.user,
  }), { changePath, logout })(App);

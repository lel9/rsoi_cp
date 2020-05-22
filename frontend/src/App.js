import React, { Component, Fragment } from 'react';
import { Switch, Route, Link } from "react-router-dom";
import { Breadcrumb, Menu } from 'antd';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import Reception from './layouts/reception';
import AddDrug from './layouts/addDrug';
import Instruction from './layouts/instruction';
import Recomendation from './layouts/recomendation';
import DrugSelection from './layouts/drugSelection';
import AllDrugs from './layouts/allDrugs';
import AllPatients from './layouts/allPatients';
import AddPatient from './layouts/addPatient';
import SignIn from './layouts/signIn';
import SignUp from './layouts/signUp';
import Profile from './layouts/profile';
import Statistics from './layouts/statistics';

import Portal from './components/portal';

import { changePath } from './actions/actionPath';
import { logout } from './actions/actionSession';
import getHistory from './modules/history';

const getCrumbs = (path, template) => {
  const crumbs = [];
  let positionEnd = 0;
  let link = '';
  path.split('/').map((element) => {
    positionEnd += element.length + 1;
    link = path.slice(0, positionEnd);
    if (element !== '') {
      crumbs.push({
        crumb: template[element],
        link: link,
      });
    }
  })
  return crumbs;
}

const Error = () => {
  return (
    <div> 404 NOT FOUND</div>
  )
}

const pages = {
  "select-drug": "Подбор препарата",
  "recomendation": "Рекомендация",
  "add-drug": "Добавить препарат",
  "reception": "Осмотры",
  "all-drugs": "Все препараты",
  "all-patients": "Все пациенты",
  "add-patient": "Добавить пациента",
  "instruction": "Инструкция",
  "statistics": "Статистика",
}



class App extends Component {
  state = {
    current: 'select-drug',
    user: 'name',
    path: '',
    isDisabledPatients: ['ROLE_USER'],
    role: ['ROLE_USER'],
    timer: 0,
    id: ''
  }
  //
  // static propTypes = {
  //   authenticated: PropTypes.bool.isRequired,
  // }
  //
  // static defaultProps = {
  //   authenticated: false,
  // }

  setPath = (path) => {
    this.setState({
      path: path,
    })
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
      // getHistory().push('/sign-in')
    }
  }

  shouldComponentUpdate = (nextProps, nextState) => {
    if (nextProps.user !== this.props.user) {
      return true;
    }
    if (nextState.isDisabledPatients !== this.state.isDisabledPatients) {
      return true;
    }
    if (nextState.current !== this.state.current) {
      return true;
    }
    if(this.state.path !== nextState.path) {
       return true
    }
    if(nextProps.authenticated !== this.props.authenticated) {
       return true
    }

    return false;
  }

  componentDidMount = () => {
    // const { user_name } = this.props.user;

    // getHistory().push('/select-drug')
    // console.log(getHistory().location.pathname.slice(0));
    this.props.changePath(getHistory().location.pathname);
    this.setState({
      // current: getHistory().location.pathname.slice(0)
    })
  }

  componentDidUpdate = (prevProps, prevState) => {
    if (this.props.user !== prevProps.user) {
      let isDisabledPatients = true;
      let id = '';
      if(Object.keys(this.props.user).length) {
        isDisabledPatients = !(this.props.user.authorities.includes('ROLE_ADMIN')
        || this.props.user.authorities.includes('ROLE_OPERATOR')
        || this.props.user.authorities.includes('ROLE_EXPERT'));
        id = this.props.user.user_name;
      }
      this.setState({
        isDisabledPatients,
        id
      })
    }
  }

  render() {
    const { path, current, isDisabledPatients, timer, id } = this.state;
    const { authenticated } = this.props;
    const crumbs = getCrumbs(path, pages)
    const navigation = [
      {name: "select-drug", title: "Подбор препарата"},
      {name: "all-drugs", title: "Все препараты"},
      {name: "all-patients", title: "Все пациенты", disabled: isDisabledPatients},
      {name: "statistics", title: "Статистика"},
    ];

    const WrapperContent1 = () => {
      return <DrugSelection setPath={this.setPath}/>
    }
    const WrapperContent2 = () => {
      return <AllDrugs setPath={this.setPath}/>
    }
    const WrapperContent3 = (props) => {
      return <Reception setPath={this.setPath} match={props.match}/>
    }
    const WrapperContent4 = (props) => {
      return <Instruction setPath={this.setPath} match={props.match}/>
    }
    const WrapperContent5 = () => {
      return <Recomendation setPath={this.setPath}/>
    }
    const WrapperContent6 = () => {
      return <AddDrug setPath={this.setPath}/>
    }
    const WrapperContent7 = () => {
      return <AllPatients setPath={this.setPath}/>
    }
    const WrapperContent8 = () => {
      return <AddPatient setPath={this.setPath}/>
    }
    const WrapperContent9 = () => {
      return <Portal><SignIn/></Portal>
    }
    const WrapperContent10 = () => {
      return <Portal><SignUp/></Portal>
    }
    const WrapperContent11 = (props) => {
      return <Profile setPath={this.setPath} match={props.match}/>
    }
    const WrapperContent12 = (props) => {
      return <Statistics setPath={this.setPath}/>
    }

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
                <Menu.Item key={element.name} disabled={element.disabled}>
                  <Link to={`/${element.name}`} key={element.name}>
                    {element.title}
                  </Link>
                </Menu.Item>
              ))}
            </Menu>
          </div>

          <div className="content">
            <div className="content__breadcrumb">
               <Breadcrumb>
                 {crumbs.map((element, index)=> (
                   <Breadcrumb.Item key={index}>
                     <Link to={element.link} key={index}>
                       {element.crumb}
                     </Link>
                   </Breadcrumb.Item>
                 ))}
              </Breadcrumb>
            </div>
            <Switch>
              <Route exact path="/select-drug" component={WrapperContent1}/>
              <Route exact path="/all-drugs" component={WrapperContent2}/>
              <Route exact path="/all-patients" component={WrapperContent7}/>
              <Route path="/all-drugs/add-drug" component={WrapperContent6}/>
              <Route path="/all-drugs/instruction/:id" component={WrapperContent4}/>
              <Route path="/all-patients/reception/:id" component={WrapperContent3}/>
              <Route path="/all-patients/add-patient" component={WrapperContent8}/>
              <Route path="/select-drug/recomendation" component={WrapperContent5}/>
              <Route path="*/sign-in" component={WrapperContent9}/>
              <Route path="/sign-up" component={WrapperContent10}/>
              <Route path="/profile/:id" component={WrapperContent11}/>
              <Route path="/statistics" component={WrapperContent12}/>
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

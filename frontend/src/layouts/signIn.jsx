import React, { Component } from 'react';
import { Button } from  'antd';
import { connect } from 'react-redux';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { authorization, logout } from '../actions/actionSession';
import InputField from '../components/inputField';
import getHistory from '../modules/history';
import Portal from '../components/portal';

const Elements = ({obj, classN, actionOnChange}) => obj.map((element, index) => (
  <InputField
    key={index}
    placeholder={element.label}
    className={classN}
    func={actionOnChange}
    lel9={element.lel9}
    reference={element.ref}
    tag='input'
    type={element.type}
  />
))

class SignIn extends Component{
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      green: '#0fc5c5',
      color: '#0fc5c5',
      blue: '#3c97e4',
      errorS: '',
    }
    this.usernameRef = React.createRef();
    this.passwordRef = React.createRef();
    this.portalRef = React.createRef();
    this.handleClickOutside = this.handleClickOutside.bind(this);
  }

  componentDidMount() {
    document.addEventListener('click', this.handleClickOutside);
  }

  componentWillUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  }

  handleClickOutside(e) {
    if (this.portalRef.current && !this.portalRef.current.contains(e.target)) {
      getHistory().replace('/');
    }
  }

  handleOnChange = () => {
    this.setState({
      username: this.usernameRef.current.value,
      password: this.passwordRef.current.value
    })
  }

  handleOnClick = () => {
    const { username, password } = this.state;
    if(username === '' || password === '') {

    } else {
      this.props.authorization({username: username, password: password});
    }
  }

  handleOnMouseDown = () => {
    const { blue } = this.state;
    this.setState({
      color: blue,
    })
  }

  handleOnMouseUp = () => {
    const { green } = this.state;
    this.setState({
      color: green,
    })
  }

  handleOnClickClose = () => {
    getHistory().push('/');
  }

  handleOnClickSignUp = () => {
    getHistory().push('/sign-up');
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.errorS !== prevProps.errorS) {
      if (this.props.errorS.error === undefined) {
          this.setState({
            error: this.props.errorS.err
          })
          // alert('Сервис временно недоступен')
      } else {
        if(this.props.errorS.error) {
          this.setState({
            errorS: this.props.errorS.error.data.error
          })
        }
      }
    }
  }

  render () {
    const { username, password, color, errorS } = this.state;

    const Parametres1 = [
      {lel9: username, label: "Логин", ref: this.usernameRef},
      {lel9: password, label: "Пароль", type: "password", ref: this.passwordRef}
    ];

    return(
      <Portal>
        <div className="signIn" onClick={this.handleClickOutside}>
          <div className="signIn-wrapper" ref={this.portalRef}>
            <div className="signIn-close" onClick={this.handleOnClickClose}>
              <FontAwesomeIcon icon={faTimes} size="lg"/>
            </div>
            <div className="signIn-tittle">ВХОД</div>
            <Elements obj={Parametres1} actionOnChange={this.handleOnChange} classN="SignIn"/>
            {errorS !== '' &&
            <p className="signIn-wrongData">Неверное имя пользователя или пароль</p>
            }
            <Button style={{background: color}}
              onMouseDown={this.handleOnMouseDown}
              onMouseUp={this.handleOnMouseUp}
              onClick={this.handleOnClick}
              >
              Войти
            </Button>
            <div
              onClick={this.handleOnClickSignUp}
              className="signIn-signUp"
              >
              Регистрация
            </div>
          </div>
        </div>
      </Portal>
    )
  }
}

export default connect(state => ({
  errorS: state.sessions.error,
}), { authorization, logout })(SignIn)

import React, { Component } from 'react';
import { Button } from  'antd';
import { connect } from 'react-redux';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { registration } from '../actions/actionSession';
import InputField from '../components/inputField';
import Portal from '../components/portal';
import getHistory from '../modules/history';

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

class SignUp extends Component{
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      green: '#0fc5c5',
      color: '#0fc5c5',
      blue: '#3c97e4',
      isEmpty: false,
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
      getHistory().push('/');
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
      this.setState({
        isEmpty: true
      })
    } else {

      this.props.registration({username: username, password: password});
      this.setState({
        isEmpty: false
      })
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

  handleOnClickSignIn = () => {
    getHistory().push('/sign-in');
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.errorS !== prevProps.errorS) {
      // console.log(this.props.errorS);
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
    const { username, password, color, isEmpty, errorS } = this.state;

    const Parametres1 = [
      {lel9: username, label: "Логин", ref: this.usernameRef},
      {lel9: password, label: "Пароль", type: "password", ref: this.passwordRef}
    ];

    return(
      <Portal>
        <div className="signUp" onClick={this.handleClickOutside} >
          <div className="signUp-wrapper" ref={this.portalRef}>
            <div className="signUp-close" onClick={this.handleOnClickClose}>
              <FontAwesomeIcon icon={faTimes} size="lg"/>
            </div>
            <p className="signUp-tittle">РЕГИСТРАЦИЯ</p>

            <Elements obj={Parametres1} actionOnChange={this.handleOnChange} classN="signUp"/>
            {errorS !== '' &&
            <p className="signUp-userExist">Пользователь с таким именем уже существует</p>
            }
            {isEmpty &&
            <p className="signUp-emptyField">Заполните все поля</p>
            }
            <Button style={{background: color}}
              onMouseDown={this.handleOnMouseDown}
              onMouseUp={this.handleOnMouseUp}
              onClick={this.handleOnClick}
              >
              Зарегистрироваться
            </Button>
            <div
                onClick={this.handleOnClickSignIn}
                className="signUp-signIn"
            >
              Вход
            </div>
          </div>
        </div>
      </Portal>
    )
  }
}

export default connect(state => ({
  errorS: state.sessions.error
}), { registration })(SignUp)

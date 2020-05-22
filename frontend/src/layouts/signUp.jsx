import React, { Component } from 'react';
import { Button } from  'antd';
import { connect } from 'react-redux';
import { sessionService } from 'redux-react-session';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { registration } from '../actions/actionSession';
import InputField from '../components/inputField';
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
      this.props.registration(username, password)
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

  render () {
    const { username, password, color, isEmpty } = this.state;
    const { error } = this.props;
    console.log(error);

    const Parametres1 = [
      {lel9: username, label: "Логин", ref: this.usernameRef},
      {lel9: password, label: "Пароль", ref: this.passwordRef}
    ];

    return(
      <div className="signUp" onClick={this.handleClickOutside} >
        <div className="signUp-wrapper" ref={this.portalRef}>
          <div className="signUp-close" onClick={this.handleOnClickClose}>
            <FontAwesomeIcon icon={faTimes} size="lg"/>
          </div>
          <p className="signUp-tittle">РЕГИСТРАЦИЯ</p>
          {error ?
            <p className="signUp-userExist">{error.data.error_description}</p>
            :
            isEmpty ?
            <p className="signUp-emptyField">Заполните все поля</p>
            :
            null
          }

          <Elements obj={Parametres1} actionOnChange={this.handleOnChange} classN="signUp"/>
          <Button style={{background: color}}
            onMouseDown={this.handleOnMouseDown}
            onMouseUp={this.handleOnMouseUp}
            onClick={this.handleOnClick}
            >
            ЗАРЕГИСТРИРОВАТЬСЯ
          </Button>
        </div>
      </div>
    )
  }
}

export default connect(state => ({
  error: state.sessions.error
}), { registration })(SignUp)

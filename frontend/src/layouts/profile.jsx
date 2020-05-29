import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Button } from 'antd';
import InputField from '../components/inputField' ;
import { updateProfile, getProfileProtected } from '../actions/actionProfile';
import { changePath } from '../actions/actionPath.js';
import getHistory from '../modules/history';

const Elements = ({obj, classN, actionOnChange}) => obj.map((element, index) => (
  <InputField
    key={index}
    label={element.label}
    className={classN}
    func={actionOnChange}
    lel9={element.lel9}
    reference={element.ref}
    tag="input"
    required={true}
    disabled={element.disabled}
  />
))

class Profile extends Component {
  state = {
    displayName: '',
    organization: '',
    profession: '',
    disabled: true,
    id: '',
    error: null,
  };

  displayNameRef = React.createRef();
  organizationRef = React.createRef();
  professionRef = React.createRef();

  handleOnChange = () => {
    this.setState({
      displayName: this.displayNameRef.current.value,
      organization: this.organizationRef.current.value,
      profession: this.professionRef.current.value,
    })
  }

  componentDidMount = () => {
    console.log('hello');
    const id = this.props.match.params.id;
    console.log(id);
    this.props.getProfileProtected(id)
    this.props.changePath(getHistory().location.pathname);
    // this.props.setPath(getHistory().location.pathname);
    this.setState({
      id,
    })
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.location !== prevProps.location) {
      const id = this.props.location.pathname.split('/').slice(-1)[0];
      this.props.changePath(getHistory().location.pathname);
      this.props.getProfileProtected(id)
    }
    if (this.props.profile !== prevProps.profile) {
      this.setState({
        ...this.props.profile
      })
    }
    if(this.props.error !== prevProps.error) {
        if (this.props.error && this.props.error.data.error === 'invalid_profileIn') {
          this.setState({
            error: this.props.error
          })
        }
      }
  }

  handleOnClickSave = () => {
    const data = this.state;
    this.props.updateProfile(data);
  }

  render() {
    const { displayName, organization, profession, id, error } = this.state;
    const { authenticated } = this.props;
    const disabled = id === this.props.user.user_name ? false : true;

    const Parametres = [
      {disabled: disabled, lel9: displayName, label: "ФИО", ref: this.displayNameRef},
      {disabled: disabled, lel9: organization, label: "Организация", ref: this.organizationRef},
      {disabled: disabled, lel9: profession, label: "Должность", ref: this.professionRef},
     ];

    return (
      <div className="profile">
        { authenticated ?
          <Fragment>
            <Elements obj={Parametres} actionOnChange={this.handleOnChange} classN="profile"/>
            { !disabled &&
            <div className="profile__footer">
              {error &&
              <div className="emptyField prifile-emptyField">{error.data.error_description}</div>
              }
              <Button onClick={this.handleOnClickSave}>Сохранить</Button>
            </div>
            }
          </Fragment>
          :
          <div className="profile__nonAuthenticated">Авторизуйтесь для просмотра этой страницы</div>
        }

      </div>
    )
  }
}

export default connect(state => ({
  authenticated: state.sessionReducer.authenticated,
  profile: state.profile.profile,
  user: state.sessionReducer.user,
  path: state.path.path,
  error: state.profile.error
}), { changePath, updateProfile, getProfileProtected })(Profile);

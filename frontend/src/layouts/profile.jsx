import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button } from 'antd';
import InputField from '../components/inputField' ;
import { updateProfile, getProfileProtected } from '../actions/actionProfile';
import { sessionService } from 'redux-react-session';

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
  />
))

class Profile extends Component {
  state = {
    displayName: '',
    organization: '',
    profession: '',
    disabled: true,
    id: '',
  }

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
    const id = this.props.match.params.id;
    this.props.getProfileProtected(id)
    this.setState({
      id,
    })
  }

  componentDidUpdate = (prevProps) => {
    if(this.props.profile != prevProps.profile) {
      this.setState({
        ...this.props.profile
      })
    }
  }

  handleOnClickSave = () => {
    const data = this.state;
    this.props.updateProfile(data);
  }

  render() {
    const { displayName, organization, profession, disabled, userName } = this.state;

    const Parametres = [
      {disabled: disabled, lel9: displayName, label: "ФИО", ref: this.displayNameRef},
      {disabled: disabled, lel9: organization, label: "Организация", ref: this.organizationRef},
      {disabled: disabled, lel9: profession, label: "Должность", ref: this.professionRef},
     ];

    return (
      <div className="profile">
        <Elements obj={Parametres} actionOnChange={this.handleOnChange} classN="profile"/>
        <Button onClick={this.handleOnClickSave}>Сохранить</Button>
      </div>
    )
  }
}

export default connect(state => ({
  profile: state.profile.profile,
  user: state.sessionReducer.user,
}), { updateProfile, getProfileProtected })(Profile);

import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, DatePicker, Radio } from 'antd';
import { addPatient } from '../actions/actionPatient.js';
import getHistory from '../modules/history';
import InputField from '../components/inputField' ;

import { changePath } from '../actions/actionPath.js';
import dayjs from "dayjs";

class AddPatient extends Component {
  state = {
    birthday: '',
    cardId: '',
    sex: '',
    error: null
  }

  cardIdRef = React.createRef();

  handleOnChange = () => {
    this.setState({
      cardId: this.cardIdRef.current.value,
    })
  }

  handleOnChangeDate = (date) => {
    this.setState({
      birthday: dayjs(date).format()
    })
  }

  handleOnChangeSex = ({target}) => {
    this.setState({
      sex: target.value
    })

  }
  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    getHistory().push(`/all-patients/add-patient`)
  }

  componentDidUpdate = (prevProps) => {
    if(this.props.error !== prevProps.error) {
      if (this.props.error && this.props.error.data.error === 'patient_already_exists') {
        this.setState({
          error: this.props.error
        })
      }
    }
  }

  handleOnSubmit = () => {
    let data = this.state;
    this.props.addPatient(data);
  }

  render() {
    const { error } = this.state;
    return(
      <div className="addPatient">
        <div className="addPatient__content">
          <div className="addPatient__datePicker">
            <label>Дата рождения</label>
            <DatePicker onChange={this.handleOnChangeDate} format={'YYYY-MM-DD'} />
          </div>
          <div className="addPatient__sex">
            <label>Пол</label>
            <Radio.Group onChange={this.handleOnChangeSex} value={this.state.sex}>
             <Radio value={"m"}>мужской</Radio>
             <Radio value={"f"}>женский</Radio>
           </Radio.Group>
          </div>
          <InputField
            label="Идентификатор"
            className="addPatient"
            func={this.handleOnChange}
            lel9={this.state.cardId}
            reference={this.cardIdRef}
            tag="input"
          />
        </div>
        {error &&
        <div className="emptyField addPatient-emptyField">{error.data.error_description}</div>
        }
        <div className="addPatient__footer">
            <Button onClick={this.handleOnSubmit}>Сохранить</Button>
        </div>
      </div>
    )
  }
}

export default connect(state => ({
error: state.patients.error
}), { changePath, addPatient })(AddPatient);

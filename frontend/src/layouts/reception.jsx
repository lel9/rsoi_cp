import React, { Component, Fragment } from 'react';
import { Select, TimePicker, DatePicker, Tabs, Menu, Dropdown, Button } from 'antd';
import getHistory from '../modules/history';
import InputField from '../components/inputField';
import { connect } from 'react-redux';
import { getPatientById } from '../actions/actionPatient';
import { getDrugs } from '../actions/actionDrug';
import { addReception, updateReception, deleteReception } from '../actions/actionReception';
import dayjs from 'dayjs'
import { changePath } from '../actions/actionPath.js';

const { Option } =  Select;
const { TabPane } = Tabs;



const Elements = ({obj, classN}) => obj.map((element, index) => (
  <InputField
    key={index}
    label={element.label}
    className={classN}
    func={element.actionOnChange}
    lel9={element.lel9}
    disabled={element.disabled}
    reference={element.ref}
  />
))

const children = [];
for (let i = 10; i < 36; i++) {
  children.push(<Option key={i.toString(36) + i}>{i.toString(36) + i}</Option>);
}

class Reception extends Component {
  state = {
    lifeAnamnesis: '',
    diseaseAnamnesis: '',
    plaints: '',
    objectiveInspection: '',
    examinationsResults: '',
    specialistsConclusions: '',
    text: '', //// diagnosis
    drugs: [],
    date: new Date().getTime(),
    time: new Date().getTime(),
    disabled: true,
    type: 'read',
    pid: '',
    rid: '',
    flag: 0,
    lengthReceptions: 0,
    currentView: 0,
    timer: 0,
    searchV: 'Please select',
    isSearch: false,
    drugsSelect: [],
    upLoad: false,
    sex: '',
    birthday: 0,
    isDisabled: false,
  }

  timer = () => {
    let { timer, isSearch, searchV } = this.state;
    if (isSearch && timer > 0.3) {
      this.props.getDrugs(searchV, 0, 15);
      this.setState({
        isSearch: false,
      })
    }
    if (timer < 0.4) {
      this.setState({
        timer: timer + 0.1,
      })
    }
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    this.props.setPath(getHistory().location.pathname);
    // console.log(this.props.getPath);
    const pid = this.props.match.params.id;
    this.intervalId = setInterval(this.timer, 100);
    this.props.getPatientById(pid);
    console.log('hello');
    let isDisabled = false;
    if(Object.keys(this.props.role).length) {
      isDisabled = (this.props.role.authorities.includes('ROLE_ADMIN')
      || this.props.role.authorities.includes('ROLE_OPERATOR'));
    }
    this.setState({
      pid,
      isDisabled,
    })

  }

  componentWillUnmount = () => {
    clearInterval(this.intervalId);
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.patient !== prevProps.patient) {
      if (this.props.receptions.length) {
        this.setState({
          ...this.props.receptions.slice(-1)[0].state,
          rid: this.props.receptions.slice(-1)[0].id,
          birthday: this.props.patient.birthday,
          sex: this.props.patient.sex,
          date: this.props.receptions.slice(-1)[0].date,
          lengthReceptions: this.props.receptions.length,
          currentView: this.props.receptions.length,
        })
      }
    }
    if (this.props.reception !== prevProps.reception) {
      if(this.state.type === 'new') {
        this.setState({
          rid: this.props.reception.data
        })
      }
    }
    if(this.props.drugs !== prevProps.drugs) {
      this.setState({
        drugsSelect: this.props.drugs.results
      })
    }
  }

  handleMenuClick = (e) => {
    console.log('click', e);
  }

  handleOnClickNew = (e) => {
    const { lengthReceptions } = this.state;
    this.setState({
      type: 'new',
      disabled: false,
      lengthReceptions: lengthReceptions + 1,
      currentView: lengthReceptions + 1,
    })
  }

  handleOnClickEdit = () => {
    this.setState({
      type: 'edit',
      disabled: false,
    })
  }

  handleOnClickRemove = () => {
    const { rid, pid, lengthReceptions } = this.state;
    this.props.deleteReception({pid: pid, rid: rid});
    if(lengthReceptions > 1) {
    this.setState({
      ...this.props.receptions.slice(lengthReceptions - 2)[0].state,
      rid: this.props.receptions.slice(lengthReceptions - 2)[0].id,
      date: this.props.receptions.slice(lengthReceptions - 2)[0].date,
      currentView: lengthReceptions - 1,
      lengthReceptions: lengthReceptions - 1,
      type: 'read',
    })
    } else {
      this.setState({
        lifeAnamnesis: '',
        diseaseAnamnesis: '',
        plaints: '',
        objectiveInspection: '',
        examinationsResults: '',
        specialistsConclusions: '',
        text: '',
        drugs: [],
        date: new Date().getTime(),
        time: new Date().getTime(),
        lengthReceptions: 0,
        type: 'read',
        currentView: 0,
      })
    }
  }

  handleOnChangeLifeAnamnesis = (e) => {
    this.setState({
      lifeAnamnesis: e.target.value
    })
  }

  handleOnChangeDiseaseAnamnesis = (e) => {
    this.setState({
      diseaseAnamnesis: e.target.value
    })
  }

  handleOnChangePlaints = (e) => {
    this.setState({
      plaints: e.target.value
    })
  }

  handleOnChangeObjectiveInspection = (e) => {
    this.setState({
      objectiveInspection: e.target.value
    })
  }

  handleOnChangeExaminationsResults = (e) => {
    this.setState({
      examinationsResults: e.target.value
    })
  }

  handleOnChangeSpecialistsConclusions = (e) => {
    this.setState({
      specialistsConclusions: e.target.value
    })
  }

  handleOnChangeDiagnosis = (e) => {
    this.setState({
      text: e.target.value
    })
  }

  handleOnSubmit = () => {
    const state = this.state;
    const { pid, type, text, date, rid, time} = this.state;
    const drugs = [] //
    const diagnosis = { text }
    if (type === 'new') {
      this.props.addReception({id: pid, date: date + time, diagnosis: diagnosis, drugs: drugs, state: state})
    }
    if (type === 'edit') {
      this.props.updateReception({pid: pid, rid: rid, date: date + time, diagnosis: diagnosis, drugs: drugs, state: state})
    }
    this.setState({
      type: 'read',
      disabled: true,
    })
  }

  handleOnChangeDate = (date, dateString) => {
    const dateParts = dateString.split('/');
    this.setState({
      date: new Date(dateParts[2], dateParts[1], dateParts[0]).getTime()
    })
  }

  handleOnChangeT = (time, timeString) => {
    const timeParts = timeString.split(':');
    this.setState({
      time: new Date(1970, 0, 0 , timeParts[0], timeParts[1], timeParts[2], 97200000).getTime()
    })
  }

  handleOnClickPrev = () => {
    let { currentView } = this.state;
    const isEnabledPaginationPrev = currentView > 1 ? true : false;
    currentView = currentView - 1
    if (isEnabledPaginationPrev) {
      this.setState({
        ...this.props.receptions.slice(currentView - 1)[0].state,
        rid: this.props.receptions.slice(currentView - 1)[0].id,
        date: this.props.receptions.slice(currentView - 1)[0].date,
        currentView: currentView,
        type: 'read',
      })
    }
  }

  handleOnClickNext = () => {
    let { currentView, lengthReceptions } = this.state;
    const isEnabledPaginationNext = currentView < lengthReceptions ? true : false;
    currentView = currentView + 1
    if (isEnabledPaginationNext) {
      this.setState({
        ...this.props.receptions.slice(currentView - 1)[0].state,
        rid: this.props.receptions.slice(currentView - 1)[0].id,
        date: this.props.receptions.slice(currentView - 1)[0].date,
        currentView: currentView,
        type: 'read',
      })
    }
  }

  handleOnChangeSelect = (value) => {
    console.log('change');
    console.log(value);
  }

  handleOnSearch = (searchV) => {
    this.setState({
      timer: 0,
      isSearch: true,
      searchV,
      upLoad: false
    })
  }

  select = () => {
    const { drugsSelect } = this.state;
    const children = [];
    for (let i = 0; i < drugsSelect.length; i++) {
      children.push(
        <Option key={i.toString(36) + i}>{drugsSelect[i].tradeName}</Option>
      )
    }
    return children;
  }

  render() {
    const { receptions } = this.props;
    const {
      disabled, lifeAnamnesis, diseaseAnamnesis, plaints, objectiveInspection,
      examinationsResults, specialistsConclusions, text, type, date, isDisabled,
      time, lengthReceptions, currentView, drugsSelect, birthday, sex
    } = this.state;

    const isDisabledMenu = lengthReceptions ? false : true;
    const isEnabledPaginationPrev = currentView > 1 ? true : false;
    const isEnabledPaginationNext = currentView < lengthReceptions ? true : false;

    const menu = (
      <Menu onClick={this.handleMenuClick}>
        <Menu.Item key="1" onClick={this.handleOnClickEdit} disabled={isDisabledMenu}>
          Редактировать
        </Menu.Item>
        <Menu.Item key="2" onClick={this.handleOnClickRemove} disabled={isDisabledMenu}>
          Удалить
        </Menu.Item>
      </Menu>
    );

    const Parametres1 = [
      {disabled: disabled, lel9: lifeAnamnesis, label: "Анаnnмез жизни",
        actionOnChange: this.handleOnChangeLifeAnamnesis},
      {disabled: disabled, lel9: diseaseAnamnesis, label: "Анамнез заболевания",
        actionOnChange: this.handleOnChangeDiseaseAnamnesis}
       ];

    const Parametres2 = [
      {disabled: disabled, lel9: plaints, label: "Жалобы",
        actionOnChange: this.handleOnChangePlaints},
      {disabled: disabled, lel9: objectiveInspection, label: "Объективный осмотр",
        actionOnChange: this.handleOnChangeObjectiveInspection},
      {disabled: disabled, lel9: examinationsResults, label: "Результаты исследований",
        actionOnChange: this.handleOnChangeExaminationsResults},
      {disabled: disabled, lel9: specialistsConclusions, label: "Заключения специалистов",
        actionOnChange: this.handleOnChangeSpecialistsConclusions},
       ];

    const Parametres3 = [
      {disabled: disabled, lel9: text, label: "Диагноз",
        actionOnChange: this.handleOnChangeDiagnosis}
       ];

    return(
      <div className="reception">
        { isDisabled ?
          <Fragment>
            <div className="reception__header">
              <div className="reception__header-center">
                {receptions.length ?
                  type === 'new' ?
                    <Fragment>
                      <DatePicker onChange={this.handleOnChangeDate} format={'DD/MM/YYYY'} />
                      <TimePicker onChange={this.handleOnChangeT}/>
                    </Fragment>
                    :
                    <Fragment>
                      <i className="fa fa-angle-double-left fa-1x" aria-hidden="true"
                        onClick={this.handleOnClickPrev}
                        style={ isEnabledPaginationPrev ? { cursor: 'pointer', color: 'black' } : { cursor: 'default', color: '#bdbcbc' } }
                      />
                      <label className="reception__date">{dayjs.unix(date/1000).format('DD/MM/YYYY')}</label>
                      <label className="reception__time">{dayjs.unix((date + time)/1000).format('HH:mm:ss')}</label>
                      <i className="fa fa-angle-double-right fa-1x" aria-hidden="true"
                        onClick={this.handleOnClickNext}
                        style={isEnabledPaginationNext ? { cursor: 'pointer', color: 'black' } : { cursor: 'default', color: '#bdbcbc' } }
                      />
                    </Fragment>
                  :
                  null
                }
              </div>
              <div className="reception__header-rightSide">
                { type === 'read' ?
                  <Dropdown.Button onClick={this.handleOnClickNew} overlay={menu}>
                    Новый
                  </Dropdown.Button>
                  :
                  <Button onClick={this.handleOnSubmit}>Сохранить</Button>
                }
              </div>
            </div>
            <div className="reception__content">
              <Tabs defaultActiveKey={'2'} onChange={null}>
                <TabPane tab="Анамнез" key="1">
                  <div className="reception__content-sex">
                    <p>Пол</p>
                    <label>
                      {sex === 'm' ? 'Мужской' : 'Женский'}
                    </label>
                  </div>
                  <div className="reception__content-brth">
                    <p>Возраст</p>
                    <label>
                      {dayjs.unix(birthday/1000).format('DD/MM/YYYY')}
                    </label>
                  </div>
                  <Elements obj={Parametres1} classN="reception"/>
                </TabPane>
                <TabPane tab="Текущее состояние" key="2">
                  <Elements obj={Parametres2} classN="reception"/>
                </TabPane>
                <TabPane tab="Диагноз" key="3">
                  <Elements obj={Parametres3} classN="reception"/>
                    <Select
                      mode="multiple"
                      style={{ width: '100%' }}
                      onChange={this.handleOnChangeSelect}
                      onSearch={this.handleOnSearch}
                    >
                    {drugsSelect.map((item, index) => (
                        <Option key={item.tradeName}>{item.tradeName}</Option>
                    ))}
                    </Select>
                </TabPane>
              </Tabs>
              {receptions.length?
                null
                :
                <div className="noData reception__noData">Нет осмотров</div>
              }
            </div>
          </Fragment>
          :
          <div className="noPermition reception__noPermition">
            вы лошара, идите вон
          </div>
        }
      </div>
    )
  }
}

export default connect(state => ({
  patient: state.patients.patient,
  reception: state.patients.reception,
  receptions: state.patients.receptions,
  drugs: state.drugs.drugs,
  role: state.sessionReducer.user
}), { getPatientById, addReception, updateReception, deleteReception, getDrugs, changePath})(Reception);

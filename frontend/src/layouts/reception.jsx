import React, { Component, Fragment } from 'react';
import { Select, DatePicker, Tabs, Menu, Dropdown, Button, List } from 'antd';
import { Link } from "react-router-dom";
import getHistory from '../modules/history';
import InputField from '../components/inputField';
import { connect } from 'react-redux';
import { getPatientById } from '../actions/actionPatient';
import { getDrugs } from '../actions/actionDrug';
import { addReception, updateReception, deleteReception } from '../actions/actionReception';
import dayjs from 'dayjs'
import { changePath } from '../actions/actionPath.js';
import { drugEnglToRus } from '../constants';

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
    date: '',
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
    years: null,
    months: null,
  }

  timer = () => {
    let { timer, isSearch, searchV } = this.state;
    if (isSearch && timer > 0.3) {
      this.props.getDrugs({tradeName: searchV, page: 0, size: 7});
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
    // this.props.setPath(getHistory().location.pathname);
    const pid = this.props.match.params.id;
    this.intervalId = setInterval(this.timer, 100);
    this.props.getPatientById(pid);
    this.setState({
      pid,
    })
  }

  componentWillUnmount = () => {
    clearInterval(this.intervalId);
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.patient !== prevProps.patient) {
      this.setState({
        sexP: this.props.patient.sex,
        birthdayP: this.props.patient.birthday,
        cardIdP: this.props.patient.cardId,
      })
      if (this.props.receptions.length) {
        this.setState({
          ...this.props.receptions.slice(-1)[0].state,
          rid: this.props.receptions.slice(-1)[0].id,
          text: this.props.receptions.slice(-1)[0].diagnosis.text,
          date: this.props.receptions.slice(-1)[0].date,
          lengthReceptions: this.props.receptions.length,
          currentView: this.props.receptions.length,
          drugs: this.props.receptions.slice(-1)[0].drugs,
          type: 'read',
        })
      }
    }
    if (this.props.reception !== prevProps.reception) {
      if(this.state.type === 'new') {
        this.setState({
          lengthReceptions: this.props.receptions.length,
          currentView: this.props.receptions.length,
          type: 'read',
          rid: this.props.reception.id,
          disabled: true
        })
      }
    }
    if(this.props.drugs !== prevProps.drugs) {
      this.setState({
        drugsSelect: this.props.drugs.results
      })
    }
  }

  handleOnClickNew = (e) => {
    const { lengthReceptions } = this.state;
    this.setState({
      type: 'new',
      disabled: false,
      lengthReceptions: lengthReceptions + 1,
      currentView: lengthReceptions + 1,
      date: '',
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
        text: this.props.receptions.slice(lengthReceptions - 2)[0].diagnosis.text,
        date: this.props.receptions.slice(lengthReceptions - 2)[0].date,
        drugs: this.props.receptions.slice(lengthReceptions - 2)[0].drugs,
        currentView: lengthReceptions - 1,
        lengthReceptions: lengthReceptions - 1,
        type: 'read',
      })
    } else {
      this.setState({
        years: 0,
        months: 0,
        lifeAnamnesis: '',
        diseaseAnamnesis: '',
        plaints: '',
        objectiveInspection: '',
        examinationsResults: '',
        specialistsConclusions: '',
        text: '',
        drugs: [],
        date: '',
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
    const { pid, type, text, date, rid, drugs } = this.state;
    delete state['lengthReceptions'];
    const diagnosis = { text };
    if (type === 'new') {
      this.props.addReception({id: pid, date: date, diagnosis: diagnosis, drugs: drugs, state: state})
    }
    if (type === 'edit') {
      this.props.updateReception({pid: pid, rid: rid, date: date, diagnosis: diagnosis, drugs: drugs, state: state});
      this.setState({
        disabled: true
      })
    }
  };

  handleOnChangeDate = (date) => {
    this.setState({
      date: date !== null && date !== '' ? dayjs(date).format() : ''
    })
  };

  handleOnClickPrev = () => {
    let { currentView } = this.state;
    const isEnabledPrev = currentView > 1;
    currentView = currentView - 1;
    if (isEnabledPrev) {
      this.setState({
        ...this.props.receptions.slice(currentView - 1)[0].state,
        rid: this.props.receptions.slice(currentView - 1)[0].id,
        date: this.props.receptions.slice(currentView - 1)[0].date,
        drugs: this.props.receptions.slice(currentView - 1)[0].drugs,
        text: this.props.receptions.slice(currentView - 1)[0].diagnosis.text,
        currentView,
        type: 'read',
        disabled: true,
      })
    }
  };

  handleOnClickNext = () => {
    let { currentView, lengthReceptions } = this.state;
    const isEnabledNext = currentView < lengthReceptions;
    currentView = currentView + 1;
    if (isEnabledNext) {
      this.setState({
        ...this.props.receptions.slice(currentView - 1)[0].state,
        rid: this.props.receptions.slice(currentView - 1)[0].id,
        date: this.props.receptions.slice(currentView - 1)[0].date,
        drugs: this.props.receptions.slice(currentView - 1)[0].drugs,
        text: this.props.receptions.slice(currentView - 1)[0].diagnosis.text,
        currentView,
        type: 'read',
        disabled: true,
      })
    }
  }

  handleOnChangeSelect = (value, obj) => {
    const drugs = [];
    obj.map((element, index) => {
       drugs.push({
         id: element.id,
         tradeName: element.value,
         manufacturer: element.manufacturer,
         releaseFormVSDosage: element.releaseformvsdosage
       })
       return null;
    })
    this.setState({
      drugs
    })
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
    const {
      disabled, lifeAnamnesis, diseaseAnamnesis, plaints, objectiveInspection,
      examinationsResults, specialistsConclusions, text, type, date, lengthReceptions,
      currentView, sex, years, months, sexP, birthdayP, cardIdP
    } = this.state;
    let { drugs, drugsSelect } = this.state;
    const { error } = this.props;

    let isDisabledGet = false;
    let isDisabledPost = false;
    if (Object.keys(this.props.role) !== '{}' && this.props.role.authorities !== undefined) {
      isDisabledGet = (this.props.role.authorities.includes('ROLE_ADMIN')
          || this.props.role.authorities.includes('ROLE_OPERATOR')
          || this.props.role.authorities.includes('ROLE_EXPERT'));
      isDisabledPost = (this.props.role.authorities.includes('ROLE_ADMIN')
          || this.props.role.authorities.includes('ROLE_OPERATOR'));
    }

    const defaultValue = drugs.map((el, i) => { return el.tradeName});
    drugsSelect = drugs.concat(drugsSelect);
    drugsSelect = drugsSelect.filter((element, index, self) =>
      index === self.findIndex((el) => (
        el.id === element.id && el.tradeName === element.tradeName
      ))
    )

    const isDisabledMenu = !lengthReceptions;
    const isEnabledPrev = currentView > 1;
    const isEnabledNext = currentView < lengthReceptions;

    const menu = (
      <Menu>
        <Menu.Item key="1" onClick={this.handleOnClickEdit} disabled={isDisabledMenu}>
          Редактировать осмотр
        </Menu.Item>
        <Menu.Item key="2" onClick={this.handleOnClickRemove} disabled={isDisabledMenu}>
          Удалить осмотр
        </Menu.Item>
      </Menu>
    );

    const Parametres1 = [
      {disabled: disabled, lel9: lifeAnamnesis, label: "Анамнез жизни",
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
        { isDisabledGet ?
          <Fragment>

            <div className="reception__top-data">
              <div className="reception__top-sex">
                <p>Пол:</p>
                <label>
                  {sexP === 'f' ? 'Женский' : this.sex === 'm' ? 'Мужской' : 'не указан'}
                </label>
              </div>
              <div className="reception__top-birthday">
                <p>Дата рождения:</p>
                <label>
                  {birthdayP ? dayjs(birthdayP).format('DD/MM/YYYY') : 'не указана'}
                </label>
              </div>
              <div className="reception__top-pid">
                <p>Идентификатор:</p>
                <label>
                  {cardIdP}
                </label>
              </div>
            </div>

            <div className="reception__header">
              <div className="reception__header-center">
                {type === 'new' ?
                    <DatePicker showTime onChange={this.handleOnChangeDate} format={'DD/MM/YYYY HH:mm'} />
                  :
                  lengthReceptions ?
                    <Fragment>
                      {disabled &&
                      <i className="fa fa-angle-double-left fa-1x" aria-hidden="true"
                         onClick={this.handleOnClickPrev}
                         style={isEnabledPrev ? {cursor: 'pointer', color: 'black'} : {
                           cursor: 'default',
                           color: '#bdbcbc'
                         }}
                      />
                      }
                      <label className="reception__date">{date === null || date === '' ? 'Дата не указана' : dayjs(date).format('DD/MM/YYYY HH:mm')}</label>
                      {disabled &&
                      <i className="fa fa-angle-double-right fa-1x" aria-hidden="true"
                        onClick={this.handleOnClickNext}
                        style={isEnabledNext ? {cursor: 'pointer', color: 'black'} : {cursor: 'default', color: '#bdbcbc'}}
                        />
                      }
                    </Fragment>
                  :
                  null
                }
              </div>
              <div className="reception__header-rightSide">
                {isDisabledPost &&
                    <Fragment>
                {type === 'read' ?
                  <Dropdown.Button onClick={this.handleOnClickNew} overlay={menu}>
                  Добавить осмотр
                  </Dropdown.Button>
                  :
                  <Button onClick={this.handleOnSubmit}>Сохранить</Button>
                }
                    </Fragment>
                }
              </div>
            </div>
            {lengthReceptions || type === 'new' ?
              <div className="reception__content">
                {error && error.data.error === "invalid_reception_date" &&
                  <div className="reception__invalidDate">{error.data.error_description}</div>
                }
                <Tabs defaultActiveKey={'2'} onChange={null}>
                  <TabPane tab="Анамнез" key="1">
                    <div className="reception__content-sex">
                      <p>Пол</p>
                      <label>
                        {sex === 'f' ? 'Женский' : this.sex === 'm' ? 'Мужской' : 'не указан'}
                      </label>
                    </div>

                    <div className="reception__content-brth">
                      <label>Возраcт</label>
                      {years !== null && months !== null ?
                          <Fragment>
                            <p className="reception__years">
                              {years}
                              <label>лет</label>
                            </p>
                            <p className="reception__month">
                              {months}
                              <label>мес</label>
                            </p>
                          </Fragment>
                          : <p className="reception__no-age">не указан</p>
                      }
                    </div>
                    <Elements obj={Parametres1} classN="reception"/>
                  </TabPane>
                  <TabPane tab="Текущее состояние" key="2">
                    <Elements obj={Parametres2} classN="reception"/>
                  </TabPane>
                  <TabPane tab="Диагноз и назначения" key="3">
                    <Elements obj={Parametres3} classN="reception"/>
                    { type !== 'read' ?
                        <div className="reception__content-drugs-read">
                          <span className="reception__content-drugsTittle">Назначения</span>
                      <Select
                        mode="multiple"
                        style={{ width: '100%' }}
                        onChange={this.handleOnChangeSelect}
                        onSearch={this.handleOnSearch}
                        defaultValue={defaultValue}
                      >
                      {drugsSelect.map((item, index) => (
                          <Option key={item.tradeName} id={item.id}
                            manufacturer = {item.manufacturer}
                            releaseformvsdosage = {item.releaseFormVSDosage}
                            >
                            <p>{item.tradeName}</p>
                            <p>{item.releaseFormVSDosage}</p>
                            <p>{item.manufacturer}</p>
                          </Option>
                      ))}
                      </Select>
                        </div>

                      :

                      <div className="reception__content-drugs">
                        <span className="reception__content-drugsTittle">Назначения</span>
                        <List
                          itemLayout="horizontal"
                          dataSource={drugs}
                          renderItem={(item, index) => (
                            <List.Item key={index}>
                              <List.Item.Meta key={index}
                                title={[
                                  <Link key={index} to={`/all-drugs/instruction/${item.id}`}>
                                    {item.tradeName}
                                  </Link>
                                ]}
                                description={[
                                  <div className="allDrugs__description" key={index}>
                                    <div className="allDrugs__description-1">
                                      <label className="allDrugs__description-tittle">
                                        {drugEnglToRus["releaseFormVSDosage"]}
                                      </label>
                                      <span className="allDrugs__description-text">
                                        {item.releaseFormVSDosage}
                                      </span>
                                    </div>
                                    <div className="allDrugs__description-2">
                                      <label className="allDrugs__description-tittle">
                                        {drugEnglToRus["manufacturer"]}
                                      </label>
                                      <span className="allDrugs__description-text">
                                        {item.manufacturer}
                                      </span>
                                    </div>
                                  </div>
                                ]}
                              />
                            </List.Item>
                          )}
                        />
                      </div>
                    }
                  </TabPane>
                </Tabs>
              </div>
              :
              <div className="noData reception__noData">Нет осмотров</div>
            }
          </Fragment>
          :
          null
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
  role: state.sessionReducer.user,
  error: state.patients.error
}), { getPatientById, addReception, updateReception, deleteReception, getDrugs, changePath})(Reception);

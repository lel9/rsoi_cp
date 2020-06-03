import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Tabs, List } from 'antd';
import { Link } from "react-router-dom";
import dayjs from 'dayjs'

import { getReception } from '../actions/actionReception';
import { getDrugs } from '../actions/actionDrug';
import { changePath } from '../actions/actionPath.js';

import { drugEnglToRus } from '../constants';

import getHistory from '../modules/history';
import InputField from '../components/inputField';

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


class PublicReceptions extends Component {
  state = {
    sex: '',
    years: null,
    months: null,
    lifeAnamnesis: '',
    diseaseAnamnesis: '',
    plaints: '',
    objectiveInspection: '',
    examinationsResults: '',
    specialistsConclusions: '',
    text: '', //// diagnosis
    drugs: [],
    date: '',
    pid: '',
    rid: '',
    lengthReceptions: 0,
    currentView: 0,
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    // this.props.setPath(getHistory().location.pathname);
    const pid = this.props.match.params.id;
    this.props.getReception(pid)
    this.setState({
      pid
    })
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.receptions !== prevProps.receptions) {
      const reception = this.props.receptions[0];
      this.setState({
        rid: reception.id,
        date: reception.date,
        ...reception.state,
        text: reception.diagnosis.text,
        drugs: reception.drugs,
        currentView: 0,
        lengthReceptions: this.props.receptions.length
      })
    }
  }

  handleOnClickPrev = () => {
    let { currentView } = this.state;
    const { receptions } = this.props;
    const isEnabledPrev = currentView > 0;
    currentView = currentView - 1;
    if (isEnabledPrev) {
      this.setState({
        ...receptions[currentView].state,
        rid: receptions[currentView].id,
        date: receptions[currentView].date,
        drugs: receptions[currentView].drugs,
        text: receptions[currentView].diagnosis.text,
        currentView,
      })
    }
  };

  handleOnClickNext = () => {
    let { currentView, lengthReceptions } = this.state;
    const { receptions } = this.props;
    const isEnabledNext = currentView < lengthReceptions - 1;
    currentView = currentView + 1;
    if (isEnabledNext) {
      this.setState({
        ...receptions[currentView].state,
        rid: receptions[currentView].id,
        date: receptions[currentView].date,
        drugs: receptions[currentView].drugs,
        text: receptions[currentView].diagnosis.text,
        currentView,
      })
    }
  }

  render() {
    const {
      lifeAnamnesis, diseaseAnamnesis, plaints, objectiveInspection,
      examinationsResults, specialistsConclusions, text, date, lengthReceptions,
      currentView, sex, years, months
    } = this.state;

    let { drugs } = this.state;

    const Parametres1 = [
      {disabled: true, lel9: lifeAnamnesis, label: "Анамнез жизни",
        actionOnChange: this.handleOnChangeLifeAnamnesis},
      {disabled: true, lel9: diseaseAnamnesis, label: "Анамнез заболевания",
        actionOnChange: this.handleOnChangeDiseaseAnamnesis}
       ];

    const Parametres2 = [
      {disabled: true, lel9: plaints, label: "Жалобы",
        actionOnChange: this.handleOnChangePlaints},
      {disabled: true, lel9: objectiveInspection, label: "Объективный осмотр",
        actionOnChange: this.handleOnChangeObjectiveInspection},
      {disabled: true, lel9: examinationsResults, label: "Результаты исследований",
        actionOnChange: this.handleOnChangeExaminationsResults},
      {disabled: true, lel9: specialistsConclusions, label: "Заключения специалистов",
        actionOnChange: this.handleOnChangeSpecialistsConclusions},
       ];

    const Parametres3 = [
      {disabled: true, lel9: text, label: "Диагноз",
        actionOnChange: this.handleOnChangeDiagnosis}
       ];

   const isEnabledPrev = currentView > 1;
   const isEnabledNext = currentView < lengthReceptions - 1;

    return (
      <div className="reception">
        <div className="reception__header">
          <div className="reception__header-center">
            <i className="fa fa-angle-double-left fa-1x" aria-hidden="true"
               onClick={this.handleOnClickPrev}
               style={isEnabledPrev ? {cursor: 'pointer', color: 'black'} : {
                 cursor: 'default',
                 color: '#bdbcbc'
               }}
            />
            <label className="reception__date">{date === null || date === '' ? 'Дата не указана' : dayjs(date).format('DD/MM/YYYY HH:mm')}</label>
            <i className="fa fa-angle-double-right fa-1x" aria-hidden="true"
              onClick={this.handleOnClickNext}
              style={isEnabledNext ? {cursor: 'pointer', color: 'black'} : {cursor: 'default', color: '#bdbcbc'}}
              />
          </div>
        </div>
        <div className="reception__content">
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
            </TabPane>
          </Tabs>
        </div>
      </div>
    )
  }
}

export default connect (state => ({
  receptions: state.patients.receptions
}), { getReception, getDrugs, changePath })(PublicReceptions)

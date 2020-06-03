import React, { Component, Fragment } from 'react';
import { Table } from 'antd';
import { connect } from 'react-redux';
import { Select, DatePicker, Button } from 'antd';
import { getStatistics } from '../actions/actionStatistics';
import { changePath } from '../actions/actionPath.js';
import getHistory from '../modules/history';
import dayjs from 'dayjs';

const { Option } = Select;
const { RangePicker } = DatePicker;

const columns1 = [
  {
    title: 'Препарат (торг. наименование, форма выпуска/дозировка, производитель)',
    colName: 'drug',
    dataIndex: 'data',
    key: 'data',
    fields: ['tradeName', 'releaseFormVSDosage', 'manufacturer'],
    render: (data = []) => (
        <Fragment>
          {data.map((elem, index) => {
              return (
                    <p key={index} className="statistics__list-drug-info">{elem}</p>
              )
            })
          }
        </Fragment>
    )
  },
  {
    title: 'Просмотрено',
    dataIndex: 'Watched',
    key: 'Watched',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.Watched - b.Watched,
  },
  {
    title: 'Добавлено рекомендаций',
    dataIndex: 'childCreateCount',
    key: 'childCreateCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childCreateCount - b.childCreateCount,
  },
  {
    title: 'Обновлено рекомендаций',
    dataIndex: 'childUpdateCount',
    key: 'childUpdateCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childUpdateCount - b.childUpdateCount,
  },
  {
    title: 'Удалено рекомендаций',
    dataIndex: 'childDeleteCount',
    key: 'childDeleteCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childDeleteCount - b.childDeleteCount,
  },
]
const columns2 = [
  {
    title: 'Пациент (идентификатор, дата рождения, пол)',
    colName: 'patient',
    dataIndex: 'data',
    key: 'data',
    fields: ['cardId', 'birthday', 'sex'],
    render: (data) => (
        <Fragment>
          {
            data.map((elem, index) => {
              return (
                    <p key={index} className="statistics__list-drug-info">{elem}</p>
              )
            })
          }
        </Fragment>
    )
  },
  {
    title: 'Просмотрено',
    dataIndex: 'Watched',
    key: 'Watched',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.Watched - b.Watched,
  },
  {
    title: 'Добавлено осмотров',
    dataIndex: 'childCreateCount',
    key: 'childCreateCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childCreateCount - b.childCreateCount,
  },
  {
    title: 'Обновлено осмотров',
    dataIndex: 'childUpdateCount',
    key: 'childUpdateCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childUpdateCount - b.childUpdateCount,
  },
  {
    title: 'Удалено осмотров',
    dataIndex: 'childDeleteCount',
    key: 'childDeleteCount',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.childDeleteCount - b.childDeleteCount,
  },
]
const columns3 = [
  {
    title: 'Пользователи',
    colName: 'user',
    dataIndex: 'data',
    key: 'data',
    fields: ['id'],
    render: (data) => (
        <>
          {
            data.map((elem, index) => {
              return (
                  <p key={index} className="statistics__list-user-info">{elem}</p>
              )
            })
          }
        </>
    )
  },
  {
    title: 'Просмотрено',
    dataIndex: 'Watched',
    key: 'Watched',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.Watched - b.Watched,
  },
]

const columns = {
  drug: columns1,
  patient: columns2,
  user: columns3
}


const translate = {
  'user': 'пользователями',
  'drug': 'препаратами и рекомендациями',
  'patient': 'пациентами и осмотрами',
}

class Statistics extends Component {
  state = {
    dateStart: 0,
    dateEnd: 0,
    entity: 'drug',
    statistics: [],
    childTotalUpdateCount: 0,
    childTotalDeleteCount: 0,
    childTotalCreateCount: 0,
    totalUpdateCount: 0,
    totalReadCount: 0,
    totalDeleteCount: 0,
    totalCreateCount: 0,
    errMessage: null,
    entitiesStatistic: [],
    fill: [],
    isDisabled: true,
  }

  componentDidMount = () => {
    // this.props.setPath(getHistory().location.pathname
    this.props.changePath(getHistory().location.pathname);
  }

  componentDidUpdate = (prevProps) => {
    const { entity } = this.props;
    let fill = [];
    if (prevProps.statistics !== this.props.statistics) {
      let results;
      if (entity !== 'user') {
        results = this.props.statistics.results;
        this.setState({
          ...this.props.ids.data,
          statistics: this.props.statistics.results,
        })
      } else {
        results = this.props.statistics.data;
        this.setState({
          ...this.props.statistics.data,
        })
      }
      fill = this.fillData(this.props.ids.data.entitiesStatistic,
        results, columns[entity]);
      this.setState({
        fill
      })
    }

    if (this.props.role !== prevProps.role) {
      let isDisabled = false;
      if(Object.keys(this.props.role).length) {
        isDisabled = this.props.role.authorities.includes('ROLE_ADMIN');
      }
      this.setState({
        isDisabled
      })
    }
  }

  shouldComponentUpdate = (nextProps, nextState) => {
    if (nextProps.statistics !== this.props.statistics) {
      return true;
    }
    if (this.props.role !== nextProps.role) {
      return true;
    }
    if (this.props.entity !== nextProps.entity) {
      return true;
    }
    if (this.props.dateStart !== nextProps.dateStart) {
      return true;
    }
    if (this.props.dateEnd !== nextProps.dateEnd) {
      return true;
    }
    if (this.props.statistics !== nextProps.statistics) {
      return true;
    }
    if (this.props.dateStart !== nextProps.dateStart) {
      return true;
    }
    if (this.props.dateEnd !== nextProps.dateEnd) {
      return true;
    }
    if (this.state !== nextState) {
      return true
    }
    return false;
  }

  fillData = (entitiesStatistic, statistics, columns) => {
    let rows = [];
    if (columns[0].colName === "user") {
      statistics = statistics.entitiesStatistic;
    }
    entitiesStatistic.map(element => {
      const index = statistics.map(val => (val.id)).indexOf(element['id']);
      const data = columns[0].fields.map(element => {
        const statisticsElement = Object(statistics[index])[element];
        if (element === "birthday")
          return statisticsElement !== null && statisticsElement !== '' ? dayjs(statisticsElement).format('DD/MM/YYYY') : 'дата рождения не указана';
        else if (element === "sex")
          return statisticsElement === 'f' ? 'Женский' : statisticsElement === 'm' ? 'Мужской': 'пол не указан';
        return statisticsElement;
      });
      const row = {
        'key': index,
        'data': data,
        'Watched': element.readCount,
      };
      if (columns[0].colName === "patient" || columns[0].colName === "drug") {
        row[columns[2].key] = element.childCreateCount;
        row[columns[3].key] = element.childUpdateCount;
        row[columns[4].key] = element.childDeleteCount;
      }
      rows = rows.concat(row)
      return null;
    })
    return rows;
  }

  handleOnChangeRangePicker = (value, dateString) => {
    if (dateString[0] !== '' && dateString[1] !== '') {
      const dateStart = new Date(value[0]).getTime();
      const dateEnd = new Date(value[1]).getTime();
      this.setState({
        dateStart,
        dateEnd
      })
    }
  };

  handleOnClickShow = () => {
    const { dateStart, dateEnd, entity } = this.state;
    if( dateStart !== 0 && dateEnd !== 0 && entity !== '') {
      this.props.getStatistics({dateStart, dateEnd, entity});
    }
  }

  handleOnChangeSelect = (value) => {
    this.setState({
      entity: value
    })
  }

  render() {
    const { fill, totalCreateCount, isDisabled } = this.state;
    const { entity } = this.props;
    const { ids, dateStart, dateEnd } = this.props;
    return (
      <div className="statistics">
        {isDisabled ?
          <Fragment>
            <div className="statistics__header">
              <div className="statistics__header-rangePicker">
                <label>Интервал времени</label>
                <RangePicker showTime onChange={this.handleOnChangeRangePicker}/>
              </div>
              <div className="statistics__header-select">
                <label>Сущность</label>
                <Select defaultValue="drug" style={{ width: 300 }} onChange={this.handleOnChangeSelect}>
                  <Option value="drug">Препараты и рекомендации</Option>
                  <Option value="patient">Пациенты и осмотры</Option>
                  <Option value="user">Пользователи</Option>
                </Select>
              </div>
            </div>
            <div className="statistics__footer">
              <Button onClick={this.handleOnClickShow}>Просмотреть</Button>
            </div>
            <div className="statistics__content">
                { entity !== '' ? ids.data.entitiesStatistic.length ?
                    <Fragment>
                      <div className="statistics__total-info">
                        <p>Добавлено новых: </p>
                        <label>{totalCreateCount}</label>
                      </div>
                      <div className="statistics__content-table">
                      <Table dataSource={fill} columns={columns[entity]} size="middle"/>
                      </div>
                    </Fragment>
                  :
                    <div className="statistics__content-empty">
                      Нет операций над <span>{` ${translate[entity]} `}</span>
                      в интервале <br/> с
                      {' ' + dayjs.unix(dateStart/1000).format('DD/MM/YYYY HH:mm:ss')}
                      <br/> по
                      {' ' + dayjs.unix(dateEnd/1000).format('DD/MM/YYYY HH:mm:ss')}
                    </div>
                  :
                    null
                }
            </div>
          </Fragment>
          :
          null
        }
      </div>
    )
  }
}

export default connect((state => ({
  statistics: state.statistics.statistics,
  ids: state.statistics.ids,
  entity: state.statistics.entity,
  dateStart: state.statistics.dateStart,
  dateEnd: state.statistics.dateEnd,
  role: state.sessionReducer.user,
})), { changePath, getStatistics })(Statistics);

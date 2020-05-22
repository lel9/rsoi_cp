import React, { Component, Fragment } from 'react';
import { Table, Tag, Space } from 'antd';
import { connect } from 'react-redux';
import { Select, TimePicker, DatePicker, Tabs, Menu, Dropdown, Button } from 'antd';
import { getStatistics } from '../actions/actionStatistics';
import getHistory from '../modules/history';
import dayjs from 'dayjs';

const { Option } = Select;
const { RangePicker } = DatePicker;

const columns1 = [
  {
    title: 'Препарат (наименование, форма выпуска/дозировка, производитель)',
    dataIndex: 'tradeName',
    key: 'tradeName',
  },
  {
    title: 'Просмотрено',
    dataIndex: 'Watched',
    key: 'Watched',
  },
  {
    title: 'Добавлено',
    dataIndex: 'Added',
    key: 'Added',
  },
  {
    title: 'Обновлено',
    dataIndex: 'Updated',
    key: 'Updated',
  },
  {
    title: 'Добавлено рекомендаций',
    dataIndex: 'childCreateCount',
    key: 'childCreateCount',
  },
  {
    title: 'Обновлено рекомендаций',
    dataIndex: 'childUpdateCount',
    key: 'childUpdateCount',
  },
  {
    title: 'Удалено рекомендаций',
    dataIndex: 'childDeleteCount',
    key: 'childDeleteCount',
  },
]
const columns2 = [
  {
    title: 'Пациент (№ карты/ИБ, пол, дата рождения)',
    dataIndex: 'cardId',
    key: 'cardId',
  },
  {
    title: 'Просмотрено',
    dataIndex: 'Watched',
    key: 'Watched',
  },
  {
    title: 'Добавлено',
    dataIndex: 'Added',
    key: 'Added',
  },
  {
    title: 'Обновлено',
    dataIndex: 'Updated',
    key: 'Updated',
  },
  {
    title: 'Добавлено осмотров',
    dataIndex: 'childCreateCount',
    key: 'childCreateCount',
  },
  {
    title: 'Обновлено осмотров',
    dataIndex: 'childUpdateCount',
    key: 'childUpdateCount',
  },
  {
    title: 'Удалено осмотров',
    dataIndex: 'childDeleteCount',
    key: 'childDeleteCount',
  },
]
const columns = {
  drug: columns1,
  patient: columns2,
  user: [{
      title: 'Пользователи',
      dataIndex: 'cardId',
      key: 'cardId',
    }]
}

const translate = {
  'user': 'пользователей',
  'drug': 'лекарств',
  'patient': 'пациентов',
}

class Statistics extends Component {
  state = {
    dateStart: 0,
    dateEnd: 0,
    entity: '',
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
  }

  componentDidMount = () => {
    this.props.setPath(getHistory().location.pathname);
  }

  componentDidUpdate = (prevProps) => {
    const { entity } = this.props;
    if (prevProps.statistics !== this.props.statistics) {
      if (entity !== 'user') {
        this.setState({
          ...this.props.ids.data,
          statistics: this.props.statistics.results,
        })
      } else {
        this.setState({
          ...this.props.statistics.data,
        })
      }
      const fill = this.fillData(this.props.ids.data.entitiesStatistic,
        this.props.statistics.results, columns[entity]);
      console.log(this.state);
      this.setState({
        fill
      })
    }
  }

  fillData = (entitiesStatistic, statistics, columns) => {
    let rows = [];
    entitiesStatistic.map((element, index) => {
       const row = {
        'key': index,
        [columns[0].key]: Object(statistics[index])[columns[0].key],
        'Watched': element.readCount,
        'Added': element.createCount,
        'Updated': element.updateCount,
        'Removed': element.deleteCount,
      };
      if (columns.length > 1) {
        row[columns[4].key] = element.childCreateCount;
        row[columns[5].key] = element.childUpdateCount;
        row[columns[6].key] = element.childDeleteCount;
      }
      rows = rows.concat(row)
    })
    return rows;
  }

  handleOnChangeRangePicker = (value, dateString) => {
    if (dateString[0] !== '' && dateString[1] !== '') {
      const dateStart = new Date(value[0]).getTime();
      const dateEnd = new Date(value[1]).getTime()
      this.setState({
        dateStart,
        dateEnd
      })
    }
  }

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
    const { fill } = this.state;
    const { entity } = this.props;
    const { ids, dateStart, dateEnd } = this.props;
    console.log(dateStart, dateEnd);
    return (
      <div className="statistics">
        <div className="statistics__header">
          <div className="statistics__header-rangePicker">
            <label>Интервал времени</label>
            <RangePicker showTime onChange={this.handleOnChangeRangePicker}/>
          </div>
          <div className="statistics__header-select">
            <label>Тип</label>
            <Select defaultValue="" style={{ width: 300 }} onChange={this.handleOnChangeSelect}>
              <Option value="drug">Препараты и рекомендации</Option>
              <Option value="patient">Пациенты и осмотры</Option>
              <Option value="user">Пользователи</Option>
            </Select>
          </div>
        </div>
        <div className="statistics__content">
          <Button  type="primary" onClick={this.handleOnClickShow}>Показать</Button>
            { entity !== '' ? ids.data.entitiesStatistic.length ?
                <Fragment>
                  <div className="statistics__content-table">
                  <Table dataSource={fill} columns={columns[entity]} />
                  </div>
                  <div className="statistics__content-conclusion">
                  </div>
                </Fragment>
              :
                <div className="statistics__content-empty">
                  Данные отсутствуют для <span>{` ${translate[entity]} `}</span>
                  интервала <br/> от
                  {' ' + dayjs.unix(dateStart/1000).format('DD/MM/YYYY HH:mm:ss')}
                  <br/> по
                  {' ' + dayjs.unix(dateEnd/1000).format('DD/MM/YYYY HH:mm:ss')}
                </div>
              :
                null
            }
        </div>

      </div>
    )
  }
}
// {dayjs.unix((date + time)/1000).format('HH:mm:ss')}


export default connect((state => ({
  statistics: state.statistics.statistics,
  ids: state.statistics.ids,
  entity: state.statistics.entity,
  dateStart: state.statistics.dateStart,
  dateEnd: state.statistics.dateEnd,
})), { getStatistics })(Statistics);

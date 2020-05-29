import React, { Component, Fragment } from 'react'
import { Input } from 'antd';
import { Link } from "react-router-dom";
import { List, Button, Pagination } from 'antd';
import getHistory from '../modules/history';
import { connect } from 'react-redux';
import { getPatients } from '../actions/actionPatient.js';
import { changePath } from '../actions/actionPath.js';

import dayjs from 'dayjs';

const { Search } = Input;

class AllPatients extends Component {
  state = {
    currentView: 1,
    totalPages: 0,
    totalElements: 0,
    patients: [],
    data: []
  }

  handleOnClickPagination = (currentView, pageSize) => {
    this.props.getPatients({cardId: '', page: currentView - 1, size: 7});
    this.setState({
      currentView: currentView,
    })
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.patients !== prevProps.patients) {
      if (this.state.data.length === 0) {
        this.setState({
          patients: this.props.patients.results,
        })
      }
      this.setState({
        totalPages: this.props.patients.totalPages,
        totalElements: this.props.patients.totalElements,
        data: this.props.patients.results,
      })
    }
  }

  componentDidMount = () => {
    this.props.getPatients({cardId: '', page: 0, size: 7});
    this.props.changePath(getHistory().location.pathname);
  }

  handleOnClickAdd = () => {
    getHistory().push('/all-patients/add-patient');
  }

  handleOnSearch = (value) => {
    const { data, patients } = this.state;
    if (patients.length === 0) {
      this.setState({
        patients: data
      })
    }
    this.props.getPatients({cardId: value, page: 0, size: 7});
  }

  handleOnChange = ({target}) => {
    const { patients } = this.state;
    if (target.value === '') {
      this.setState({
        data: patients
      })
    }
  }

  render() {
    const { currentView, totalElements, data, error } = this.state;
    let isDisabledGet = false, isDisabledPost = false;
    if (Object.keys(this.props.role) !== '{}' && this.props.role.authorities !== undefined) {
      isDisabledGet = (this.props.role.authorities.includes('ROLE_ADMIN')
      || this.props.role.authorities.includes('ROLE_OPERATOR')
      || this.props.role.authorities.includes('ROLE_EXPERT'));
      isDisabledPost = (this.props.role.authorities.includes('ROLE_ADMIN')
          || this.props.role.authorities.includes('ROLE_OPERATOR'));
    }
    return (
      <div className="allPatients">
        { isDisabledGet ?
          error === 'Network Error' ?
          <div className="Network-Error allPatients__Network-Error">
            Сервис пациентов временно недоступен
          </div>
          :
          <Fragment>
            <div className="allPatients__leftSide">
              <Search
                placeholder="Введите идентификатор"
                onSearch={this.handleOnSearch}
                onChange={this.handleOnChange}
                style={{ width: 450 }}
              />
              <List
                itemLayout="horizontal"
                dataSource={data}
                renderItem={(item, index) => (
                  <List.Item key={index}>
                    <List.Item.Meta key={index}
                      title={[
                        <Link key={index} to={`/all-patients/reception/${item.id}`}>
                          {item.cardId}
                        </Link>
                      ]}
                      description={
                        <Fragment>
                          {item.birthday !== null && item.birthday !== '' ?
                              <div>Дата рождения: {dayjs(item.birthday).format('DD/MM/YYYY')}</div>
                              : <div>Дата рождения: не указана</div>
                          }
                          <div>Пол: {item.sex === 'f' ? 'Женский' : this.sex === 'm' ? 'Мужской' : 'не указан'}</div>
                        </Fragment>
                      }
                    />
                  </List.Item>
                )}
              />
            <Pagination defaultPageSize={7} pageSize={7} defaultCurrent={1}
                total={totalElements} onChange={this.handleOnClickPagination}
                current={currentView}
              />
            </div>
            <div className="allPatients__rightSide">
              {isDisabledPost &&
                <Button onClick={this.handleOnClickAdd}>Добавить</Button>
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

export default connect(state => ({
  patients: state.patients.patients,
  role: state.sessionReducer.user
}),{ getPatients, changePath })(AllPatients);

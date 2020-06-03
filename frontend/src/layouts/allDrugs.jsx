import React, { Component, Fragment } from 'react'
import { Input } from 'antd';
import { Link } from "react-router-dom";
import { List, Button, Pagination } from 'antd';

import getHistory from '../modules/history';
import { connect } from 'react-redux';
import { getDrugs } from '../actions/actionDrug.js';
import { changePath } from '../actions/actionPath.js';
import { drugEnglToRus } from '../constants';
// sessionService.loadSession().then(session => console.log(session));
// sessionService.loadUser().then(user => console.log(user));
const { Search } = Input;

class AllDrugs extends Component {
  state = {
    currentView: 1,
    totalPages: 0,
    totalElements: 0,
    drugs: [],
    data: [],
    errorS: '',
    role: ['USER'],
  }

  handleOnClickPagination = (currentView, pageSize) => {
    this.props.getDrugs({tradeName: '', page: currentView - 1, size: 7});
    this.setState({
      currentView: currentView,
    })
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    // this.props.setPath(getHistory().location.pathname);
    this.props.getDrugs({tradeName: '', page: 0, size: 7});
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.drugs !== prevProps.drugs) {
      if (this.state.data.length === 0) {
        this.setState({
          drugs: this.props.drugs.results,
        })
      }
      this.setState({
        totalPages: this.props.drugs.totalPages,
        totalElements: this.props.drugs.totalElements,
        data: this.props.drugs.results,
      })
    }
    if (this.props.errorS !== prevProps.errorS) {
      if (this.props.errorS) {
        this.setState({
          errorS: this.props.errorS.err
        })
      }
    }
    if (this.props.error !== prevProps.error) {
      if (this.props.error) {
        this.setState({
          error: this.props.error.data
        })
      }
    }
  }

  handleOnClickAdd = () => {
    getHistory().push('/all-drugs/add-drug')
  }

  handleOnClickAbout = () => {
    getHistory().push('/select-drug/recomendation')
  }

  handleOnSearch = (value) => {
    const { data, drugs } = this.state;
    if (drugs.length === 0) {
      this.setState({
        drugs: data
      })
    }
    this.props.getDrugs({tradeName: value, page: 0, size: 7});
  }


  handleOnChange = ({target}) => {
    const { drugs } = this.state;
    if (target.value === '') {
      this.setState({
        data: drugs
      })
    }
  }

  render() {
    const { currentView, totalElements, data, errorS } = this.state;
    const { authenticated } = this.props;
    let isDisabled = false;
    if (Object.keys(this.props.role) !== '{}' && this.props.role.authorities !== undefined) {
      isDisabled = (this.props.role.authorities.includes('ROLE_ADMIN')
          || this.props.role.authorities.includes('ROLE_OPERATOR'));
    }
    return (
      <div className="allDrugs">
        { errorS === 'Network Error' ?
          <div className="Network-Error allDrugs__Network-Error">
            Сервис препаратов временно недоступен
          </div>
          :
          <Fragment>
            <div className="allDrugs__leftSide">
              <Search
                placeholder="Введите торговое наименование"
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
                        <Link key={index} to={`/all-drugs/instruction/${item.id}`}>
                          {item.tradeName}
                        </Link>
                      ]}
                      description={[
                        <div className="allDrugs__description" key={index}>
                          <div className="allDrugs__description-1">
                            <label className="allDrugs__description-tittle">
                              {drugEnglToRus["releaseFormVSDosage"]}:
                            </label>
                            <span className="allDrugs__description-text">
                              {item.releaseFormVSDosage}
                            </span>
                          </div>
                          <div className="allDrugs__description-2">
                            <label className="allDrugs__description-tittle">
                              {drugEnglToRus["manufacturer"]}:
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
              <Pagination defaultPageSize={7} pageSize={7} defaultCurrent={1}
                  total={totalElements} onChange={this.handleOnClickPagination}
                  current={currentView}
              />
            </div>
            {authenticated &&
              <div className="allDrugs__rightSide">
                {isDisabled ?
                  <Button
                    onClick={this.handleOnClickAdd}
                  >
                    Добавить
                  </Button>
                  :
                  <div className="noPermition allDrugs__noPermition">
                  </div>
                }
              </div>
            }

          </Fragment>
        }
      </div>
    )
  }
}

export default connect(state => ({
  drugs: state.drugs.drugs,
  errorS: state.sessions.error,
  role: state.sessionReducer.user,
  authenticated: state.sessionReducer.authenticated,
  error: state.drugs.error,
}),{getDrugs, changePath})(AllDrugs);

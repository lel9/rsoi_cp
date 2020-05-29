import React, {Component, Fragment} from 'react';
import { TimePicker, DatePicker, List, Button, Pagination } from 'antd';
import { Select, Tabs, Menu, Dropdown } from 'antd';
import getHistory from '../modules/history';
import { changePath } from '../actions/actionPath.js';
import { drugEnglToRus } from '../constants';
import moment from 'moment';
import { connect } from 'react-redux';
const dateFormat = 'DD/MM/YYYY';

class Drugs extends Component {
  state = {
    show: false,
    drugs: [],
    data: []
  }

  componentDidMount = () => {
    const { drugs } = this.props;
    this.setState({
      drugs,
      // data: drugs
    })
  }

  render() {
    const { drugs, show, index } = this.state;
    const data = drugs.length ? show ? drugs : [drugs[0]] : [];
    return (
      <Fragment>
        <div className="recomendation__drug-list" data-index={index}>
          <List
            itemLayout="horizontal"
            dataSource={data}
            renderItem={(item, index) => (
              <List.Item key={index} data-index={index}>
                <List.Item.Meta key={index}
                  title={[
                    <label data-index={index} key={index}>{item.tradeName}</label>
                  ]}
                  description={[
                    <div data-index={index} className="recommendation__description" key={index}>
                      <div data-index={index} className="recommendation__description-1">
                        <label data-index={index} className="recommendation__description-tittle">
                          {drugEnglToRus["releaseFormVSDosage"]}:
                        </label>
                        <span data-index={index} className="recommendation__description-text">
                          {item.releaseFormVSDosage}
                        </span>
                      </div>
                      <div data-index={index} className="recommendation__description-2">
                        <label data-index={index} className="recommendation__description-tittle">
                          {drugEnglToRus["manufacturer"]}:
                        </label>
                        <span data-index={index} className="recommendation__description-text">
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
        <div className="recomendation__drug-showAll" data-index={index}
          onClick={() => this.setState({show: !this.state.show})}
          >
          {show ?
            "Скрыть препараты"
            :
            "Показать полгостью"
          }
        </div>
      </Fragment>
    )
  }
}

class Outcomes extends Component {
  state = {
    current: 0,
    date: '',
    examinationsResults: '',
    objectiveInspection: '',
    plaints: '',
    specialistsConclusions: '',
    noData: true,
    length: 0,
    currentView: 0,
  }

  componentDidMount = () => {
    const { outcomes } = this.props;
    if (outcomes.length) {
      this.setState({
        noData: false,
        date: outcomes[0].date,
        ...outcomes[0].state,
        length: outcomes.length
      })
    }
  }

  handleOnClickPrev = () => {
    // this.setState({
    //
    // )}
  }
  handleOnClickNext = () => {
    // this.setState({
    //
    // )}
  }


  render () {
    console.log(this.state);
    const { noData } = this.state;
    // isEnabledPaginationPrev
    // isEnabledPaginationNext
    return (
      <div className="recomendation__reception">
        {noData ?
          <div className="recomendation__reception-noData"></div>
          :
          <Fragment>
            <div className="recomendation__reception-header">
                <i className="fa fa-angle-double-left fa-1x" aria-hidden="true"
                   onClick={this.handleOnClickPrev}
                   style={ true ? {cursor: 'pointer', color: 'black'} :
                   {cursor: 'default',color: '#bdbcbc'}}
                />
                <div className="recomendation__reception-datePicker">
                  <DatePicker showTime onChange={this.handleOnChangeDate} format={'DD/MM/YYYY HH:mm'} />
                </div>
                <i className="fa fa-angle-double-right fa-1x" aria-hidden="true"
                  onClick={this.handleOnClickNext}
                  style={ true ? {cursor: 'pointer', color: 'black'} :
                  {cursor: 'default', color: '#bdbcbc'}}
                />
            </div>
            <div className="recomendation__reception-body">
            </div>
          </Fragment>
        }
      </div>
    )
  }
}

class Recommendation extends Component {
  state = {
    pid: '',
    results: [],
    currentDrug: -1
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    //this.props.setPath(getHistory().location.pathname);
    if (!this.props.recomendation) {
      getHistory().push(`/select-drug`)
    } else {
      if (this.props.recomendation.results.length) {
        const results = this.props.recomendation.results;

        this.setState({
          results,
        })
      } else {
        // no data
      }
    }
  }

  handleOnClickDrug = (e) => {
    const { currentDrug } = this.state;
    const current = currentDrug === e.target.dataset['index'] ? -1 : e.target.dataset['index'];
    this.setState({
      currentDrug: current
    })
  }

  render() {
    const {results, currentDrug } = this.state;
    console.log(currentDrug);
    const drugs = results.map((element, index) => (
        <div className="recomendation__drug" data-index={index} key={index}
          data-pid={element.pid} onClick={this.handleOnClickDrug}
          >
          <Drugs index={index}  drugs={element.drug}/>
        </div>
    ))
    return(
      <div className="recommendation">
        <div className="recommendation__leftSide">
          {drugs}
        </div>
        <div className="recommendation__rightSide">
          {currentDrug !== -1 &&
            <Outcomes outcomes={results[currentDrug].outcomes}/>
          }
        </div>
      </div>
    )
  }
}

export default connect(state => ({
  recomendation: state.recommendations.recommendation
}), { changePath }) (Recommendation);

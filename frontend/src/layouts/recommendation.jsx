import React, {Component, Fragment} from 'react';
import { Link } from "react-router-dom";
import { connect } from 'react-redux';
import dayjs from 'dayjs'
import getHistory from '../modules/history';
import { changePath } from '../actions/actionPath';
import { drugEnglToRus } from '../constants';
import { recommendationClean } from '../actions/actionSession';

class Drugs extends Component {
  state = {
    show: false,
    drugs: [],
    data: [],
    current: -1
  }

  componentDidMount = () => {
    const { drugs } = this.props;
    this.setState({
      drugs,
    })
  }

  componentDidUpdate = (prevProps) => {
    if (prevProps.current !== this.props.current) {
      this.setState({
        current: +this.props.current,
      })
    }
  }

  handleOnClick = (e) => {
    this.props.func(e.target.dataset['index'])
  }

  handleOnClickLink = () => {
    this.props.clean()
  }

  render() {
    const { drugs, current } = this.state;
    const { indexP } = this.props;
    const data = drugs.length ? drugs.splice(1, drugs.length - 1) : [];
    const analogies = data.map((element, index) => (
      <span key={index} className="recomendation__analogies">
        <Link to={`/all-drugs/instruction/${element.id}`} onClick={this.handleOnClickLink}>
          {element.tradeName}
        </Link>
      </span>
    ))
    return (
      <Fragment>
        <div className="recomendation__drug-list" data-index={indexP} >
          {drugs.length !== 0 &&
            <Fragment>
              <div className="recomendation__drug-tittle">
                <label data-index={indexP} >
                  {drugs[0].tradeName}
                </label>
              </div>
              <div className="recomendation__description">
                <div className="allDrugs__description-1">
                  <label className="allDrugs__description-tittle">
                    {drugEnglToRus["releaseFormVSDosage"]}:
                  </label>
                  <span className="allDrugs__description-text">
                    {drugs[0].releaseFormVSDosage}
                  </span>
                </div>
                <div className="allDrugs__description-2">
                  <label className="allDrugs__description-tittle">
                    {drugEnglToRus["manufacturer"]}:
                  </label>
                  <span className="allDrugs__description-text">
                    {drugs[0].manufacturer}
                  </span>
                </div>
                <div className="allDrugs__description-3">
                  <label className="allDrugs__description-tittle">
                    Аналоги:
                  </label>
                  <span className="allDrugs__description-text">
                    {analogies}
                  </span>
                </div>
              </div>
            </Fragment>
          }
        </div>
        <div className="recomendation__drug-showAll" data-index={indexP}
          onClick={this.handleOnClick}
          >
          {current === indexP ?
            "Скрыть осмотр"
            :
            "Показать осмотр"
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
        length: outcomes.length,
      })
    }
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.outcomes !== prevProps.outcomes) {
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
  }

  handleOnClickPrev = () => {
    let { currentView } = this.state;
    const { outcomes } = this.props;
    const isEnabledPrev = currentView > 0;
    currentView = currentView - 1;
    if (isEnabledPrev) {
      this.setState({
        currentView,
        ...outcomes[currentView].state,
        date: outcomes[currentView].date,
      })
    }
  };

  handleOnClickNext = () => {
    let { currentView, length } = this.state;
    const { outcomes } = this.props;
    const isEnabledNext = currentView < length - 1;
    currentView = currentView + 1;
    if (isEnabledNext) {
      this.setState({
        currentView,
        ...outcomes[currentView].state,
        date: outcomes[currentView].date,
      })
    }
  }

  render () {
    const { noData, examinationsResults, objectiveInspection, plaints,
      specialistsConclusions, date, currentView, length } = this.state;

    const isEnabledPrev = currentView > 0;
    const isEnabledNext = currentView < length - 1;

    return (
      <div className="recomendation__reception">
        {noData ?
          <div className="recomendation__reception-noData"></div>
          :
          <Fragment>
            <div className="recomendation__reception-header">
                <i className="fa fa-angle-double-left fa-1x" aria-hidden="true"
                   onClick={this.handleOnClickPrev}
                   style={ isEnabledPrev ? {cursor: 'pointer', color: 'black'} :
                   {cursor: 'default',color: '#bdbcbc'}}
                />
                <div className="recomendation__reception-date">
                  <label className="reception__date">
                    {date == null ?
                        'Дата не указана'
                      :
                        dayjs(date).format('DD/MM/YYYY HH:mm')
                    }
                  </label>
                </div>
                <i className="fa fa-angle-double-right fa-1x" aria-hidden="true"
                  onClick={this.handleOnClickNext}
                  style={ isEnabledNext ? {cursor: 'pointer', color: 'black'} :
                  {cursor: 'default', color: '#bdbcbc'}}
                />
            </div>
            <div className="recomendation__reception-body">
              <div className="recomendation__reception-examinationsResults">
                <label> examinationsResults: </label>
                <p> {examinationsResults} </p>
              </div>
              <div className="recomendation__reception-objectiveInspection">
                <label> objectiveInspection: </label>
                <p> {objectiveInspection} </p>
              </div>
              <div className="recomendation__reception-plaints">
                <label> plaints </label>
                <p> {plaints} </p>
              </div>
              <div className="recomendation__reception-specialistsConclusions">
                <label> specialistsConclusions: </label>
                <p> {specialistsConclusions} </p>
              </div>
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

  setCurrentDrug = (value) => {
    const { currentDrug } = this.state;
    const current = currentDrug === value ? -1 : value;
    this.setState({
      currentDrug: current
    })
  }

  render() {
    const {results, currentDrug } = this.state;
    const drugs = results.map((element, index) => (
      <div className="recomendation__drug" key={index} data-index={index}
        data-pid={element.pid}
        >
        <Drugs indexP={index} drugs={element.drug} func={this.setCurrentDrug}
          current={currentDrug} clean={this.props.recommendationClean}
        />
      </div>
    ))
    return(
      <div className="recommendation-wrap">
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
      </div>
    )
  }
}

export default connect(state => ({
  recomendation: state.recommendations.recommendation
}), { changePath, recommendationClean }) (Recommendation);

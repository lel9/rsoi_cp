import React, {Component} from 'react';
import { Select, Button, Tabs, Radio } from 'antd';
import getHistory from '../modules/history';
import InputField from '../components/inputField';
import { changePath } from '../actions/actionPath.js';
import { addRecommendation } from '../actions/actionRecommendation';
import { connect } from 'react-redux';
const { Option } = Select;
const { TabPane } = Tabs;


const Elements = ({obj}) => obj.map((element, index) => (
  <InputField
    key={index}
    label={element.label}
    className={element.className}
    required={element.required}
    id={element.id}
    func={element.actionOnChange}
    lel9={element.lel9}
  />
))

const genOption = (val1, val2) => {
  const data = [];
  for (let i = val1; i <= val2; i++) {
    data.push(<Option key={i} value={i}>{i}</Option>)
  }
  return data;
}

class DrugSelection extends Component {
  state = {
    plaints: '',
    objectiveInspection: '',
    examinationsResults: '',
    specialistsConclusions: '',
    text : '',
  }

  handleOnClickSex = (value) => {
    // console.log("hello");
    this.setState({
      sex: value
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

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    // this.props.setPath(getHistory().location.pathname);
  }
  handleOnSubmit = () => {
    const data = this.state;
    const { text } = this.state;
    const diagnosis = { text };
    delete data["text"]
    this.props.addRecommendation({diagnosis: diagnosis, state: data});
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.recommendation !== prevProps.recommendation) {
      getHistory().push(`/select-drug/recommendation`)
    }
  }

  render() {
    const { plaints, objectiveInspection, examinationsResults,
       specialistsConclusions, text } = this.state;
    const years = genOption(1, 200);
    const month = genOption(1, 12);

    const Parametres1 = [
      {lel9: plaints, label: "Жалобы",
        actionOnChange: this.handleOnChangePlaints},
      {lel9: objectiveInspection, label: "Объективный осмотр",
        actionOnChange: this.handleOnChangeObjectiveInspection},
      {lel9: examinationsResults, label: "Результаты исследований",
        actionOnChange: this.handleOnChangeExaminationsResults},
      {lel9: specialistsConclusions, label: "Заключения специалистов",
        actionOnChange: this.handleOnChangeSpecialistsConclusions},
       ];

    const Parametres2 = [
      {lel9: text, label: "Диагноз",
        actionOnChange: this.handleOnChangeDiagnosis}
       ];

    return (
      <div className="drugSelection">
        <Tabs defaultActiveKey="1" onChange={null}>
          <TabPane tab="Текущее состояние" key="1">
              <Elements obj={Parametres1}/>
          </TabPane>
          <TabPane tab="Диагноз" key="2">
            <Elements obj={Parametres2}/>
          </TabPane>
        </Tabs>
        <Button onClick={this.handleOnSubmit}>
          Подбор препаратов
        </Button>
      </div>
    )
  }
}

export default connect(state => ({
  recommendation: state.recommendations.recommendation
}), { addRecommendation, changePath })(DrugSelection);

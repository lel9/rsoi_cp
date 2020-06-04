import React, {Component} from 'react';
import { Select, Button, Radio } from 'antd';
import getHistory from '../modules/history';
import InputField from '../components/inputField';
import { changePath } from '../actions/actionPath.js';
import { addRecommendation } from '../actions/actionRecommendation';
import { connect } from 'react-redux';
const { Option } = Select;


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
    sex: '',
    diseaseAnamnesis: '',
    lifeAnamnesis: '',
  }

  handleOnClickSex = (e) => {
    this.setState({
      sex: e.target.value
    })
  }
  handleOnChangeDiseaseAnamnesis = (e) => {
    this.setState({
      diseaseAnamnesis: e.target.value
    })
  }
  handleOnChangeLifeAnamnesis = (e) => {
    this.setState({
      lifeAnamnesis: e.target.value
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
    if (this.props.results !== prevProps.results) {
      getHistory().push(`/select-drug/recommendation`)
    }
  }

  render() {
    const { plaints, objectiveInspection, examinationsResults,
       specialistsConclusions, text, diseaseAnamnesis, lifeAnamnesis } = this.state;
    const years = genOption(0, 120);
    const month = genOption(0, 11);

    const Parametres1 = [
      {lel9: diseaseAnamnesis, label: "Анамнез заболевания",
        actionOnChange: this.handleOnChangeDiseaseAnamnesis},
      {lel9: lifeAnamnesis, label: "Анамнез жизни",
        actionOnChange: this.handleOnChangeLifeAnamnesis},
      {lel9: plaints, label: "Жалобы",
        actionOnChange: this.handleOnChangePlaints},
      {lel9: objectiveInspection, label: "Объективный осмотр",
        actionOnChange: this.handleOnChangeObjectiveInspection},
      {lel9: examinationsResults, label: "Результаты исследований",
        actionOnChange: this.handleOnChangeExaminationsResults},
      {lel9: specialistsConclusions, label: "Заключения специалистов",
        actionOnChange: this.handleOnChangeSpecialistsConclusions},
      {lel9: text, label: "Диагноз",
        actionOnChange: this.handleOnChangeDiagnosis}
       ];

    return (
      <div className="drugSelection">
        <div className="drugSelection__sex">
          <label className="drugSelection__sex-tittle">
            Пол
          </label>
          <Radio.Group onChange={this.handleOnClickSex} value={this.state.sex}>
           <Radio value={"m"}>мужской</Radio>
           <Radio value={"f"}>женский</Radio>
         </Radio.Group>
        </div>
        <div className="drugSelection__age">
          <label>Возраcт</label>
          <div className="drugSelection__years">
            <Select defaultValue="0" style={{ width: 70 }} onChange={null}>
              {years}
            </Select>
            <label>лет</label>
          </div>
         <div className="drugSelection__month">
           <Select defaultValue="0" style={{ width: 70 }} onChange={null}>
             {month}
           </Select>
           <label>мес</label>
         </div>
        </div>
        <Elements obj={Parametres1}/>
        <Button onClick={this.handleOnSubmit}>
          Поиск препаратов
        </Button>
      </div>
    )
  }
}

export default connect(state => ({
  recommendations: state.recommendations.recommendations,
  results: state.recommendations
}), { addRecommendation, changePath })(DrugSelection);

import React, {Component} from 'react';
import { Select, Button, Tabs, Radio } from 'antd';
import getHistory from '../modules/history';
import InputField from '../components/inputField';
import { changePath } from '../actions/actionPath.js';
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
    sex: '',
    lel9_1_1: '',
    lel9_1_2: '',
    lel9_1_3: '',
    lel9_1_4: '',
    lel9_1_5: '',
    lel9_2_1: '',
    lel9_2_2: '',
    lel9_2_3: '',
    lel9_2_4: '',
  }

  handleOnClickSex = (value) => {
    // console.log("hello");
    this.setState({
      sex: value
    })
  }

  componentDidMount = () => {
    // console.log(this.props);
    this.props.changePath(getHistory().location.pathname);
    this.props.setPath(getHistory().location.pathname);
  }
  handleOnSubmit = () => {
    getHistory().push('/select-drug/recomendation')
  }

  render() {
    const years = genOption(1, 200);
    const month = genOption(1, 12);
    const Parametres1 = [
      {lel9: this.state.lel9_1_3, actionOnChange: this.handleOnChange1_3, label: "Ананмез жизни", className: "page1",
          id: "", error: ""},
      {lel9: this.state.lel9_1_4, actionOnChange: this.handleOnChange1_4, label: "Анамнез заболевания", className: "page1",
         id: "", error: ""}
       ];
    const Parametres2 = [
      {lel9: this.state.lel9_2_1 ,actionOnChange: this.handleOnChange2_1, label: "Жалобы", className: "page1",
          id: "", error: ""},
      {lel9: this.state.lel9_2_2, actionOnChange: this.handleOnChange2_2, label: "Объективный осмотр", className: "page1",
         id: "", error: ""},
      {lel9: this.state.lel9_2_3, actionOnChange: this.handleOnChange2_3, label: "Результаты исследований", className: "page1",
          id: "", error: ""},
      {lel9: this.state.lel9_2_4, actionOnChange: this.handleOnChange2_4, label: "Заключения специалистов", className: "page1",
         id: "", error: ""},
    ];
    const Parametres3 = [
      {lel9: this.state.lel9_1_5, actionOnChange: this.handleOnChange1_5, label: "Диагноз", className: "page1",
          id: "", error: ""},
       ];
    return (
      <div className="drugSelection">
        <Tabs defaultActiveKey="1" onChange={null}>
          <TabPane tab="Анамнез" key="1">
            <div className="drugSelection__sex">
              <Radio.Group onChange={this.onChange} value={this.state.value}>
               <Radio value={"мужской"}>мужской</Radio>
               <Radio value={"женский"}>женский</Radio>
             </Radio.Group>
            </div>
            <div className="drugSelection__age">
              <label>Возрат</label>
              <div className="drugSelection__years">
                <Select defaultValue="1" style={{ width: 70 }} onChange={null}>
                  {years}
                </Select>
                <label>лет</label>
              </div>
              <div className="drugSelection__month">
                <Select defaultValue="1" style={{ width: 70 }} onChange={null}>
                  {month}
                </Select>
                <label>мес</label>
              </div>
            </div>
              <Elements obj={Parametres1}/>
          </TabPane>
          <TabPane tab="Текущее состояние" key="2">
            <Elements obj={Parametres2}/>
          </TabPane>
          <TabPane tab="Диагноз" key="3">
            <Elements obj={Parametres3}/>
          </TabPane>
        </Tabs>
        <Button onClick={this.handleOnSubmit}>
          Поиск препаратов
        </Button>
      </div>
    )
  }
}

export default connect(() => ({}), { changePath })(DrugSelection);

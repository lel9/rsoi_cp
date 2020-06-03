import React, {Component } from 'react';
import { connect } from 'react-redux';
import { Steps, Button } from 'antd';
import { addDrug } from '../actions/actionDrug.js';
import getHistory from '../modules/history';
import InputField from '../components/inputField' ;
import { changePath } from '../actions/actionPath.js';
const { Step } = Steps;

const Elements = ({obj, classN, actionOnChange}) => obj.map((element, index) => (
  <InputField
    key={index}
    label={element.label}
    className={classN}
    func={actionOnChange}
    lel9={element.lel9}
    reference={element.ref}
    required={element.required}
  />
))

class AddDrug extends Component {
  state = {
      currentView: 0,
      tradeName: '',
      activeSubstance: '',
      group: '',
      atx: '',
      form: '',
      composition: '',
      description: '',
      pharmacodynamics: '',
      pharmacokinetics: '',
      indications: '',
      contraindications: '',
      withCaution: '',
      pregnancyAndLactation: '',
      directionForUse: '',
      sideEffects: '',
      overdose: '',
      interaction: '',
      specialInstruction: '',
      vehicleImpact: '',
      releaseFormVSDosage: '',
      transportationСonditions: '',
      storageСonditions: '',
      storageLife: '',
      vacationFromPharmacies: '',
      manufacturer: '',
      certificateOwner: '',
      error: null,
    }

  tradeNameRef = React.createRef();
  activeSubstanceRef = React.createRef();
  groupRef = React.createRef();
  atxRef = React.createRef();
  formRef = React.createRef();
  compositionRef = React.createRef();
  descriptionRef = React.createRef();
  pharmacodynamicsRef = React.createRef();
  pharmacokineticsRef = React.createRef();
  indicationsRef = React.createRef();
  contraindicationsRef = React.createRef();
  withCautionRef = React.createRef();
  pregnancyAndLactationRef = React.createRef();
  directionForUseRef = React.createRef();
  sideEffectsRef = React.createRef();
  overdoseRef = React.createRef();
  interactionRef = React.createRef();
  specialInstructionRef = React.createRef();
  vehicleImpactRef = React.createRef();
  releaseFormVSDosageRef = React.createRef();
  transportationСonditionsRef = React.createRef();
  storageСonditionsRef = React.createRef();
  storageLifeRef = React.createRef();
  vacationFromPharmaciesRef = React.createRef();
  manufacturerRef = React.createRef();
  certificateOwnerRef = React.createRef();


  handleOnChange1 = () => {
    this.setState({
      tradeName: this.tradeNameRef.current.value,
      activeSubstance: this.activeSubstanceRef.current.value,
      form: this.formRef.current.value,
      composition: this.compositionRef.current.value,
      description: this.descriptionRef.current.value,
    })
  }

  handleOnChange2 = () => {
    this.setState({
      group: this.groupRef.current.value,
      atx: this.atxRef.current.value,
      pharmacodynamics: this.pharmacodynamicsRef.current.value,
      pharmacokinetics: this.pharmacokineticsRef.current.value,
    })
  }

  handleOnChange3 = () => {
    this.setState({
      indications: this.indicationsRef.current.value,
      contraindications: this.contraindicationsRef.current.value,
      withCaution: this.withCautionRef.current.value,
      pregnancyAndLactation: this.pregnancyAndLactationRef.current.value,
      directionForUse: this.directionForUseRef.current.value,
    })
  }

  handleOnChange4 = () => {
    this.setState({
      sideEffects: this.sideEffectsRef.current.value,
      overdose: this.overdoseRef.current.value,
      interaction: this.interactionRef.current.value,
      specialInstruction: this.specialInstructionRef.current.value,
      vehicleImpact: this.vehicleImpactRef.current.value,
    })
  }

  handleOnChange5 = () => {
    this.setState({
      releaseFormVSDosage: this.releaseFormVSDosageRef.current.value,
      transportationСonditions: this.transportationСonditionsRef.current.value,
      storageСonditions: this.storageСonditionsRef.current.value,
      storageLife: this.storageLifeRef.current.value,
      vacationFromPharmacies: this.vacationFromPharmaciesRef.current.value,
      manufacturer: this.manufacturerRef.current.value,
      certificateOwner: this.certificateOwnerRef.current.value,
    })
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    // this.props.setPath(getHistory().location.pathname);
    getHistory().push(`/all-drugs/add-drug/1`)
    this.setState({
      currentView: +window.location.pathname.split('/')[3] - 1,
    })
  }

  handleOnClickNext = () => {
    const currentPage = getHistory().location.pathname;
    let currentView = +currentPage.split('/')[3];
    getHistory().push(`/all-drugs/add-drug/${currentView + 1}`)
    this.setState({
      currentView: currentView,
    })
  }

  handleOnClickPrev = () => {
    const currentPage = getHistory().location.pathname;
    let currentView = +currentPage.split('/')[3];
    getHistory().push(`/all-drugs/add-drug/${currentView - 1}`)
    this.setState({
      currentView: currentView - 2,
    })
  }

  handleOnClickSteps = (value) => {
    this.setState({
      currentView: value
    })
    getHistory().push(`/all-drugs/add-drug/${value + 1}`)
  }

  handleOnSubmit = () => {
    let data = this.state;
    delete data["currentView"];
    getHistory().push(`/all-drugs/add-drug/1`)
    this.props.addDrug(data);
    this.setState({
      currentView: 0,
    })
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.error !== prevProps.error && !this.props.error) {
      this.setState({
        tradeName: '',
        activeSubstance: '',
        group: '',
        atx: '',
        form: '',
        composition: '',
        description: '',
        pharmacodynamics: '',
        pharmacokinetics: '',
        indications: '',
        contraindications: '',
        withCaution: '',
        pregnancyAndLactation: '',
        directionForUse: '',
        sideEffects: '',
        overdose: '',
        interaction: '',
        specialInstruction: '',
        vehicleImpact: '',
        releaseFormVSDosage: '',
        transportationСonditions: '',
        storageСonditions: '',
        storageLife: '',
        vacationFromPharmacies: '',
        manufacturer: '',
        certificateOwner: '',
      })
    }
    if (this.props.error !== prevProps.error) {
      this.setState({
        error: this.props.error,
      })
    }
  }

  render() {
    const {
      currentView, tradeName, activeSubstance, group, atx, form,
      composition, description, pharmacodynamics, pharmacokinetics,
      indications, contraindications, withCaution, pregnancyAndLactation,
      directionForUse, sideEffects, overdose, interaction, specialInstruction,
      vehicleImpact, releaseFormVSDosage, transportationСonditions,
      storageСonditions, storageLife, vacationFromPharmacies, manufacturer,
      certificateOwner, error
     } = this.state;
     const Parametres1 = [
       {lel9: tradeName, label: "Торговое наименование", ref: this.tradeNameRef, required: true},
       {lel9: activeSubstance, label: "Действующее вещество", ref: this.activeSubstanceRef},
       {lel9: form, label: "Лекарственная форма", ref: this.formRef},
       {lel9: composition, label: "Состав", ref: this.compositionRef},
       {lel9: description, label: "Описание", ref: this.descriptionRef}
      ];
     const Parametres2 = [
       {lel9: group, label: "Фармакотерапевтическая группа", ref: this.groupRef},
       {lel9: atx, label: "АТХ", ref: this.atxRef},
       {lel9: pharmacodynamics, label: "Фармакодинамика", ref: this.pharmacodynamicsRef},
       {lel9: pharmacokinetics, label: "Фармакокинетика", ref: this.pharmacokineticsRef},
     ];
     const Parametres3 = [
       {lel9: indications, label: "Показания к применению", ref: this.indicationsRef},
       {lel9: contraindications, label: "Противопоказания", ref: this.contraindicationsRef},
       {lel9: withCaution, label: "С осторожностью", ref: this.withCautionRef},
       {lel9: pregnancyAndLactation, label: "Применение при беременности и в период грудного вскармливания", ref: this.pregnancyAndLactationRef},
       {lel9: directionForUse, label: "Способ применения и дозы", ref: this.directionForUseRef},
     ];
     const Parametres4 = [
       {lel9: sideEffects, label: "Побочные эффекты", ref: this.sideEffectsRef},
       {lel9: overdose, label: "Передозировка", ref: this.overdoseRef},
       {lel9: interaction, label: "Взаимодействие с другими лекарственными средствами", ref: this.interactionRef},
       {lel9: specialInstruction, label: "Особые указания", ref: this.specialInstructionRef},
       {lel9: vehicleImpact, label: "Влияние на способность управлять транспортными средствами и механизмами", ref: this.vehicleImpactRef},
     ];
     const Parametres5 = [
       {lel9: releaseFormVSDosage, label: "Форма выпуска", ref: this.releaseFormVSDosageRef},
       {lel9: transportationСonditions, label: "Условия транспортирования", ref: this.transportationСonditionsRef},
       {lel9: storageСonditions, label: "Условия хранения", ref: this.storageСonditionsRef},
       {lel9: storageLife, label: "Срок годности", ref: this.storageLifeRef},
       {lel9: vacationFromPharmacies, label: "Условия отпуска", ref: this.vacationFromPharmaciesRef},
       {lel9: manufacturer, label: "Производитель", ref: this.manufacturerRef},
       {lel9: certificateOwner, label: "Владелец регистрационного удостоверения", ref: this.certificateOwnerRef},
     ];

    return(
      <div className="addDrug">
        <div className="addDrug__pagination">
          <Steps current={currentView} onChange={this.handleOnClickSteps}>
            <Step title="Шаг 1"/>
            <Step title="Шаг 2"/>
            <Step title="Шаг 3"/>
            <Step title="Шаг 4"/>
            <Step title="Шаг 5"/>
          </Steps>
        </div>
        {error && error.status === 400 &&
          <div className="emptyField addDrug-emptyField">{error.data.error_description}</div>
        }
        <div className="addDrug__content">
          { currentView === 0 ?
            <Elements obj={Parametres1} actionOnChange={this.handleOnChange1} classN="addDrug"/>
            :
            currentView === 1 ?
            <Elements obj={Parametres2} actionOnChange={this.handleOnChange2} classN="addDrug"/>
            :
            currentView === 2 ?
            <Elements obj={Parametres3} actionOnChange={this.handleOnChange3} classN="addDrug"/>
            :
            currentView === 3 ?
            <Elements obj={Parametres4} actionOnChange={this.handleOnChange4} classN="addDrug"/>
            :
            currentView === 4 ?
            <Elements obj={Parametres5} actionOnChange={this.handleOnChange5} classN="addDrug"/>
            :
            null
          }
        </div>
        <div className="addDrug__footer">
          {currentView !== 0 ?
            <Button onClick={this.handleOnClickPrev}>Назад</Button>
            :
            null
          }
          {currentView !== 4 ?
            <Button onClick={this.handleOnClickNext}>Далее</Button>
            :
            <Button onClick={this.handleOnSubmit}>Сохранить</Button>
          }
        </div>
      </div>
    )
  }
}

export default connect(state => ({
  initialState: state.initialState,
  error: state.drugs.error
}),{addDrug, changePath})(AddDrug);

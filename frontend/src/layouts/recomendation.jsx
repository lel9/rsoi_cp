import React, {Component, Fragment} from 'react';
import { TimePicker, DatePicker } from 'antd';
import Button from '../components/button';
import getHistory from '../modules/history';
import { changePath } from '../actions/actionPath.js';
import moment from 'moment';
import { connect } from 'react-redux';
const dateFormat = 'DD/MM/YYYY';

const obj = [
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "1",
            "pewpew1": "2",
            "pewpew2": "3"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "4",
            "pewpew1": "5",
            "pewpew2": "6"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  },
  {
    "preparate": "Амитриптилин",
    "count": 10,
    "analogy": [],
    "patients": [
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      },
      {
        "sex": "male",
        "age": 40,
        "map": [
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          },
          {
            "date": "12/12/2012",
            "pewpew": "dasdadas",
            "pewpew1": "hello",
            "pewpew2": "hello"
          }
        ]
      }
    ]
  }
];

const RightSide = ({obj, func, pagination, curMap, max}) => {
  const handleOnClick = (e) => {
    func(e.target.dataset["index"])
  }

  const rightSide = obj.map((element, index) =>
    <div className="map" key={index}  data-index={index}
      onClick={handleOnClick}
      >
      { +curMap === +index &&
        <Fragment>
          {element.pewpew}
          {element.pewpew1}
          {element.pewpew2}
        </Fragment>
      }
    </div>
  )

  const handleOnClickPrev = () => {
    func(curMap - 1)
  }

  const handleOnClickNext = () => {
    func(curMap + 1)
  }

  const disabledNext = curMap === max - 1 ? true : false;
  const disabledPrev = curMap === 0 ? true : false;

  return (
    <div className="recomendations__rightSide">
      <div className="recomendations__map-header">
        <Button disabled={disabledPrev} visible={true} func={handleOnClickPrev}>&lt;</Button>
        <DatePicker defaultValue={moment('06/06/2015', dateFormat)}
           format={dateFormat} disabled
        />
        <TimePicker defaultValue={moment('12:08:23', 'HH:mm:ss')} disabled />
        <Button disabled={disabledNext} visible={true} func={handleOnClickNext}>&gt;</Button>
      </div>
      {rightSide}
    </div>
  )
}


class Recomendation extends Component {
  state = {
    currentPatient: 0,
    currentPreparate: 0,
    currentMap: 0
  }

  getMap = (value) => {
    this.setState({
      currentMap: +value,
    })
  }


  getPatient = (value) => {
    this.setState({
      currentPatient: +value,
    })
  }

  handleOnClick = (e) => {
    e.persist();
    this.setState({
      currentPreparate: +e.target.dataset["index"],
    })
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    this.props.setPath(getHistory().location.pathname);
  }

  render() {
    const {currentPatient, currentPreparate, currentMap} = this.state;
    const leftSide = obj.map((element, index) =>
      <div className="recomendations__preparate" data-index={index}
        onClick={this.handleOnClick} key={index}
      >
        {element.preparate}({element.count})
      </div>
    )

    return(
      <div className="recomendations">
        <div className="recomendations__leftSide">
          {leftSide}
        </div>

        <RightSide obj={obj[currentPreparate].patients[currentPatient].map}
          func={this.getMap} curMap={currentMap}
          max={obj[currentPreparate].patients[currentPatient].map.length}
        />

      </div>
    )
  }
}

export default connect(() => ({}), { changePath }) (Recomendation);

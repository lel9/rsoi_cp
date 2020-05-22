import React, { Component, Fragment } from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import { Link } from "react-router-dom";

import rect from '../public/images/blue_rectangle.jpg'

const Select = ({element, index, func, visibility, color}) => {
  return (
    <div key={index} data-value={element} style={{background: color}}
      onClick={func} data-index={index} className="radioButton-select">
      <label data-index={index} data-value={element}>{element}</label>
      <div data-index={index} data-value={element}>
        <img src={rect} style={{visibility: visibility}} alt=""/>
      </div>
    </div>
  )
}

const ToogleAbleLink = ({children, disable, ...props}) => {
  const { ...rest } = props;
  return disable ? children : <Link {...rest}>{children}</Link>
}

class RadioB extends Component {
  state = {
    value: 0,
    visibility: [],
  }

  static propTypes = {
    id: PropTypes.string.isRequired,
    label: PropTypes.string,
    vertical: PropTypes.bool,
    className: PropTypes.string,
    func: PropTypes.func,
    array: PropTypes.array,
    inside: PropTypes.bool,
  }

  static defaultProps = {
    id: '',
    label: '',
    vertical: false,
    className: '',
    func: () => {},
    array:[],
    inside: false,
  }

  componentDidMount = () => {
    let visibility = [];
    const { array } = this.props;
    for(let i = 0; i < array.length; i++) {
      visibility.push("hidden")
    }
    this.setState({
      visibility: visibility
    })
  }

  render() {
    const { className, vertical, error, id, label, inside, ...attrs } = this.props;
    const classes = classNames (
      'radioButton',
      {[`radioB-${className}`]:className},
      {'vertical':vertical},
      {error},
      {'inside':inside}
    )

    const getData = (e) => {
      e.persist();
      const index = e.target.dataset.index;
      const { visibility } = this.state;
      for(let i = 0; i < attrs.array.length; i++) {
        visibility[i] = "hidden";
      }
      visibility[index] = "visible";
      this.setState({
        visibility: visibility,
        value: e.target.dataset.value
      })
      attrs.func(e.target.dataset.value)
    }

    const Elements = attrs.array.map((element, index) =>
      <ToogleAbleLink key={index}
        to={{
            pathname: `/${attrs.path}/${element.val}`
        }}
        disable={!this.props.link}
        >
        <Select element={element.val} index={index} func={getData}
          visibility={this.state.visibility[index]}
          color={!inside ? '' : index + 1 === attrs.current ? '#c5c5c5' : 'white'}
        />
    </ToogleAbleLink>
    )

    return(
      <Fragment>
        {label &&
          <span className="raioButton__Label">{label}</span>
        }

        <div className={classes}>
          {Elements}
          {inside &&
            <div className="radioButton__background"></div>
          }
        </div>

        {error &&
          <span className="radioButton__Error"></span>
        }
      </Fragment>
    )
  }
}



export default RadioB;

import React, {Component} from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


class Button extends Component {
  state = {
    color: "white"
  }
  static propTypes = {
    children: PropTypes.node,
    className: PropTypes.string,
    func: PropTypes.func,
    disabled: PropTypes.bool,
    active: PropTypes.bool
  }

  static defaultProps = {
    children: 'default button',
    className: '',
    func: () => {},
    disable: false,
    active: false
  }



  handleOnMouseOver = () => {
    this.setState({
      color: "#bbbbbb69",
    })
  }

  handleOnMouseOut = () => {
    this.setState({
      color: "white"
    })
  }


  render() {
  const {className, active, visible, disabled, func, children} = this.props;
  const classes = classNames (
      'btn',
      {className},
      {active}
    )
  const display = visible ? 'block' : 'none';
    return (
      <button className={classes}
        disabled={disabled}
        onClick={func}
        style={{background: this.state.color, display: display}}
        onMouseOver={this.handleOnMouseOver}
        onMouseOut={this.handleOnMouseOut}
        >
        {children}
      </button>
    );
  }
}



export default Button;

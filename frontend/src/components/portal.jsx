import {Component} from 'react';
import ReactDOM from 'react-dom';

export default class Portal extends Component {
  el = document.createElement('div');


  componentDidMount() {
    this.el.id = 'portal';
    document.body.appendChild(this.el)
  }

  componentWillUnmount() {
    document.body.removeChild(this.el);
  }

  render() {
    return(ReactDOM.createPortal(this.props.children, this.el));
  }
}

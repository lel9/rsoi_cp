import React, {Component} from 'react';

class Tree extends Component {
  state = {
    show: -1
  }

  handleOnclick = (e) => {
    e.persist();
    let { show } = this.state;
    show = +show === +e.target.dataset["index"] ? -1 : +e.target.dataset["index"];
    this.setState({
      show: show,
    })
  }

  render() {
    const Elements = this.props.obj.map((element, index) =>
    <div key={index} className="branch tree__branch"
        data-index={index}
      >
      <div className="tree__branch-title" data-index={index}
        onClick={this.handleOnclick}
        >
        { this.state.show === index &&
          <span data-index={index} >&or;</span>
        }
        { this.state.show !== +index &&
          <span data-index={index} >&gt;</span>
        }
        {element.title}
      </div>
      { this.state.show === index &&
        <p className="tree__branch-description">
          {element.description}
        </p>
      }

    </div>

  )
    return (
      <div className="tree">
        {Elements}
      </div>
    )
  }
}

export default Tree;

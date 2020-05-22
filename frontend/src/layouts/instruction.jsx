import React, { Component } from 'react';
import { Link } from "react-router-dom";
import getHistory from '../modules/history';
import Tree from '../components/tree';
import { connect } from 'react-redux';
import { getDrugById, getDrugs } from '../actions/actionDrug';
import { getComments, addComment } from '../actions/actionComment';
import { drugEnglToRus} from '../constants';
import InputField from '../components/inputField' ;
import { Button, List } from 'antd';
import dayjs from 'dayjs';
import { changePath } from '../actions/actionPath.js';

class Instruction extends Component {
  state = {
    id: '',
    drug: [],
    comments: [],
    textComment: '',
    error: '',
    role: ['USER'],
    isDisabledPost: true,
    emptyProfile: null,
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);
    this.props.setPath(getHistory().location.pathname);
    const id = this.props.match.params.id;
    let isDisabledPost = true;
    if(Object.keys(this.props.role).length) {
      isDisabledPost = this.props.role.authorities.includes('ROLE_ADMIN')
      || this.props.role.authorities.includes('ROLE_EXPERT')
    }
    this.props.getDrugById(id);
    this.props.getComments({id: id, page: 0, size: 15});
    this.setState({
      id,
      role: this.props.role.authorities,
      isDisabledPost
    })
  }

  componentDidUpdate = (prevProps) => {
    if (this.props.drug !== prevProps.drug) {
      this.setState({
        drug: this.props.drug
      })
    }
    if(this.props.comments !== prevProps.comments) {
      this.setState({
        comments: this.props.comments.results
      })
    }
    if(this.props.error !== prevProps.error) {
      this.setState({
        error: this.props.error.err
      })
    }
    if(this.props.emptyProfile !== prevProps.emptyProfile) {
      if(this.props.emptyProfile) {
        this.setState({
          emptyProfile: this.props.emptyProfile.data
        })
      }
    }
  }

  getInstruction = drugs => {
    const data = []
    for (let key in drugs) {
      if(key !== 'id') {
        data.push({
          title: drugEnglToRus[key],
          description: drugs[key]
        })
      }
    }
    return data;
  }

  handleOnClick = () => {
    const { textComment, id } = this.state;
    if(textComment !== '') {
      this.props.addComment({id: id, text: textComment});
    }
    this.setState({
      textComment: ''
    })
  }

  handleOnChange = ({target}) => {
    this.setState({
      textComment: target.value
    })
  }

  render() {
    const {drug, comments, textComment, error, role,
       isDisabledPost, emptyProfile } = this.state;
    // const { role } = this.props;
    // console.log(isDisabledPost);
    const data = this.getInstruction(drug);
    return (
      <div className="instruction">
        <Tree obj={data}/>
        { error === 'Network Error' ?
          <div className="Network-Error allDrugs__Network-Error">
            Сервис комментариев временно недоступен
          </div>
          :
          <div className="instruction__comments">
            {emptyProfile && emptyProfile.error === 'no_profile' &&
              <div className="emptyField instruction-emptyField">{emptyProfile.error_description}</div>
            }

            {isDisabledPost &&
              <div className="instruction__comments-add">
                <InputField lel9={textComment} className="instruction"
                  func={this.handleOnChange}
                />
                <Button
                  onClick={this.handleOnClick}
                 >
                   Добавить
                 </Button>
              </div>
            }
            <div className="instruction__comments-list">
              <List
                itemLayout="horizontal"
                dataSource={comments}
                renderItem={(item, index) => (
                  <List.Item >
                    <List.Item.Meta
                      title={item.text}
                      description={
                        <Link className="title" key={index} to={`/profile/${item.author.id}`}>
                          <span className="title__name">{item.author.displayName}</span>
                          <span className="title__time">{dayjs.unix(item.date/1000).format('DD/MM/YYYY')}</span>
                        </Link>
                      }
                    />
                  </List.Item>
                )}
              />
            </div>
          </div>
        }
      </div>
    )
  }
}

export default connect(state => ({
  drug: state.drugs.drug,
  comments: state.comments.comments,
  errroComments: state.comments.error,
  error: state.sessions.error,
  role: state.sessionReducer.user,
  emptyProfile: state.comments.error
}), {getDrugById, getDrugs, addComment, getComments, changePath})(Instruction);

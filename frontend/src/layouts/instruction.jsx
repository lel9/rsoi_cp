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
import { commentsClean } from "../actions/actionSession";

class Instruction extends Component {
  state = {
    id: '',
    drug: [],
    comments: [],
    textComment: '',
    error: '',
    role: ['USER'],
    emptyProfile: null,
    pagination: 0,
  }

  componentDidMount = () => {
    this.props.changePath(getHistory().location.pathname);

    const id = this.props.match.params.id;
    this.props.getDrugById(id);
    this.props.commentsClean();
    this.props.getComments({id: id, page: 0, size: 20});
    this.setState({
      id,
      role: this.props.role.authorities
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
        comments: this.props.results
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

  handleOnScroll = (e) => {
    const bottom = e.target.scrollHeight - e.target.scrollTop === e.target.clientHeight;
    const { pagination, id } = this.state;
    const { comments } = this.props;
    if (bottom && comments.totalPages !== pagination + 1) {
      this.props.getComments({id: id, page: pagination + 1, size: 20});
      this.setState({
        pagination: pagination + 1,
      })
    }
  }

  render() {
    const {drug, comments, textComment, error,
       emptyProfile } = this.state;
    const { authenticated } = this.props;
    const data = this.getInstruction(drug);
    let isDisabledPost = true;
    if (Object.keys(this.props.role) !== '{}' && this.props.role.authorities !== undefined) {
      isDisabledPost = this.props.role.authorities.includes('ROLE_ADMIN')
          || this.props.role.authorities.includes('ROLE_EXPERT')
    }
    return (
      <div className="instruction">
        <Tree obj={data}/>
        { error === 'Network Error' ?
          <div className="Network-Error allDrugs__Network-Error">
            Сервис лекарственных рекомендаций временно недоступен
          </div>
          :
          <div className="instruction__comments">
            {emptyProfile && emptyProfile.error === 'no_profile' &&
              <div className="emptyField instruction-emptyField">{emptyProfile.error_description}</div>
            }
            <p className="instruction__comments-title">Экспертные рекомендации</p>
            {isDisabledPost && authenticated &&
              <div className="instruction__comments-add">
                <InputField lel9={textComment} className="instruction"
                  func={this.handleOnChange}
                />
                <div className="instruction__comments-add-footer">
                <Button
                  onClick={this.handleOnClick}
                 >
                   Добавить
                 </Button>
                </div>
              </div>
            }
            <div className="instruction__comments-list" onScroll={this.handleOnScroll}>
              <List
                itemLayout="horizontal"
                dataSource={comments}
                renderItem={(item, index) => (
                  <List.Item >
                    <List.Item.Meta
                      title={item.text}
                      description={
                        <div>
                          <p className="title__time">{dayjs.unix(item.date/1000).format('DD/MM/YYYY')}</p>
                        <Link className="title" key={index} to={`/profile/${item.author.id}`}>
                          <span className="title__name">{item.author.displayName}</span>
                        </Link>
                        </div>
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
  errorComments: state.comments.error,
  error: state.sessions.error,
  role: state.sessionReducer.user,
  emptyProfile: state.comments.error,
  results: state.comments.results,
  authenticated: state.sessionReducer.authenticated,
}), {getDrugById, getDrugs, addComment, getComments, changePath, commentsClean})(Instruction);

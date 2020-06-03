import React, { Component, Fragment } from 'react';
import { Link } from "react-router-dom";
import { Breadcrumb } from 'antd';
import { connect } from 'react-redux';


const pages = {
  "select-drug": "Поиск препарата",
  "recommendation": "Результаты поиска",
  "add-drug": "Новый препарат",
  "reception": "Просмотр пациента",
  "all-drugs": "Все препараты",
  "all-patients": "Все пациенты",
  "add-patient": "Новый пациент",
  "instruction": "Просмотр препарата",
  "statistics": "Статистика",
  "profile": "Профиль",
}

class BreadCrumb extends Component {

  getCrumbs = (path, template) => {
    const crumbs = [];
    let positionEnd = 0;
    let link = '';
    path.split('/').map((element) => {
      positionEnd += element.length + 1;
      link = path.slice(0, positionEnd);
      if (element !== '') {
        crumbs.push({
          crumb: template[element],
          link: link,
          disabled: false,
        });
      }
      return null;
    })
    if (crumbs.length && crumbs.slice(-1)[0].crumb === undefined) {
      crumbs.splice(-1,1)
      crumbs.slice(-1)[0].disabled = true;
    }
    return crumbs;
  }

  render () {
    const { path } = this.props;
    let crumbs = path === '' ? [] : this.getCrumbs(path.path, pages)
    return (
      <Fragment>
        <Breadcrumb>
          {crumbs.map((element, index)=> (
            <Breadcrumb.Item key={index}>
              <Link to={element.link} key={index} disabled={element.disabled}>
                {element.crumb}
              </Link>
            </Breadcrumb.Item>
          ))}
        </Breadcrumb>
      </Fragment>
    )
  }
}

export default connect(state => ({
  path: state.path.path
}))(BreadCrumb);

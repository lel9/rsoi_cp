import React, {Component} from 'react';
import getHistory from '../modules/history';

const paragraph = {
  'add-preparate': 'Новый препарат',
  'page2': 'Пациент',
  'page3': 'Название',
  'page4': 'Рекомендации',
  'page5': 'Подбор лекарств'
}

class Header extends Component {
  render() {
    const path = getHistory().location.pathname.split('/')[1];
    return (
      <div className="header">
        {paragraph[path]}
      </div>
    )
  }
}

export default Header;

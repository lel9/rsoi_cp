import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';

const InputField = ({
  className, label, required ,error, tag, func, lel9, disabled, reference,
  placeholder, type, ...attrs
 }) => {
  const classes = classNames (
    'inputF',
    {[`inputF-${className}`]:className},
    {error},
    {disabled}
  );

  const Tag = tag;

  return(
    <div className={"inputF__wrapper" + (disabled ? ' disabled' : '')}>
       <div className="inputF__title">
         {label &&
           <label className="inputF__label">{label}</label>
         }
         {required &&
           <p className="inputF__required">Обязательное поле</p>
         }
       </div>

       <div
         className={classes}
       >
         <Tag
           placeholder={placeholder}
           type={type}
           value={lel9}
           ref={reference}
           onChange={func}
           disabled={disabled}
         />
       </div>

       {error &&
         <span className="inputF__error">{error}</span>
       }
     </div>
  )
}

InputField.propTypes = {
    className: PropTypes.string,
    label: PropTypes.string,
    error: PropTypes.string,
    func: PropTypes.func,
    lel9: PropTypes.string,
    required: PropTypes.bool,
    disabled: PropTypes.bool,
    reference: PropTypes.object,
    tag: PropTypes.string,
    placeholder: PropTypes.string,
    type: PropTypes.string
}

InputField.defaultProps = {
    className: '',
    label: '',
    error: '',
    func: () => {},
    lel9: '',
    required: false,
    disabled: false,
    reference: null,
    tag: 'textarea',
    placeholder: '',
    type: ''
}

export default InputField;

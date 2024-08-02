import React, { useState } from 'react';
import { ReactComponent as MenuDown } from '../../assets/images/down.svg';
import { ReactComponent as MenuUp } from '../../assets/images/up.svg';

interface DropdownProps {
    className?: string;
    options: string[];
    selected?: string;
    onSelect: (value: string) => void;
    iconPosition?: 'right' | 'left';
}

export default function Dropdown({className, options, selected=options[0], onSelect, iconPosition='right'}: DropdownProps) {
  const [isOpen, setIsOpen] = useState(false);
  const handleToggle = () => {
    setIsOpen(!isOpen);
  };

  const handleSelect = (value: string) => {
    onSelect(value);
    setIsOpen(false);
  };

  return (
    <div className={`dropdown ${className}`} style={{textAlign:iconPosition==='left'?'right':'left'}}>
      <div className="dropdown_head" onClick={handleToggle}>
        {iconPosition === 'left' && <div className='dropdown_icon'>
          {isOpen ? <MenuUp/> : <MenuDown/>}
        </div>}
        <div>{selected}</div>
        {iconPosition !== 'left' && <div className='dropdown_icon'>
          {isOpen ? <MenuUp/> : <MenuDown/>}
        </div>}
      </div>
      {isOpen && (
        <ul className="dropdown_list">
          {options.filter(option=>option !== selected).map((option,idx) => (
            <li
              key={idx}
              className="dropdown_list_item"
              onClick={() => handleSelect(option)}
            >
              {option}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
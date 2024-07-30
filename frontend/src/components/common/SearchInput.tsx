import React from 'react'
import { ReactComponent as Search } from '../../assets/images/search.svg';

interface InputProps{
  placeholder?: string;
}

export default function SearchInput({placeholder} : InputProps) {
  return (
    <div className="search-container">
        <span><input type="text" placeholder={placeholder}/>
        <div className='search-icon'><Search/></div>
        </span>
      </div>
  )
}

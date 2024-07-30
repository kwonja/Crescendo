import React from 'react'
import { ReactComponent as Search } from '../../assets/images/search.svg';
export default function SearchInput() {
  return (
    <div className="search-container">
        <span><input type="text" placeholder="친구를 검색하세요"/>
        <div className='search-icon'><Search/></div>
        </span>
      </div>
  )
}

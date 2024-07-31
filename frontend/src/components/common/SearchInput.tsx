import React from 'react';
import { ReactComponent as Search } from '../../assets/images/search.svg';

interface InputProps {
  placeholder?: string;
  value?: string;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onSearch?: () => void;
}

export default function SearchInput({ placeholder, value, onChange, onSearch }: InputProps) {
  return (
    <div className="search-container">
      <span>
        <input
          type="text"
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className="search-input"
        />
        <div className="search-icon" onClick={onSearch}>
          <Search />
        </div>
      </span>
    </div>
  );
}

import React from 'react';
import '../../scss/components/community/_dropdownmenu.scss';

interface DropdownMenuProps {
  onEdit: () => void;
  onDelete: () => void;
  onClose: () => void;
}

export default function DropdownMenu({ onEdit, onDelete, onClose }: DropdownMenuProps) {
  return (
    <ul className="feed-detail-menu">
      <li onClick={onEdit}>수정</li>
      <li onClick={onDelete}>삭제</li> 
      <li className="close-button" onClick={onClose}>닫기</li>
    </ul>
  );
}

import React, { useEffect, useRef } from 'react';

interface ActionMenuProps {
  onClose: () => void;
  onDeleteAction: ()=>void; 
}

export default function ActionMenu({ onClose, onDeleteAction }: ActionMenuProps) {
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        onClose();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [menuRef, onClose]);

  return (
    <div
      ref={menuRef}
      className="action-menu"
    >
      <div className="action-menu-item" onClick={() => onDeleteAction()}>
        삭제
      </div>
    </div>
  )
}

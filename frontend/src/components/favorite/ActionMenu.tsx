import React, { useEffect, useRef, useState } from 'react';
import { deleteFavoriteRankAPI } from '../../apis/favorite';
import CommonModal from '../common/CommonModal';
import ReactDOM from 'react-dom';

interface ActionMenuProps {
    favoriteRankId:number
    position: {
        top: number;
        left: number;
    };
    onClose: () => void;
}

export default function ActionMenu({favoriteRankId, position, onClose }: ActionMenuProps) {
    const menuRef = useRef<HTMLDivElement>(null);
    const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false);

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

    const onDelete = async () => {
        try {
            await deleteFavoriteRankAPI(favoriteRankId);
            alert("성공적으로 삭제했습니다.");
        }
        catch (error:any) {
            if (error.response && error.response.data) {
                alert(error.response.data);
                return;
            }
            else {
                alert('삭제에 실패했습니다.');
            }
        }
        finally {
            onClose();
        }
    }

    return ReactDOM.createPortal((
        <div
            ref={menuRef}
            className='action-menu'
            style={{
                top: `${position.top}px`,
                left: `${position.left}px`,
            }}
        >
            <div className='action-menu-item' onClick={()=>setShowDeleteModal(true)}>삭제</div>
            {showDeleteModal && <CommonModal 
                                    title="삭제 확인" 
                                    msg= "정말로 삭제하시겠습니까?" 
                                    onClose={()=>setShowDeleteModal(false)} 
                                    onConfirm={onDelete}
                                    closeOnOutsideClick= {true}
                                    />}
        </div>
    ), document.body);
}

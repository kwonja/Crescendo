import React, { useState } from "react";
import {ReactComponent as UserDefault} from "../../assets/images/UserProfile/reduser.svg"; 

interface UserProfileProps {
    className?: string;
    userId: number;
    userNickname: string;
    userProfilePath: string | null;
    date?: string;
}

export default function UserProfile({ className, userId, userNickname, userProfilePath, date }: UserProfileProps) {
    const [notFoundImgError, setNotFoundImgError] = useState<boolean>(false);
    // userProfilePath = "";
    return <div className={`flex ${className}`}>
        <div className='mr-5'>
            {notFoundImgError?<UserDefault />:<img className="w-12 h-12 rounded-full" src={userProfilePath || ''} alt="이미지없음에러" onError={()=>setNotFoundImgError(true)}/>}
        </div>
        <div className="flex-col">
            <div>{userNickname}</div>
            {date&&<div>{date}</div>}
        </div>
    </div>
}

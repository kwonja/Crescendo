import React, { useEffect } from 'react';
import ChallengeItem from './ChallengeItem';
import { useAppDispatch, useAppSelector } from '../../store/hooks/hook';
import { getChallengeList } from '../../features/challenge/challengeSlice';

export default function ChallengeList() {

  const dispatch = useAppDispatch();
  const {currentPage,challengeLists} = useAppSelector( state => state.challenge)

  useEffect( ()=>{
    dispatch(getChallengeList({page : currentPage, size : 10, title : '', sortBy : ''}));
  },[dispatch,currentPage])
  
  return (
    <div className="challenge-list">
      <div className="title">SHOW YOUR CHALLENGE!!</div>
      <div className="flex flex-wrap gap-10 mx-auto w-9/12 justify-between">
       {challengeLists.map( (challenge)=>(
         <ChallengeItem Challenge={challenge} key={challenge.challengeId}/>
       )
      )
       }
      </div>
    </div>
  );
}

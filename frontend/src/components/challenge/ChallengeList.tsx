import React from 'react'
import ChallengeItem from './ChallengeItem'

export default function ChallengeList() {
  return (
    <div className='challenge-list'>
        <div className='title'>SHOW YOUR CHALLENGE!!</div>
        <div className='flex flex-wrap mx-auto w-9/12 justify-between'>
        <ChallengeItem/>
        <ChallengeItem/>
        </div>
    </div>
  )
}

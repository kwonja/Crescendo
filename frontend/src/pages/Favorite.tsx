import BestPhotoSlide from "../components/favorite/BestPhotoSlide";
import Dropdown from "../components/common/Dropdown";
import { useEffect, useState } from "react";
import FavoriteRankList from "../components/favorite/FavoriteRankList";

interface idolGroupInfo {
  groupId: number;
  groupName: string;
};

interface idolInfo {
  idolId: number;
  idolName: string;
};

const idolGroupTmp:idolGroupInfo[] = [
  {
    groupId: 1,
    groupName: 'NewJeans'
  },
  {
    groupId: 2,
    groupName: 'BTS'
  },
];

const idolTmp1:idolInfo[] = [
  {
    idolId: 1,
    idolName: '민지'
  },
  {
    idolId: 2,
    idolName: '하니'
  },
  {
    idolId: 3,
    idolName: '다니엘'
  },
  {
    idolId: 4,
    idolName: '해린'
  },
  {
    idolId: 5,
    idolName: '혜인'
  },
];

const idolTmp2:idolInfo[] = [
  {
    idolId: 6,
    idolName: '진'
  },
  {
    idolId: 7,
    idolName: '슈가 '
  },
  {
    idolId: 8,
    idolName: '제이홉'
  },
  {
    idolId: 9,
    idolName: 'RM'
  },
  {
    idolId: 10,
    idolName: '지민'
  },
  {
    idolId: 11,
    idolName: '뷔'
  },
  {
    idolId: 12,
    idolName: '정국'
  },
];


export default function Favorite() {

  const [idolGroupOption, setIdolGroupOption] = useState<string>('그룹');
  const [idolGroupList, setIdolGroupList] = useState<idolGroupInfo[]>([]);
  const [idolOption, setIdolOption] = useState<string>('멤버');
  const [idolList, setIdolList] = useState<idolInfo[]>([]);
  const [sortOption, setSortOption] = useState<string>('정렬');

  //그룹 리스트 가져오기
  useEffect(()=> {
    const getIdolGroupList = async () => {
      setIdolGroupList(idolGroupTmp);
    }
    getIdolGroupList();
  }, [])

  //멤버 리스트 가져오기
  useEffect(()=>{
    if (idolGroupOption === '그룹') {
      setIdolList([]);
      return;
    }
    setIdolOption('멤버');
    const selectedGroup = idolGroupList.find((group)=> group.groupName===idolGroupOption)
    if (!selectedGroup) throw new Error('not found idol group');
    const getIdolGroupList = async (groupId:number) => {
      groupId === 1?setIdolList(idolTmp1):setIdolList(idolTmp2);
    }
    getIdolGroupList(selectedGroup.groupId);
  }, [idolGroupOption, idolGroupList])


  return <div className="favorite">
      <div className="bestphotos_container">
        <BestPhotoSlide/>
      </div>
      <div className="favorite_container">
        <div className="favorite_title">
          최애 자랑 갤러리
        </div>
        <div className="conditionbar">
          <div className="filter">
            <div className="menu">
              <Dropdown
                className="group text"
                selected={idolGroupOption}
                options={idolGroupList.map(group=>group.groupName)}
                onSelect={(selected)=>setIdolGroupOption(selected)}
              />
            </div>
            <div className = "menu">
              <Dropdown
                className="member text"
                selected={idolOption}
                options={idolList.map(idol=>idol.idolName)}
                onSelect={(selected)=>setIdolOption(selected)}
              />
            </div>
          </div>
          <div className = "sort menu">
            <Dropdown
              className="sort text"
              selected={sortOption}
              options={["최신순", "좋아요순"]}
              onSelect={(selected)=>setSortOption(selected)}
              iconPosition="left"
            />
          </div>
        </div>
        <FavoriteRankList />
      </div>
    </div>

}

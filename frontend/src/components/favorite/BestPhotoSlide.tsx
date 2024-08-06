import { useEffect, useState } from "react"


type bestPhotoInfo = {
        idolId: number, // 아이돌 ID
        idolGroupName: string, // 그룹 이름 
        idolName: string, // 아이돌 이름
        favoriteIdolImagePath: string // 아이돌 이미지 경로
    }

export default function BestPhotoSlide () {

    //임시 데이터
    const tmpList:bestPhotoInfo[] = [
        {
            idolId: 1,
            idolGroupName: "NewJeans",
            idolName: "민지",
            favoriteIdolImagePath: 'https://i.ibb.co/JFqgZck/minji.jpg'
        },
        {
            idolId: 2,
            idolGroupName: "NewJeans",
            idolName: "해린",
            favoriteIdolImagePath: 'https://i.ibb.co/qnNRwbY/haerin.jpg'
        },
        {
            idolId: 3,
            idolGroupName: "NewJeans",
            idolName: "하니",
            favoriteIdolImagePath: 'https://i.ibb.co/FbVfHxH/hani.jpg'
        },
    ]
    
    const [bestPhotoList, setBestPhotoList] = useState<bestPhotoInfo[]>([]);
    const [animationDuration, setAnimationDuration] = useState<number>(10);
    const ANIMATION_SPEED = 150;

    useEffect(()=>{
        const getBestPhotoList = async () => {
            const tmp = [...tmpList, ...tmpList];
            setBestPhotoList(tmp);
        }
        getBestPhotoList();
    }, [])

    useEffect(()=>{
        if (bestPhotoList.length === 0) return;
        let totalWidth = bestPhotoList.length * 450;
        setAnimationDuration(totalWidth/ANIMATION_SPEED);
    }, [bestPhotoList])

    return (
        <div className="bestphotoslide_container">
          <div className='bestphotoslide_title'>Best Photos</div>
          <div className="bestphotoslide_track_container">
            <div className="bestphotoslide_track" style={{ animation: `slide ${animationDuration}s linear infinite` }}>
                {bestPhotoList.map((bestPhoto,idx)=>(
                <div className='bestphotoslide_card' key={idx}>
                    <img src={bestPhoto.favoriteIdolImagePath} alt={bestPhoto.idolName} />
                    <div className='bestphotoslide_idolname'>{bestPhoto.idolGroupName}-{bestPhoto.idolName}</div>
                </div>
                ))}
            </div>
          </div>
        </div>
    )
}
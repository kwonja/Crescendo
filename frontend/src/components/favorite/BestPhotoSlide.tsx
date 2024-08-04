

type bestPhotoInfo = {
        idolId: number, // 아이돌 ID
        idolGroupName: string, // 그룹 이름 
        idolName: string, // 아이돌 이름
        favoriteIdolImagePath: string // 아이돌 이미지 경로
    }

export default function BestPhotoSlide () {

    //임시 데이터
    const bestPhotoList:bestPhotoInfo[] = [
        {
            idolId: 1,
            idolGroupName: "NewJeans",
            idolName: "민지",
            favoriteIdolImagePath: ''
        },
        {
            idolId: 2,
            idolGroupName: "NewJeans",
            idolName: "해린",
            favoriteIdolImagePath: ''
        },
        {
            idolId: 3,
            idolGroupName: "NewJeans",
            idolName: "하니",
            favoriteIdolImagePath: ''
        }
    ]

    return (
        <div>
            {bestPhotoList.map((bestPhoto,idx)=>(<div key={idx}>
                <img src={bestPhoto.favoriteIdolImagePath} alt={bestPhoto.idolName} />
                <div>{bestPhoto.idolGroupName}-{bestPhoto.idolName}</div>
            </div>))}
        </div>
    )
}
export interface FeedData {
    userId: number; // 작성자 userId
    profileImagePath: string; // 작성자 프로필 사진 경로
    nickname: string; // 작성자 닉네임
    createdAt: string; // 피드 생성일 (ISO 8601 문자열)
    updatedAt: string; // 피드 수정일 (ISO 8601 문자열)
    likeCnt: number; // 좋아요 수
    isLike: boolean; // 내가 좋아요 했나 (JsonIgnore)
    feedImagePathList: string[]; // 피드 이미지 경로 리스트
    content: string; // 피드 본문
    commentCnt: number; // 댓글 수
    tagList: string[]; // 태그 리스트
}
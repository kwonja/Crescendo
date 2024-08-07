export interface user {
  userId: number;
  nickname: string;
  userProfilePath: string;
}

export interface UserInfo{
  profilePath : string | null
  nickname : string;
  introduction : string | null;
  followingNum : number;
  followerNum : number;
  isFollowing : boolean;
  favoriteImagePath : string | null;
}
export interface communityInfo {
  idolGroupId: number;
  name: string;
  profile: string;
}

export interface communityListResponse {
  content: communityInfo[];
  pageable: {
    pageNumber: number; //현재 페이지 번호
    pageSize: number;
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    offset: number;
    unpaged: boolean;
    paged: boolean;
  };
  totalPages: number;
  totalElements: number;
  first: boolean;
  last: boolean;
  size: number;
  number: number; // 현재 페이지 번호
  sort: {
    empty: boolean;
    unsorted: boolean;
    sorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

export interface communityDetailInfo {
  idolGroupId: number;
  name: string;
  peopleNum: number;
  introduction: string;
  profile: string;
  banner: string;
  isFavorite: boolean;
};

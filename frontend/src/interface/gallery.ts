export interface GalleryInfo {
    fanArtId: number;
    goodsId: number;
    userId: number;
    profileImagePath: string;
    nickname: string;
    likeCnt: number;
    fanArtImagePathList: string[];
    goodsImagePathList: string[];
    content: string;
    commentCnt: number;
    createdAt: string;
    lastModified: string;
    title: string;
    like: boolean;
    isLike: boolean;
}

export interface GalleryListResponse {
    content: GalleryInfo[];
    pageable: {
    // 페이지와 관련된 페이징 정보
    sort: {
        sorted: boolean; // 데이터를 정렬했는지 여부
        unsorted: boolean; // 데이터를 정렬하지 않았는지 여부
        empty: boolean; // 정렬 기준이 비어 있는지 여부
    };
    pageNumber: number; // 현재 페이지 번호 (0부터 시작)
    pageSize: number; // 한 페이지에 포함되는 요소의 수
    offset: number; // 페이지의 시작점에서의 오프셋 (pageNumber * pageSize)
    paged: boolean; // 페이징이 적용되었는지 여부
    unpaged: boolean; // 페이징이 적용되지 않았는지 여부
    };
    totalPages: number; // 총 페이지 수
    totalElements: number; // 총 요소의 수 (전체 데이터 개수)
    numberOfElements: number; // 현재 페이지에 포함된 요소의 수
    first: boolean; // 첫 번째 페이지인지 여부
    last: boolean; // 마지막 페이지인지 여부
    size: number; // 한 페이지에 포함되는 요소의 수 (pageSize와 동일)
    number: number; // 현재 페이지 번호 (pageNumber와 동일)
    empty: boolean; // 현재 페이지가 비어 있는지 여부
}

export interface getGalleryListParams {
    'idol-group-id': number;
    page: number;
    size: number;
    title: string;
    nickname: string;
    content: string;
    sortByFollowed: boolean;
    sortByLiked: boolean;
}
export interface favoriteRankInfo {
    writerId: number,
    writerNickname: string,
	writerProfilePath: string | null,
    favoriteRankId: number,
    favoriteIdolImagePath: string,
    likeCnt: number,
    isLike: boolean,
    createdAt: string
}

export interface favoriteRankListResponse {
    content: favoriteRankInfo[],
    pageable:
    {
		sort: {
			sorted: boolean,
			unsorted: boolean,
			empty: boolean
		},
		pageNumber: number,
		pageSize: number,
		offset: number,
		paged: boolean,
		unpaged: boolean
	},
	totalPages: number,
	totalElements: number,
	numberOfElements: number,
	first: boolean,
	last: boolean,
	size: number,
	number: number,
	empty: boolean
}
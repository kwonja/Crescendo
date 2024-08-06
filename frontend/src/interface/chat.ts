export interface Chat {
  dmGroupId: number;
  content: string;
  writerId: number;
}

export interface ChatRoom {
  dmGroupId: number;
  opponentId: number;
  opponentProfilePath: string;
  opponentNickName: string;
  lastChatting: string;
  lastChattingTime: string;
}
export interface Message {
  dmMessageId: number;
  message: string;
  createdAt: string;
  writerId: number;
  writerNickname: string;
  writerProfilePath: string;
}

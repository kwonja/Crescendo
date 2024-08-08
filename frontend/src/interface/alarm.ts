export interface Alarm{
    alarmId: number;
    alarmChannelId: number;
    relatedId: number;
    content: string;
    isRead: boolean;
    createdAt: string;
}
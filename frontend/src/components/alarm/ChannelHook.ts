export const Channel = (alarmChannelId: number) => {
  if (alarmChannelId === 1) {
    return '[팔로우]';
  } else if (alarmChannelId === 2) return '[피드]';
};

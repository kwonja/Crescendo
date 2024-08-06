const DeBounce = (callback: (query: string) => Promise<void>, delay: number) => {
  let timer: ReturnType<typeof setTimeout>;

  return function (query: string) {
    clearTimeout(timer);
    timer = setTimeout(() => {
      console.log(query);
      callback(query);
    }, delay);
  };
};
export default DeBounce;

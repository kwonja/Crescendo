import React, { useCallback, useEffect, useRef, useState } from 'react';
import { ScrollTo } from '../components/main/hooks/Scroll';
import Section1 from '../components/main/Section1';
import Section2 from '../components/main/Section2';
import Section3 from '../components/main/Section3';
import Section4 from '../components/main/Section4';
export default function Main() {
  const [page, setPage] = useState(1);
  const lastPage = useRef(4);
  const htmlRef = useRef(document.documentElement);

  //어짜피 계속 렌더링이 되야하는데 useCallback으로 감싸는 이유가뭐지
  //해당 화면에서 page가 아닌 state가 변경되어 랜더링이 발생할 수 있다.
  //React가 useEffect에 의존성을 가지고 있는 함수가 무분별하게 리렌더링되어 useffect가 다시 랜더링되는것을 막히 위해서 에러를 표출한다.
  //일종의 버그를 막기 위한 방어책이라고 생각한다.
  const handleWheel = useCallback(
    (e: WheelEvent) => {
      const $html = htmlRef.current;
      if ($html.classList.contains('animated')) return;

      if (e.deltaY > 0) {
        if (page < lastPage.current) {
          setPage(prevPage => prevPage + 1);
        }
      } else if (e.deltaY < 0) {
        if (page > 1) {
          setPage(prevPage => prevPage - 1);
        }
      }
    },
    [page],
  );

  useEffect(() => {
    const $html = htmlRef.current;
    const windowHeight = window.innerHeight;
    const posTop = (page - 1) * windowHeight;

    $html.classList.add('animated');
    ScrollTo($html, posTop, 600); // Adjust the animation duration as needed

    document.addEventListener('wheel', handleWheel);
    return () => {
      document.removeEventListener('wheel', handleWheel);
    };
  }, [page, handleWheel]);
  return (
    <>
      <Section1 />
      <Section2 />
      <Section3 />
      <Section4 />
    </>
  );
}

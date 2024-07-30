import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux'; // Redux의 useDispatch와 useSelector 훅 임포트
import { RootState, AppDispatch } from '../store/store'; // Redux 스토어의 타입 임포트
import { login } from '../features/auth/authSlice'; // 로그인 액션 임포트
import { isValidEmail } from '../utils/EmailValidation'; // 이메일 유효성 검사 함수 임포트
import { isValidPassword } from '../utils/PasswordValidation'; // 비밀번호 유효성 검사 함수 임포트
import { ReactComponent as Visualization } from '../assets/images/visualization.svg'; // 비밀번호 시각화 아이콘 임포트
import '../scss/page/_login.scss';

const Login: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>(); // Redux 디스패치를 사용하여 액션 전송
  const { error, loading } = useSelector((state: RootState) => state.auth); // Redux 스토어에서 인증 상태 선택
  const [email, setEmail] = useState(''); // 이메일 상태 관리
  const [password, setPassword] = useState(''); // 비밀번호 상태 관리
  const [showPassword, setShowPassword] = useState(false); // 비밀번호 시각화 상태 관리
  const [rememberMe, setRememberMe] = useState(false); // '아이디 저장' 체크박스 상태 관리
  const [autoLogin, setAutoLogin] = useState(false); // '자동 로그인' 체크박스 상태 관리
  const [errorMessage, setErrorMessage] = useState(''); // 에러 메시지 상태 관리

  // 이메일 입력이 변경될 때 호출되는 함수
  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value); // 이메일 상태를 업데이트
  };

  // 비밀번호 입력이 변경될 때 호출되는 함수
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value); // 비밀번호 상태를 업데이트
  };

  // 로그인 폼이 제출될 때 호출되는 함수
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출의 기본 동작 막음
    if (!isValidEmail(email)) {
      setErrorMessage('유효한 이메일 주소를 입력하세요.'); // 이메일이 유효하지 않을 시 에러 메시지
      return;
    }
    if (!isValidPassword(password)) {
      setErrorMessage(
        '비밀번호는 최소 8자 이상, 32자 이하이며, 영어, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.', // 비밀번호가 유효하지 않으면 에러 메시지
      );
      return;
    }
    setErrorMessage(''); // 에러 메시지를 초기화
    dispatch(login({ email, password })); // 로그인 액션 디스패치
  };

  return (
    <div className="login-container">
      {' '}
      {/* 로그인 페이지 컨테이너 */}
      <h1 className="login-title">로그인</h1> {/* 타이틀 */}
      <div className="login-wrapper">
        {' '}
        <form onSubmit={handleSubmit} className="login-form">
          {' '}
          {/* 로그인 폼 */}
          <div className="form-group">
            {' '}
            {/* 이메일 입력 그룹 */}
            <input
              type="email"
              id="email"
              placeholder="E-Mail"
              value={email}
              onChange={handleEmailChange}
              maxLength={254}
              required
            />
          </div>
          <div className="form-group password-group">
            {' '}
            {/* 비밀번호 입력 그룹 */}
            <input
              type={showPassword ? 'text' : 'password'}
              id="password"
              placeholder="비밀번호"
              value={password}
              onChange={handlePasswordChange}
              minLength={8}
              maxLength={32}
              required
            />
            <Visualization
              onClick={() => setShowPassword(!showPassword)}
              className="toggle-password"
            >
              {showPassword ? '숨기기' : '보이기'} {/* 비밀번호 시각화 토글 버튼 */}
            </Visualization>
            <button
              type="button"
              onClick={() => alert('비밀번호 찾기 기능을 구현하세요')}
              className="forgot-password"
            >
              비밀번호를 잊으셨나요? {/* 비밀번호 찾기 버튼 */}
            </button>
          </div>
          <div className="form-actions">
            {' '}
            {/* 폼 액션 그룹 */}
            <div className="checkbox-group">
              {' '}
              {/* 체크박스 그룹 */}
              <div className="form-checkbox">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={() => setRememberMe(!rememberMe)}
                />
                <div className="custom-checkbox"></div>
                <label>아이디 저장</label>
              </div>
              <div className="form-checkbox">
                <input
                  type="checkbox"
                  checked={autoLogin}
                  onChange={() => setAutoLogin(!autoLogin)}
                />
                <div className="custom-checkbox"></div>
                <label>자동 로그인</label>
              </div>
            </div>
            <div className="button-group">
              {' '}
              {/* 버튼 그룹 */}
              <div className="signup-link">
                <button type="button" onClick={() => alert('회원가입 페이지로 이동')}>
                  회원가입
                </button>
              </div>
              <button type="submit" className="submit-button" disabled={loading}>
                {loading ? '로그인 중...' : '로그인'} {/* 로그인 버튼 */}
              </button>
            </div>
          </div>
          {error && <p className="error-message">{error}</p>} {/* 서버 에러 메시지 */}
          {errorMessage && <p className="error-message">{errorMessage}</p>}{' '}
          {/* 클라이언트 에러 메시지 */}
        </form>
      </div>
    </div>
  );
};

export default Login;

import React, { useState } from 'react';
import { isValidEmail } from '../utils/EmailValidation'; // 이메일 유효성 검사 함수 임포트
import { isValidPassword } from '../utils/PasswordValidation'; // 비밀번호 유효성 검사 함수 임포트
import { ReactComponent as Visualization } from '../assets/images/visualization.svg'; // 비밀번호 시각화 아이콘 임포트
import '../scss/page/_signup.scss'; // 새로운 회원가입 페이지 스타일

const SignUp: React.FC = () => {
  const [email, setEmail] = useState(''); // 이메일 상태 관리
  const [verificationCode, setVerificationCode] = useState(''); // 인증번호 상태 관리
  const [codeVerified, setCodeVerified] = useState(false); // 인증번호 검증 상태 관리
  const [password, setPassword] = useState(''); // 비밀번호 상태 관리
  const [confirmPassword, setConfirmPassword] = useState(''); // 비밀번호 확인 상태 관리
  const [nickname, setNickname] = useState(''); // 닉네임 상태 관리
  const [showPassword, setShowPassword] = useState(false); // 비밀번호 시각화 상태 관리
  const [showConfirmPassword, setShowConfirmPassword] = useState(false); // 비밀번호 확인 시각화 상태 관리
  const [termsAccepted, setTermsAccepted] = useState(false); // 약관 동의 상태 관리
  const [verificationButtonText, setVerificationButtonText] = useState('인증번호 전송'); // 인증번호 버튼 텍스트 관리
  const [fieldErrors, setFieldErrors] = useState({
    email: '',
    verificationCode: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    termsAccepted: '',
  });

  // 이메일 입력이 변경될 때 호출되는 함수
  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  // 인증번호 입력이 변경될 때 호출되는 함수
  const handleVerificationCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVerificationCode(e.target.value);
  };

  // 비밀번호 입력이 변경될 때 호출되는 함수
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  // 비밀번호 확인 입력이 변경될 때 호출되는 함수
  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(e.target.value);
  };

  // 닉네임 입력이 변경될 때 호출되는 함수
  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  };

  // 인증번호 전송 버튼 클릭 시 호출되는 함수
  const handleSendVerificationCode = () => {
    if (!isValidEmail(email)) {
      setFieldErrors(prev => ({ ...prev, email: '유효한 이메일 주소를 입력하세요.' }));
      return;
    }
    setFieldErrors(prev => ({ ...prev, email: '' }));
    setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 전송되었습니다.' }));
    setVerificationButtonText('재전송'); // 버튼 텍스트 변경
  };

  // 인증하기 버튼 클릭 시 호출되는 함수
  const handleVerifyCode = () => {
    if (verificationCode === '123456') {
      // 테스트용 인증번호 123456
      setCodeVerified(true);
      setFieldErrors(prev => ({ ...prev, verificationCode: '' }));
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증이 완료되었습니다.' }));
    } else {
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 올바르지 않습니다.' }));
    }
  };

  // 회원가입 폼이 제출될 때 호출되는 함수
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    let errors = {
      email: '',
      verificationCode: '',
      password: '',
      confirmPassword: '',
      nickname: '',
      termsAccepted: '',
    };
    let hasError = false;

    if (!codeVerified) {
      errors.verificationCode = '이메일 인증을 완료해주세요.';
      hasError = true;
    }

    if (!isValidEmail(email)) {
      errors.email = '유효한 이메일 주소를 입력하세요.';
      hasError = true;
    }

    if (!isValidPassword(password)) {
      errors.password =
        '비밀번호는 최소 8자 이상, 32자 이하이며, 영어, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.';
      hasError = true;
    }

    if (password !== confirmPassword) {
      errors.confirmPassword = '비밀번호가 일치하지 않습니다.';
      hasError = true;
    }

    if (!termsAccepted) {
      errors.termsAccepted = '약관에 동의해주세요.';
      hasError = true;
    }

    if (hasError) {
      setFieldErrors(errors);
      return;
    }

    setFieldErrors(errors);
    // 회원가입 로직을 여기에 추가
    console.log({ email, password, nickname });
  };

  return (
    <div className="signup-container">
      {' '}
      {/* 회원가입 페이지 컨테이너 */}
      <h1 className="signup-title">회원가입</h1>
      <div className="signup-wrapper">
        <form onSubmit={handleSubmit} className="signup-form">
          {' '}
          {/* 회원가입 폼 */}
          <div className="form-group">
            {' '}
            {/* 이메일 입력 그룹 */}
            <div className="input-group">
              <input
                type="email"
                id="email"
                placeholder="E-Mail 입력"
                value={email}
                onChange={handleEmailChange}
                maxLength={254}
                required
              />
              <button
                type="button"
                onClick={handleSendVerificationCode}
                className="verification-button"
              >
                {verificationButtonText}
              </button>
            </div>
            {fieldErrors.email && <p className="error-message">{fieldErrors.email}</p>}
          </div>
          <div className="form-group">
            {' '}
            {/* 인증번호 입력 그룹 */}
            <div className="input-group">
              <input
                type="text"
                id="verificationCode"
                placeholder="인증번호 입력"
                value={verificationCode}
                onChange={handleVerificationCodeChange}
                maxLength={6}
                required
              />
              <button type="button" onClick={handleVerifyCode} className="verification-button">
                인증하기
              </button>
            </div>
            {fieldErrors.verificationCode && (
              <p className="error-message">{fieldErrors.verificationCode}</p>
            )}
          </div>
          <div className="password-group">
            {' '}
            {/* 비밀번호 입력 그룹 */}
            <div className="input-group">
              <input
                type={showPassword ? 'text' : 'password'}
                id="password"
                placeholder="비밀번호 입력"
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
                {showPassword ? '숨기기' : '보이기'}
              </Visualization>
            </div>
            {fieldErrors.password && <p className="error-message">{fieldErrors.password}</p>}
          </div>
          <div className="password-group">
            {' '}
            {/* 비밀번호 확인 그룹 */}
            <div className="input-group">
              <input
                type={showConfirmPassword ? 'text' : 'password'}
                id="confirmPassword"
                placeholder="비밀번호 확인"
                value={confirmPassword}
                onChange={handleConfirmPasswordChange}
                minLength={8}
                maxLength={32}
                required
              />
              <Visualization
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="toggle-password"
              >
                {showConfirmPassword ? '숨기기' : '보이기'}
              </Visualization>
            </div>
            {fieldErrors.confirmPassword && (
              <p className="error-message">{fieldErrors.confirmPassword}</p>
            )}
          </div>
          <div className="form-group">
            {' '}
            {/* 닉네임 입력 그룹 */}
            <div className="input-group">
              <input
                type="text"
                id="nickname"
                placeholder="닉네임 입력"
                value={nickname}
                onChange={handleNicknameChange}
                maxLength={30}
                required
              />
            </div>
            {fieldErrors.nickname && <p className="error-message">{fieldErrors.nickname}</p>}
          </div>
          <div className="form-actions">
            {' '}
            {/* 폼 액션 그룹 */}
            <div className="form-checkbox">
              {' '}
              {/* 약관 동의 체크박스 */}
              <input
                type="checkbox"
                checked={termsAccepted}
                onChange={() => setTermsAccepted(!termsAccepted)}
              />
              <div className="custom-checkbox"></div>
              <label>약관에 동의합니다</label>
            </div>
            {fieldErrors.termsAccepted && (
              <p className="error-message">{fieldErrors.termsAccepted}</p>
            )}
            <div className="button-group">
              {' '}
              {/* 버튼 그룹 */}
              <button type="submit" className="submit-button">
                회원가입
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUp;

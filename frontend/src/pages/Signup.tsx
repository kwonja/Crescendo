import React, { useState, useEffect } from 'react';
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
  const [isVerificationButtonDisabled, setIsVerificationButtonDisabled] = useState(false); // 인증번호 버튼 비활성화 상태
  const [verificationCountdown, setVerificationCountdown] = useState(0); // 인증번호 카운트다운 타이머
  const [isFirstVerification, setIsFirstVerification] = useState(true); // 첫 번째 인증 시도 여부
  const [isEmailLocked, setIsEmailLocked] = useState(false); // 이메일 입력 잠금 상태
  const [isVerificationCodeLocked, setIsVerificationCodeLocked] = useState(false); // 인증번호 입력 잠금 상태
  const [fieldErrors, setFieldErrors] = useState({
    email: '',
    verificationCode: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    termsAccepted: '',
  });

  useEffect(() => {
    let timer: ReturnType<typeof setInterval>;
    if (verificationCountdown > 0) {
      timer = setInterval(() => {
        setVerificationCountdown(prev => prev - 1);
      }, 1000);
    } else {
      setIsVerificationButtonDisabled(false);
    }

    return () => {
      if (timer) clearInterval(timer);
    };
  }, [verificationCountdown]);

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!isEmailLocked) {
      setEmail(e.target.value);
    }
  };

  const handleVerificationCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!isVerificationCodeLocked) {
      setVerificationCode(e.target.value);
    }
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(e.target.value);
  };

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  };

  const handleSendVerificationCode = () => {
    if (!isValidEmail(email)) {
      setFieldErrors(prev => ({ ...prev, email: '유효한 이메일 형식을 입력해 주세요' }));
      return;
    }
    setFieldErrors(prev => ({ ...prev, email: '' }));
    setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 전송되었습니다.' }));
    setIsVerificationButtonDisabled(true);
    setVerificationCountdown(15);
    setIsFirstVerification(false); // 첫 번째 시도 이후 false로 설정
    setIsEmailLocked(true); // 이메일 입력 잠금
  };

  const handleVerifyCode = () => {
    if (verificationCode === '123456') {
      setCodeVerified(true);
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증이 완료되었습니다.' }));
      setIsVerificationCodeLocked(true); // 인증번호 입력 잠금
    } else {
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 틀립니다.' }));
    }
  };

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
    console.log({ email, password, nickname });
  };

  return (
    <div className="signup-container">
      <h1 className="signup-title">회원가입</h1>
      <div className="signup-wrapper">
        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group">
            <div className="input-group">
              <input
                type="email"
                id="email"
                placeholder="E-Mail 입력"
                value={email}
                onChange={handleEmailChange}
                maxLength={254}
                required
                disabled={isEmailLocked}
              />
              {isVerificationButtonDisabled ? (
                <span className="verification-timer">{verificationCountdown}초</span>
              ) : (
                <button
                  type="button"
                  onClick={handleSendVerificationCode}
                  className="verification-button"
                  style={{ visibility: codeVerified ? 'hidden' : 'visible' }}
                >
                  {isFirstVerification ? '인증번호 전송' : '재전송'}
                </button>
              )}
            </div>
            <p className={`error-message ${fieldErrors.email ? 'visible' : ''}`}>
              {fieldErrors.email}
            </p>
          </div>
          <div className="form-group">
            <div className="input-group">
              <input
                type="text"
                id="verificationCode"
                placeholder="인증번호 입력"
                value={verificationCode}
                onChange={handleVerificationCodeChange}
                maxLength={6}
                required
                disabled={isVerificationCodeLocked}
              />
              <button
                type="button"
                onClick={handleVerifyCode}
                className="verification-button"
                style={{ visibility: codeVerified ? 'hidden' : 'visible' }}
              >
                인증하기
              </button>
            </div>
            <p className={`error-message ${fieldErrors.verificationCode ? 'visible' : ''}`}>
              {fieldErrors.verificationCode}
            </p>
          </div>
          <div className="password-group">
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
            <p className={`error-message ${fieldErrors.password ? 'visible' : ''}`}>
              {fieldErrors.password}
            </p>
          </div>
          <div className="password-group">
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
            <p className={`error-message ${fieldErrors.confirmPassword ? 'visible' : ''}`}>
              {fieldErrors.confirmPassword}
            </p>
          </div>
          <div className="form-group">
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
            <p className={`error-message ${fieldErrors.nickname ? 'visible' : ''}`}>
              {fieldErrors.nickname}
            </p>
          </div>
          <div className="form-actions">
            <div className="form-checkbox">
              <input
                type="checkbox"
                checked={termsAccepted}
                onChange={() => setTermsAccepted(!termsAccepted)}
              />
              <div className="custom-checkbox"></div>
              <label>약관에 동의합니다</label>
            </div>
            <div className="button-group">
              <button type="submit" className="submit-button">
                회원가입
              </button>
            </div>
          </div>
        </form>
        <div className="terms-div">
          <p className={`error-message ${fieldErrors.termsAccepted ? 'visible' : ''}`}>
            {fieldErrors.termsAccepted}
          </p>
        </div>
      </div>
    </div>
  );
};

export default SignUp;

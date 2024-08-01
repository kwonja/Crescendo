import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store/store';
import {
  sendVerificationCode,
  verifyEmailCode,
  checkEmailExists,
  checkNicknameExists,
  signUp,
} from '../features/auth/authSlice';
import { isValidEmail } from '../utils/EmailValidation';
import { isValidPassword } from '../utils/PasswordValidation';
import { ReactComponent as Visualization } from '../assets/images/visualization.svg';
import TermsModal from '../components/signup/TermsModal';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import '../scss/page/_signup.scss';

const SignUp = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { loading, error, emailAuthId } = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();
  const [email, setEmail] = useState(''); // 이메일
  const [verificationCode, setVerificationCode] = useState(''); // 인증번호
  const [codeVerified, setCodeVerified] = useState(false); // 인증번호 검증
  const [password, setPassword] = useState(''); // 비밀번호
  const [confirmPassword, setConfirmPassword] = useState(''); // 비밀번호 확인
  const [nickname, setNickname] = useState(''); // 닉네임
  const [showPassword, setShowPassword] = useState(false); // 비밀번호 시각화
  const [showConfirmPassword, setShowConfirmPassword] = useState(false); // 비밀번호 확인 시각화
  const [termsAccepted, setTermsAccepted] = useState(false); // 약관 동의
  const [isVerificationButtonDisabled, setIsVerificationButtonDisabled] = useState(false); // 인증번호 버튼 비활성화
  const [verificationCountdown, setVerificationCountdown] = useState(0); // 인증번호 타이머
  const [isFirstVerification, setIsFirstVerification] = useState(true); // 첫 번째 인증 시도 여부
  const [isEmailLocked, setIsEmailLocked] = useState(false); // 이메일 입력 잠금
  const [isVerificationCodeLocked, setIsVerificationCodeLocked] = useState(false); // 인증번호 입력 잠금
  const [fieldErrors, setFieldErrors] = useState({
    email: '',
    verificationCode: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    termsAccepted: '',
  });

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태

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

  useEffect(() => {
    if (termsAccepted) {
      setFieldErrors(prev => ({ ...prev, termsAccepted: '' }));
    }
  }, [termsAccepted]);

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

  const handleSendVerificationCode = async () => {
    if (!isValidEmail(email)) {
      setFieldErrors(prev => ({ ...prev, email: '유효한 이메일 형식을 입력해 주세요' }));
      return;
    }

    const emailExists = await dispatch(checkEmailExists(email));
    if (emailExists.type === `${checkEmailExists.rejected}`) {
      setFieldErrors(prev => ({ ...prev, email: '이메일이 이미 존재합니다.' }));
      return;
    }

    setFieldErrors(prev => ({ ...prev, email: '' }));
    dispatch(sendVerificationCode(email));
    setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 전송되었습니다.' }));
    setIsVerificationButtonDisabled(true);
    setVerificationCountdown(15);
    setIsFirstVerification(false); // 첫 번째 시도 이후 false로 설정
    setIsEmailLocked(true); // 이메일 입력 잠금
    console.log('emailAuthId:', emailAuthId); // emailAuthId 로그로 확인
  };

  const handleVerifyCode = async () => {
    if (!emailAuthId) {
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호 전송을 먼저 해주세요.' }));
      return;
    }

    const result = await dispatch(verifyEmailCode({ emailAuthId, randomKey: verificationCode }));
    if (result.type === `${verifyEmailCode.fulfilled}`) {
      setCodeVerified(true);
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증이 완료되었습니다.' }));
      setIsVerificationCodeLocked(true); // 인증번호 입력 잠금
    } else {
      setFieldErrors(prev => ({ ...prev, verificationCode: '인증번호가 틀립니다.' }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
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

    const nicknameExists = await dispatch(checkNicknameExists(nickname));
    if (nicknameExists.type === `${checkNicknameExists.rejected}`) {
      setFieldErrors(prev => ({ ...prev, nickname: '사용중인 닉네임입니다.' }));
      return;
    }

    setFieldErrors(errors);
    const result = await dispatch(
      signUp({
        email,
        password,
        nickname,
        idolId: 1,
        emailAuthId: emailAuthId!,
        randomKey: verificationCode,
      }),
    );

    if (signUp.fulfilled.match(result)) {
      toast.success('회원가입 성공! 로그인 페이지로 이동합니다.');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } else {
      toast.error('회원가입 실패! 다시 시도해주세요.');
    }
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
              <label>
                <span className="terms-link" onClick={() => setIsModalOpen(true)}>
                  약관
                </span>
                에 동의합니다.
              </label>
            </div>
            <div className="button-group">
              <button type="submit" className="submit-button" disabled={loading}>
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
        {error && <p className="error-message">{error}</p>}
      </div>
      <TermsModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} />
    </div>
  );
};

export default SignUp;

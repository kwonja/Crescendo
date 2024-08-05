package com.sokpulee.crescendo.domain.user.service.auth;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.idol.IdolRepository;
import com.sokpulee.crescendo.domain.user.dto.request.auth.*;
import com.sokpulee.crescendo.domain.user.dto.response.auth.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.entity.EmailAuth;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.EmailAuthRepository;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.encrypt.EnctyptHelper;
import com.sokpulee.crescendo.global.util.mail.MailSendHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final IdolRepository idolRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final MailSendHelper mailSendHelper;
    private final EnctyptHelper enctyptHelper;

    @Override
    public void signUp(SignUpRequest signUpRequest) {

        authenticateEmail(signUpRequest.getEmailAuthId(), signUpRequest.getRandomKey());

        Idol idol = idolRepository.findById(signUpRequest.getIdolId())
                .orElseThrow(IdolNotFoundException::new);

        User user = signUpRequest.toEntity(idol);
        user.encryptPassword(enctyptHelper);

        userRepository.save(user);
    }

    @Override
    public EmailRandomKeyResponse createEmailRandomKey(EmailRandomKeyRequest emailRandomKeyRequest) {
        String randomKey = mailSendHelper.sendEmailRandomKey(emailRandomKeyRequest.getEmail());

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.builder()
                .randomKey(randomKey)
                .expiresAt(expiresAt)
                .build());

        return new EmailRandomKeyResponse(emailAuth.getEmailAuthId());
    }

    @Override
    public Long login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(enctyptHelper.isMatch(loginRequest.getPassword(), user.getPassword())) {
            return user.getId();
        }
        else {
            throw new LoginFailException();
        }
    }

    @Override
    public void saveRefreshToken(Long userId, String refreshToken) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.changeRefreshToken(refreshToken);
    }

    @Override
    public void emailValidate(EmailValidationRequest emailValidationRequest) {

        EmailAuth emailAuth = emailAuthRepository.findById(emailValidationRequest.getEmailAuthId())
                .orElseThrow(EmailValidationNotFoundException::new);

        if(!emailValidationRequest.getRandomKey().equals(emailAuth.getRandomKey())) {
            throw new EmailAuthException();
        }
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {

        authenticateEmail(updatePasswordRequest.getEmailAuthId(), updatePasswordRequest.getRandomKey());

        User user = userRepository.findByEmail(updatePasswordRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);

        user.updatePassword(enctyptHelper.encrypt(updatePasswordRequest.getNewPassword()));

    }

    @Override
    public boolean isRefreshTokenValid(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && refreshToken.equals(user.getRefreshToken());
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.deleteRefreshToken();
    }

    private void authenticateEmail(Long emailAuthId, String randomKey) {
        EmailAuth emailAuth = emailAuthRepository.findById(emailAuthId)
                .orElseThrow(EmailValidationNotFoundException::new);

        if(!randomKey.equals(emailAuth.getRandomKey())) {
            emailAuthRepository.delete(emailAuth);
            throw new EmailAuthException();
        }
        else {
            emailAuthRepository.delete(emailAuth);
        }
    }
}

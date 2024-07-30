package com.sokpulee.crescendo.domain.user.service.auth;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import com.sokpulee.crescendo.domain.user.dto.request.auth.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.auth.EmailValidationRequest;
import com.sokpulee.crescendo.domain.user.dto.request.auth.LoginRequest;
import com.sokpulee.crescendo.domain.user.dto.request.auth.SignUpRequest;
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

        EmailAuth emailAuth = emailAuthRepository.findById(signUpRequest.getEmailAuthId())
                .orElseThrow(EmailValidationNotFoundException::new);

        if(!signUpRequest.getRandomKey().equals(emailAuth.getRandomKey())) {
            emailAuthRepository.delete(emailAuth);
            throw new EmailAuthException();
        }
        else {
            emailAuthRepository.delete(emailAuth);
        }

        Idol idol = idolRepository.findById(signUpRequest.getIdolId())
                .orElseThrow(IdolNotFoundException::new);

        User user = signUpRequest.toEntity(idol);
        user.encryptPassword(enctyptHelper);

        userRepository.save(user);
    }

    @Override
    public EmailRandomKeyResponse createEmailRandomKey(EmailRandomKeyRequest emailRandomKeyRequest) {
        String randomKey = mailSendHelper.sendEmailRandomKey(emailRandomKeyRequest.getEmail());

        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.builder().randomKey(randomKey).build());

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
}

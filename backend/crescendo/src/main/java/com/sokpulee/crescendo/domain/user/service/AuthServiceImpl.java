package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import com.sokpulee.crescendo.domain.user.dto.request.EmailRandomKeyRequest;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.dto.response.EmailRandomKeyResponse;
import com.sokpulee.crescendo.domain.user.entity.EmailAuth;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.EmailAuthRepository;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolNotFoundException;
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
}

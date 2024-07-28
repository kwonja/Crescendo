package com.sokpulee.crescendo.domain.user.service;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import com.sokpulee.crescendo.domain.user.dto.request.SignUpRequest;
import com.sokpulee.crescendo.domain.user.entity.User;
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
}

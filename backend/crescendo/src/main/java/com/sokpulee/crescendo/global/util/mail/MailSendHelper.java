package com.sokpulee.crescendo.global.util.mail;

import javax.mail.MessagingException;

public interface MailSendHelper {
    void sendNewPassword(String email, String password);
    String sendEmailRandomKey(String email);
}

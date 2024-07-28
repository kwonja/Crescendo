package com.sokpulee.crescendo.global.util.mail;

import javax.mail.MessagingException;

public interface MailSendHelper {
    void sendNewPassword(String email, String password) throws MessagingException;
    String sendEmailAuth(String email) throws MessagingException;
}

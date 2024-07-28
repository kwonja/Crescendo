package com.sokpulee.crescendo.global.util.encrypt;

public interface EnctyptHelper {
    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}

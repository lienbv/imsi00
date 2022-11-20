package com.vibee.utils;

import java.util.Random;

public class DataUtils {
    public static String generateTempPwd(int upercase,int lowercase, int num,  int character) {

        String numbers ="0123456789";
        String words="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String charac ="@#$%!^*?&~";
        String lower="abcdefghijklmnopqrstuvwxyz";
        char otp[] = new char[num];
        Random getOtpNum = new Random();
        for (int i = 0; i < num; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }

        char otpword[] = new char[upercase];
        Random getOtpWord = new Random();
        for (int i = 0; i < upercase; i++) {
            otpword[i] = words.charAt(getOtpWord.nextInt(words.length()));
        }
        String optCodeWord = "";
        for (int i = 0; i < otpword.length; i++) {
            optCodeWord += otpword[i];
        }

        char otpLowercase[] = new char[lowercase];
        Random getOtpLowercase = new Random();
        for (int i = 0; i < lowercase; i++) {
            otpLowercase[i] = lower.charAt(getOtpLowercase.nextInt(lower.length()));
        }
        String optCodeLowercase = "";
        for (int i = 0; i < otpLowercase.length; i++) {
            optCodeLowercase += otpLowercase[i];
        }

        char otpChar[] = new char[character];
        Random getOtpChar = new Random();
        for (int i = 0; i < character; i++) {
            otpChar[i] = charac.charAt(getOtpChar.nextInt(charac.length()));
        }
        String optCodeChar = "";
        for (int i = 0; i < otpChar.length; i++) {
            optCodeChar += otpChar[i];
        }

        return optCodeWord+optCodeLowercase+optCode+optCodeChar;
    }
}

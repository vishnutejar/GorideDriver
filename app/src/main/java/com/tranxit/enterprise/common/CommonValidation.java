package com.tranxit.enterprise.common;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class CommonValidation {
    public static Boolean Validation(String Values) {
        return Values == null || Values.equalsIgnoreCase("null") || Values.isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        boolean check;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            check = phone.length() < 6 || phone.length() > 13;
        } else {
            check = true;
        }
        return check;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPass(final String password) {
        return !Pattern.compile("[\\S]{6,}$").matcher(password).matches();
    }
}

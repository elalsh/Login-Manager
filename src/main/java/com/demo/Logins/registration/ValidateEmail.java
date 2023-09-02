package com.demo.Logins.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;


@Service
public class ValidateEmail implements Predicate<String> {

    @Override
    public boolean test(String s) {
        // validate the string using some sort of regex
        return true;
    }
}

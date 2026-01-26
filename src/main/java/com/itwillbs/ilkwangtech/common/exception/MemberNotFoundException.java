package com.itwillbs.ilkwangtech.common.exception;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("사원번호를 확인하세요");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

}

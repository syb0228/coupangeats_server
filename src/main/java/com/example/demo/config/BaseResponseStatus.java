package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    EXPIRED_JWT(false, 2004, "만료된 JWT입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력하세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일을 올바르게 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2017, "비밀번호를 입력하세요."),
    POST_USERS_INVALID_PASSWORD_1(false, 2018, "X 영문/숫자/특수문자 2가지 이상 조합 (8~20자)"),
    POST_USERS_INVALID_PASSWORD_2(false, 2019, "X 3개 이상 연속되거나 동일한 문자/숫자 제외"),
    POST_USERS_INVALID_PASSWORD_3(false, 2020, "X 아이디(이메일) 제외"),
    POST_USERS_EMPTY_NAME(false, 2021, "이름을 정확히 입력하세요."),
    POST_USERS_EMPTY_PHONENUM(false, 2022, "휴대폰 번호를 정확하게 입력하세요."),

    // [POST] /users/logIn
    POST_LOGIN_EMPTY_EMAIL(false, 2023, "아이디를 입력해주세요."),
    POST_LOGIN_INVALID_EMAIL(false, 2024, "아이디는 이메일주소 형식으로 입력해주세요."),
    POST_LOGIN_EMPTY_PASSWORD(false, 2025, "비밀번호를 입력해주세요."),

    // [POST] /events
    POST_EVENT_EMPTY_TITLE(false, 2026, "이벤트 제목을 입력하세요."),
    POST_EVENT_INVALID_EXPIREDAT(false, 2027, "만료일이 현재 시간보다 빠릅니다."),
    POST_EVENT_EMPTY_REPIMG(false, 2028, "이벤트 대표 사진을 첨부하세요."),
    POST_EVENT_EMPTY_NOREPIMG(false, 2029, "이벤트 본문 사진을 첨부하세요."),

    // [POST] /events/{eventId}
    POST_EVENT_EMPTY_EVENTINFO(false, 2030, "이벤트 제목이나 내용을 입력하세요."),

    // [POST] /user-addresses/{userId}
    POST_USERADDRESS_EMPTY_NAME(false, 2031, "주소 명칭을 입력하세요."),

    // [POST] /orders
    POST_USERORDER_INVAILD_ITEM(false, 2032, "올바르지 않은 일회용품 선택입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    POST_USERS_EXISTS_EMAIL(false, 3013, "이미 가입된 이메일 주소입니다. 다른 이메일을 입력하여 주세요."),

    // [POST] /users/logIn
    FAILED_TO_LOGIN(false,3014,"입력하신 아이디 또는 비밀번호가 일치하지 않습니다."),
    INACTIVE_USER(false, 3015, "비활성화된 유저입니다."),
    DELETED_USER(false, 3016, "탈퇴한 유저입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userId}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    //[PATCH] /events/{eventId}
    MODIFY_FAIL_EVENTINFO(false, 4015, "이벤트 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[PATCH] /user-addresses/{userAddressId}
    MODIFY_FAIL_ADDRESS_DETAIL(false, 4016, "상세 주소 변경에 실패하였습니다."),
    MODIFY_FAIL_DIRECTIONS(false, 4017, "길 안내 변경에 실패하였습니다."),
    MODIFY_FAIL_ADDRESS_CATEGORY(false, 4018, "주소 카테고리 변경에 실패하였습니다."),
    MODIFY_FAIL_ADDRESS_ALIAS(false, 4019, "주소 별칭 변경에 실패하였습니다."),

    //[PATCH] /reviews/{reviewId}
    MODIFY_FAIL_REVIEW_SCORE(false, 4020, "리뷰 평점 변경에 실패하였습니다."),
    MODIFY_FAIL_REVIEW_CONTENT(false, 4021, "리뷰 내용 변경에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

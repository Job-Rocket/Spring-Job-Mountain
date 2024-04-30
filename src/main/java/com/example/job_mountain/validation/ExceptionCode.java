package com.example.job_mountain.validation;

import static com.example.job_mountain.validation.HttpStatus.*;

public enum ExceptionCode {

    // 회원가입
    SIGNUP_CREATED_OK(CREATED, "A000", "회원가입 성공"),
    SIGNUP_DUPLICATED_ID(DUPLICATED_VALUE, "A001", "ID 중복"),
    SIGNUP_DUPLICATED_EMAIL(DUPLICATED_VALUE, "A002", "EMAIL 중복"),
    SIGNUP_NOTSAME_PW(NOTSAME, "A003", "두 개의 PASSWORD 불일치"),

    // 파일 관련
    FILE_SIZE_EXCEED(INVALID_ACCESS, "F001", "FILE 크기 초과"),
    FILE_NOT_FOUND(NOT_FOUND_VALUE, "F002", "FILE이 없음"),
    FILE_STORAGE_ERROR(INVALID_ACCESS, "F003", "FILE 저장 오류"),
    FILE_DELETE_ERROR(INVALID_ACCESS, "F004", "FILE 삭제 오류"),

    // 로그인
    LOGIN_OK(SUCCESS, "B000", "로그인 성공"),
    LOGIN_NOT_FOUND_ID(NOT_FOUND_VALUE, "B001", "잘못된 ID 로그인 실패"),
    LOGIN_NOT_FOUND_PW(NOT_FOUND_VALUE, "B002", "잘못된 PW 로그인 실패"),
    LOGOUT_OK(SUCCESS, "B003", "로그아웃 성공"),
    LOGOUT_INVALID(NOT_FOUND_VALUE, "B004", "이미 로그아웃한 유저"),

    // 회원 정보
    USER_FOUND(SUCCESS, "C000", "회원정보 있음"),
    USER_NOT_FOUND(NOT_FOUND_VALUE, "C001", "회원정보 없음"),
    USER_UPDATE_OK(SUCCESS, "C002", "회원정보 수정 성공"),
    USER_SEARCH_OK(SUCCESS, "C003", "회원 검색 성공"),

    
    // 토큰
    INVALID_JWT_SIGNATURE(UNAUTHORIZED,"G000", "타당하지 않은 JWT 서명 오류"),
    INVALID_JWT_TOKEN(UNAUTHORIZED,"G001", "잘못된 JWT 토큰 오류"),
    EXPIRED_JWT_TOKEN(UNAUTHORIZED,"G002", "만료된 JWT 토큰 오류"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED,"G003", "지원되지 않는 JWT 토큰 오류"),
    TOKEN_SUCCESS(SUCCESS, "G005", "토큰 확인 성공"),


    // job 정보
    JOB_SAVE_OK(SUCCESS, "J000", "JOB 저장 성공"),
    JOB_SAVE_FAIL1(NOT_FOUND_VALUE, "J001", "JOB 제목 없음"),
    JOB_SAVE_FAIL2(NOT_FOUND_VALUE, "J002", "JOB 내용 없음"),
    JOB_NOT_FOUND(NOT_FOUND_VALUE, "J003", "JOB 정보 없음"),
    JOB_UPDATE_OK(SUCCESS, "J004", "JOB 수정 성공"),
    JOB_DELETE_OK(SUCCESS, "J005", "JOB 삭제 성공"),
    JOB_GET_OK(SUCCESS, "J006", "JOB 불러오기 성공"),

    // resume 정보
    RESUME_SAVE_OK(SUCCESS, "R000", "RESUME(또는 SHORTS) 저장 성공"),
    RESUME_SAVE_FAIL1(NOT_FOUND_VALUE, "R001", "RESUME(또는 SHORTS) 제목 없음"),
    RESUME_SAVE_FAIL2(NOT_FOUND_VALUE, "R002", "RESUME(또는 SHORTS) 파일 없음"),
    RESUME_UPDATE_OK(SUCCESS, "R003", "RESUME(또는 SHORTS) 수정 성공"),
    RESUME_DELETE_OK(SUCCESS, "R004", "RESUME(또는 SHORTS) 삭제 성공"),
    RESUME_NOT_FOUND(NOT_FOUND_VALUE, "R005", "RESUME(또는 SHORTS) 정보 없음"),
    RESUME_GET_OK(SUCCESS, "R006", "RESUME(또는 SHORTS) 불러오기 성공"),

    /**
     * 잘못된 ExceptionCode
     */
    INVALID_FORM(INVALID_ACCESS, "Z000", "형식에 어긋난 이름"),
    INVALID_USER(INVALID_ACCESS, "Z001", "해당 글에 대한 접근 권한 없음"),
    EMPTY(null, "", ""),
    WRONG_PASSWORD(INVALID_ACCESS, "Z002", "잘못된 비밀번호");
    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

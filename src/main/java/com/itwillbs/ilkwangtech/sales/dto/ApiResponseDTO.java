package com.itwillbs.ilkwangtech.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    // 1. 성공 + 데이터 있음
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    // 2. 성공 + 데이터 없음 (Void 사용)
    public static <T> ApiResponseDTO<T> success(String message) {
        return new ApiResponseDTO<>(true, message, null);
    }

    // 3. 실패 + 데이터 있음 (예: 에러 상세 정보 포함)
    public static <T> ApiResponseDTO<T> fail(String message, T data) {
        return new ApiResponseDTO<>(false, message, data);
    }

    // 4. 실패 + 데이터 없음
    public static <T> ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "", data);
    }
}
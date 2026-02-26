package com.itwillbs.ilkwangtech.sales.exception;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SalesExceptionHandler {

    // 모든 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAllException(Exception e) {
        ApiResponseDTO<Void> response = ApiResponseDTO.fail(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러
                .body(response);
    }

    // 비즈니스 로직 예외 (예: 재고 부족, 중복 고객 등) 처리용 (선택 사항)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 에러
                .body(ApiResponseDTO.fail(e.getMessage()));
    }

}
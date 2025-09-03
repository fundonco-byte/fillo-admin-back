package sh.admin.backend.common.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sh.admin.backend.common.exception.MemberException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public abstract class AbstractExceptionHandler implements MemberException {

    // 모든 API의 요청 파라미터를 Valid로 1차 확인 후 에러가 발생 시 우선적으로 선행되는 ExceptionHandler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}

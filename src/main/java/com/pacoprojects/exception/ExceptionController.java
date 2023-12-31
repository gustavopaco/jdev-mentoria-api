package com.pacoprojects.exception;

import com.pacoprojects.email.EmailMessage;
import com.pacoprojects.email.EmailObject;
import com.pacoprojects.email.EmailService;
import com.pacoprojects.util.Constantes;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController extends ResponseEntityExceptionHandler {

    private final EmailService emailService;

    @Override
    @ExceptionHandler(value = {Exception.class, RuntimeException.class, Throwable.class})
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception exception, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        StringBuilder message = new StringBuilder();

        if (exception instanceof HttpMessageNotReadableException) {
            message.append("Não está sendo enviado o BODY no corpo da requisição");
        } else {
            message.append(exception.getMessage());
        }
        exception.printStackTrace();
        emailService.sendMailWithAttachment(EmailObject
                .builder()
                .destinatario(Constantes.ADMIN_EMAIL)
                .assunto(Constantes.SERVER_ERROR_EXCEPTION_MESSAGE)
                .menssagem(EmailMessage.getServerErrorMessage(ExceptionUtils.getStackTrace(exception)))
                .build());
        return new ResponseEntity<>(ExceptionObject
                .builder()
                .message(message.toString())
                .code(statusCode.value())
                .build(), headers, statusCode);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        StringBuilder message = new StringBuilder();

        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> message.append(objectError.getDefaultMessage()).append("\n"));
            String formatStringBuilder = message.substring(0, message.length() - 1);
            message.delete(0, message.length());
            message.append(formatStringBuilder);
        }
        return ResponseEntity
                .badRequest()
                .body(ExceptionObject
                        .builder()
                        .message(message.toString())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
    }


    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleExceptionResponseStatus(ResponseStatusException exception) {
        String message = exception.getReason();
        return new ResponseEntity<>(ExceptionObject
                .builder()
                .message(message)
                .code(exception.getStatusCode().value())
                .build(), exception.getStatusCode());
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, SQLException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception exception) {
        StringBuilder message = new StringBuilder();

        if (exception instanceof DataIntegrityViolationException) {
            message.append("Erro de integridade no banco: ").append(exception.getCause().getCause().getMessage());
        } else if (exception instanceof ConstraintViolationException) {
            message.append("Erro de Chave estrangeira: ").append(exception.getCause().getCause().getMessage());
        } else if (exception instanceof SQLException) {
            message.append("Erro de SQL do banco: ").append(exception.getCause().getCause().getMessage());
        } else {
            message.append(exception.getMessage());
        }
        exception.printStackTrace();
        emailService.sendMailWithAttachment(EmailObject
                .builder()
                .destinatario(Constantes.ADMIN_EMAIL)
                .assunto(Constantes.SERVER_ERROR_EXCEPTION_MESSAGE)
                .menssagem(EmailMessage.getServerErrorMessage(ExceptionUtils.getStackTrace(exception)))
                .build());
        return ResponseEntity
                .internalServerError()
                .body(ExceptionObject
                        .builder()
                        .message(message.toString())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException exception) {
        StringBuilder builder = new StringBuilder();
        Throwable cause = exception.getCause().getCause();
        if (cause instanceof ConstraintViolationException constraintViolationException) {
            constraintViolationException
                    .getConstraintViolations()
                    .forEach(constraintViolation -> builder.append(constraintViolation.getMessage()).append("\n"));
            String messsage = builder.substring(0, builder.length() - 1);
            builder.delete(0, builder.length());
            builder.append(messsage);
        } else {
            builder.append(exception.getMessage());
        }

        return ResponseEntity
                .internalServerError()
                .body(ExceptionObject
                        .builder()
                        .message(builder.toString())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }

    @ExceptionHandler(value = {RestClientException.class})
    protected ResponseEntity<Object> handleRestClienteException(RestClientException exception) {
        StringBuilder builder = new StringBuilder();

        builder.append("Erro ao tentar conectar com a Api externa.");
        exception.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(ExceptionObject
                        .builder()
                        .message(builder.toString())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}

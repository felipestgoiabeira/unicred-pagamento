package com.unicred.exception.handle;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.model.ExceptionModel;
import com.unicred.exception.EntityExistsException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

//    @ExceptionHandler(BusinessException.class)
//    public final ResponseEntity<Object> handleAllException(BusinessException ex, WebRequest request) throws Exception {
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        return handleExceptionInternal(ex, toExceptionModel(status, ex.getMessage()), new HttpHeaders(), status, request);
//    }

    @ExceptionHandler(EntityExistsException.class)
    public final ResponseEntity<Object> handleAllException(EntityExistsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(ex, toExceptionModel(status, ex.getMessage()), new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleAllException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleExceptionInternal(ex, toExceptionModel(status, ex.getMessage()), new HttpHeaders(), status, request);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, toExceptionModel(status, MSG_ERRO_GENERICA_USUARIO_FINAL), new HttpHeaders(), status, request);
    }

    private ExceptionModel toExceptionModel(HttpStatusCode status, String message) {
        return ExceptionModel
                .builder()
                .datestamp(new Date())
                .message(message)
                .code(status.value())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return handleValidationInternal(ex, headers, (HttpStatus) status, request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());


        return handleExceptionInternal(ex, toExceptionModel(status, detail), headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request, BindingResult bindingResult) {
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<String> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    return String.format("%s", message);
                })
                .collect(Collectors.toList());

        detail = new StringBuilder()
                .append(detail)
                .append(" Problemas: ")
                .append(problemObjects.stream().collect(Collectors.joining(", ")))
                .append(".")
                .toString();

        ExceptionModel problem = toExceptionModel(status, detail);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {


        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "");

        ExceptionModel problem = toExceptionModel(status, detail);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        ExceptionModel problem = toExceptionModel(status, detail);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);

        ExceptionModel problem = toExceptionModel(status, detail);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        ExceptionModel problem = toExceptionModel(status, detail);


        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {

        if (body == null) {
            body = toExceptionModel(status, MSG_ERRO_GENERICA_USUARIO_FINAL);

        } else if (body instanceof String) {
            body = toExceptionModel(status, MSG_ERRO_GENERICA_USUARIO_FINAL);
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }
}

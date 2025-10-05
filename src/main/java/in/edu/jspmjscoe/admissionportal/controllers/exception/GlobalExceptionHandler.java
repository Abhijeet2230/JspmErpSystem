package in.edu.jspmjscoe.admissionportal.controllers.exception;

import in.edu.jspmjscoe.admissionportal.dtos.response.ErrorResponse;
import in.edu.jspmjscoe.admissionportal.exception.*;
import in.edu.jspmjscoe.admissionportal.exception.achievement.CertificateMarkException;
import in.edu.jspmjscoe.admissionportal.exception.achievement.CompetitionMarkException;
import in.edu.jspmjscoe.admissionportal.exception.achievement.FieldProjectMarkException;
import in.edu.jspmjscoe.admissionportal.exception.achievement.InternshipMarkException;
import in.edu.jspmjscoe.admissionportal.exception.cce.ExamNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.cce.UnitAssessmentNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.minio.AchievementFileAccessException;
import in.edu.jspmjscoe.admissionportal.exception.minio.MinioStorageException;
import in.edu.jspmjscoe.admissionportal.exception.security.InvalidCredentialsException;
import in.edu.jspmjscoe.admissionportal.exception.trainingplacement.InvalidTrainingPlacementRequestException;
import in.edu.jspmjscoe.admissionportal.exception.trainingplacement.TrainingPlacementNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------- Common Exceptions ----------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex, request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Duplicate Resource", ex, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Credentials", ex, request);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientAuth(InsufficientAuthenticationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex, request, "Authentication is required to access this resource");
    }

    // ---------------- Training & Placement ----------------
    @ExceptionHandler(TrainingPlacementNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTPNotFound(TrainingPlacementNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Training & Placement Not Found", ex, request);
    }

    @ExceptionHandler(InvalidTrainingPlacementRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTPRequest(InvalidTrainingPlacementRequestException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Training & Placement Request", ex, request);
    }

    //----------------- Subject ----------------------
    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<String> handleSubjectNotFound(SubjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(SubjectNotAvailableForTeacherException.class)
    public ResponseEntity<ErrorResponse> handleSubjectNotAvailable(SubjectNotAvailableForTeacherException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Subject Not Available", ex, request);
    }

    @ExceptionHandler(MinioStorageException.class)
    public ResponseEntity<ErrorResponse> handleMinioStorageException(MinioStorageException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("File Upload Failed")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---------------- Teacher / User / Department ----------------
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex, request);
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeacherNotFound(TeacherNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Teacher Not Found", ex, request);
    }

    @ExceptionHandler(TeacherAccountNotApprovedException.class)
    public ResponseEntity<ErrorResponse> handleTeacherNotApproved(TeacherAccountNotApprovedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Teacher Account Not Approved", ex, request);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Department Not Found", ex, request);
    }

    // ---------------- Teacher Leaves ----------------
    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLeaveNotFound(LeaveNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Leave Not Found", ex, request);
    }

    @ExceptionHandler(PendingLeaveExistsException.class)
    public ResponseEntity<ErrorResponse> handlePendingLeaveExists(PendingLeaveExistsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Pending Leave Exists", ex, request);
    }

    @ExceptionHandler(NoPendingLeavesException.class)
    public ResponseEntity<ErrorResponse> handleNoPendingLeaves(NoPendingLeavesException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "No Pending Leaves", ex, request);
    }

    // ---------------- Head Leaves ----------------
    @ExceptionHandler(HeadLeaveNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHeadLeaveNotFound(HeadLeaveNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Head Leave Not Found", ex, request);
    }

    @ExceptionHandler(CertificateMarkException.class)
    public ResponseEntity<ErrorResponse> handleCertificateMarkException(
            CertificateMarkException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Certificate Marks")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternshipMarkException.class)
    public ResponseEntity<ErrorResponse> handleInternshipMarkException(
            InternshipMarkException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Internship Marks")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CompetitionMarkException.class)
    public ResponseEntity<ErrorResponse> handleCompetitionMarkException(
            CompetitionMarkException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Competition Marks")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PendingHeadLeaveExistsException.class)
    public ResponseEntity<ErrorResponse> handlePendingHeadLeaveExists(PendingHeadLeaveExistsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Pending Head Leave Exists", ex, request);
    }

    // ---------------- Fallback ----------------
    @ExceptionHandler(Exception.class)
    @ExceptionHandler(FieldProjectMarkException.class)
    public ResponseEntity<ErrorResponse> handleFieldProjectMarkException(
            FieldProjectMarkException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Field Project Marks")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AchievementFileAccessException.class)
    public ResponseEntity<ErrorResponse> handleAchievementFileAccessException(
            AchievementFileAccessException ex, HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("File Access Failed")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class) // fallback
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex, request);
    }

    // ---------------- Utility Method ----------------
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, Exception ex, HttpServletRequest request) {
        return buildResponse(status, error, ex, request, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, Exception ex, HttpServletRequest request, String message) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, status);
    }
}

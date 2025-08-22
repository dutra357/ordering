package com.dutra.ordering.domain.exceptions;

public class ErrorMessages {

    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid.";
    public static final String VALIDATION_ERROR_EMAIL_IS_BLANK = "Email is blank.";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived cannot be changed.";

    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED = "Cannot change order %s status from %s to %s.";
;}

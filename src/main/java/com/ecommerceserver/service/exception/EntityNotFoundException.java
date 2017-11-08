package com.ecommerceserver.service.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class clazz, String parameterName, Object parameter) {
        super(String.format("Could not found %s with %s = %s", clazz.getCanonicalName(), parameterName, parameter.toString()));
    }
}

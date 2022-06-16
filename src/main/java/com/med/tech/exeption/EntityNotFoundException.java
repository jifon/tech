package com.med.tech.exeption;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String id) {
        super("entity not found" + id);
    }

}
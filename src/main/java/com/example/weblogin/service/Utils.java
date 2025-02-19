package com.example.weblogin.service;

import com.example.weblogin.domain.itemOption.ColorType;

import java.util.Optional;

public class Utils {
    public static <E extends Enum<E>> Optional<E> convertToEnum(String status, Class<E> enumClass) {
           if (status == null || status.trim().isEmpty()) {
               throw  new IllegalArgumentException();
           }
           try {
               return Optional.of(Enum.valueOf(enumClass, status.trim()));
           } catch (IllegalArgumentException e) {
               throw  new IllegalArgumentException();
           }
       }
}

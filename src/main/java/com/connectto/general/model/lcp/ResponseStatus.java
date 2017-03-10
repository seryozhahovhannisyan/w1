package com.connectto.general.model.lcp;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
public enum ResponseStatus {

    SUCCESS(1, "success"),
    INTERNAL_ERROR(2, "error"),
    DATA_NOT_FOUND(3, "the data not found"),
    INVALID_PARAMETER(4, "not allowed like parameter"),
    RESOURCE_NOT_FOUND(5, "the resource not found"),
    DUPLICATE_DATA(6, "the data already exists"),
    PERMISSION_DENIED(7, "the action not allowed");


    private ResponseStatus(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static ResponseStatus valueOf(final int value) {

        for (ResponseStatus status : ResponseStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    private int value;
    private String title;

}

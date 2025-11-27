package com.tiketika.Tiketika.common.response;



public record StandardResponse<T>(
        int status,
        String message,
        String endpoint,
        T data
) {
    public static <T> StandardResponse<T> success(int status, String message, String endpoint, T data) {
        return new StandardResponse<>(status, message, endpoint, data);
    }


}

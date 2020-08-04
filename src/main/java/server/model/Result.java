package server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class  Result  {

    public int code;
    public String msg;
    public Object data;


    public static Result SUCCESS(Object data, String msg) {
        return Result.builder()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(msg)
                .data(data)
                .build();
    }

    public static Result SUCCESS(Object data) {
        return Result.builder()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMessage())
                .data(data)
                .build();
    }



    public static Result SUCCESS(String msg) {
        return Result.builder()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(msg)
                .build();
    }

    public static Result SUCCESS() {
        return Result.builder()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMessage())
                .build();
    }


    public static Result fail(Object data, String msg) {
        return Result.builder()
                .code(ResultEnum.FAILURE.getCode())
                .msg(msg)
                .data(data)
                .build();
    }



    public static Result fail(Object data) {
        return Result.builder()
                .code(ResultEnum.FAILURE.getCode())
                .msg(ResultEnum.FAILURE.getMessage())
                .data(data)
                .build();
    }



    public static Result fail(String msg) {
        return Result.builder()
                .code(ResultEnum.FAILURE.getCode())
                .msg(msg)
                .build();
    }

    public static Result fail() {
        return Result.builder()
                .code(ResultEnum.FAILURE.getCode())
                .msg(ResultEnum.FAILURE.getMessage())
                .build();
    }

    public static Result of(ResultEnum en, Object data, String msg) {
        return Result.builder()
                .code(en.getCode())
                .msg(en.getMessage())
                .data(data)
                .build();
    }

    public static Result of(ResultEnum en, Object data) {
        return Result.builder()
                .code(en.getCode())
                .msg(en.getMessage())
                .data(data)
                .build();
    }

    public static Result of(int code, String msg) {
        return Result.builder()
                .code(code)
                .msg(msg)
                .build();
    }


    public static Result of(ResultEnum en) {
        return Result.builder()
                .code(en.getCode())
                .msg(en.getMessage())
                .build();
    }
}

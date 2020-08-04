package server.model;

public enum ResultEnum {

    SUCCESS(200,"成功"),
    FAILURE(201,"失败"),
    USER_NEED_AUTHORITIES(201,"用户未登录"),
    USER_LOGIN_FAILED(202,"登录失败"),
    ACCOUNT_PASSWORD_WORNG(202,"用户账号或密码错误"),
    PASSWORD_EXPIRE(202,"密码过期，请联系管理员!"),
    ACCOUNT_EXPIRE(202,"用户过期，请联系管理员!"),
    ACCOUNT_LOCK(202,"用户被锁定，请联系管理员!"),
    ACCOUNT_FORBID(202,"用户被禁用，请联系管理员!"),
    Parameter_Is_Empty(204,"参数为空"),
    USER_NO_ACCESS(204,"用户无权访问"),
    USER_LOGOUT_SUCCESS(205,"用户登出成功"),
    TOKEN_IS_BLACKLIST(206,"此token为黑名单"),
    LOGIN_IS_OVERDUE(207,"登录已失效");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @deprecation:通过code返回枚举
     */
    public static ResultEnum of(int code){
        ResultEnum[] values = values();
        for (ResultEnum value : values) {
            if(value.getCode() == code){
                return value;
            }
        }
        throw  new RuntimeException("Unknown code of ResultEnum");
    }

    public static ResultEnum of(int code, String message){
        ResultEnum[] values = values();
        for (ResultEnum value : values) {
            if(value.getCode() == code){
                return value;
            }
        }
        throw  new RuntimeException("Unknown code of ResultEnum");
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}

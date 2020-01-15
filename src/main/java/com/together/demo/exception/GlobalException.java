package com.together.demo.exception;

public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorDesc;

    public GlobalException() {
        super();
    }

    public GlobalException(InfoInterface errorInfoInterface) {
        super(errorInfoInterface.getCode()+"");
        this.errorCode = errorInfoInterface.getCode();
        this.errorDesc = errorInfoInterface.getDesc();
    }

    public GlobalException(InfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getCode()+"", cause);
        this.errorCode = errorInfoInterface.getCode();
        this.errorDesc = errorInfoInterface.getDesc();
    }

    public GlobalException(String errorDesc) {
        super(errorDesc);
        this.errorDesc = errorDesc;
    }

    public GlobalException(Integer errorCode, String errorDesc) {
        super(errorCode+"");
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public GlobalException(Integer errorCode, String errorDesc, Throwable cause) {
        super(errorCode+"", cause);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorMsg(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    @Override
    public String getMessage() {
        return errorDesc;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}

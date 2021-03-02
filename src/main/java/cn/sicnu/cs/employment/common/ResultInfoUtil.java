package cn.sicnu.cs.employment.common;



/**
 * 公共返回对象工具类
 */
public class ResultInfoUtil {

    /**
     * 请求出错返回
     *
     * @param path 请求路径
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildError(String path) {
        ResultInfo<T> resultInfo = build(Constants.ERROR_CODE,
                Constants.ERROR_MESSAGE, path, null);
        return resultInfo;
    }

    /**
     * 请求出错返回
     *
     * @param errorCode 错误代码
     * @param message   错误提示信息
     * @param path      请求路径
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildError(int errorCode, String message, String path) {
        ResultInfo<T> resultInfo = build(errorCode, message, path, null);
        return resultInfo;
    }

    /**
     * 请求成功返回
     *
     * @param path 请求路径
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildSuccess(String path) {
        ResultInfo<T> resultInfo = build(Constants.SUCCESS_CODE,
                Constants.SUCCESS_MESSAGE, path, null);
        return resultInfo;
    }

    /**
     * 请求成功返回
     *
     * @param path 请求路径
     * @param data 返回数据对象
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> buildSuccess(String path, T data) {
        ResultInfo<T> resultInfo = build(Constants.SUCCESS_CODE,
                Constants.SUCCESS_MESSAGE, path, data);
        return resultInfo;
    }

    /**
     * 构建返回对象方法
     *
     * @param code
     * @param message
     * @param path
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultInfo<T> build(Integer code, String message, String path, T data) {
        if (code == null) {
            code = Constants.SUCCESS_CODE;
        }
        if (message == null) {
            message = Constants.SUCCESS_MESSAGE;
        }
        return ResultInfo.<T>builder()
                .code(code)
                .message(message)
                .path(path)
                .data(data)
                .build();
    }

}
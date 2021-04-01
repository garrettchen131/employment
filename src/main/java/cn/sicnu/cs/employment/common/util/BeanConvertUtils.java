package cn.sicnu.cs.employment.common.util;

import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.BeanUtils;

import java.util.*;

import static cn.sicnu.cs.employment.common.Constants.PREFIX_PIC_STORE;

/**
 * 转换对象工具
 */
public class BeanConvertUtils extends BeanUtils {

    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
        return convertTo(source, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param source         源对象
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == source || null == targetSupplier) {
            return null;
        }
        T target = targetSupplier.get();
        copyProperties(source, target);
        if (callBack != null) {
            callBack.callBack(source, target);
        }
        return target;
    }

    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
        return convertListTo(sources, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return null;
        }

        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T target = targetSupplier.get();
            copyProperties(source, target);
            if (callBack != null) {
                callBack.callBack(source, target);
            }
            list.add(target);
        }
        return list;
    }



    /**
     * 回调接口
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface ConvertCallBack<S, T> {
        void callBack(S t, T s);
    }

    public static String setToString(Set<String> list) {

        if (list == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        //第一个前面不拼接","
        for (String string : list) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();

    }
    public static Set<String> stringToPicSet(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return null;
        }
        String[] split = str.split(",");
        Set<String> imgs = new HashSet<>();
        for (String s : split) {
            String img = PREFIX_PIC_STORE + s;
            imgs.add(img);
        }
        return imgs;
    }

    public static Set<String> stringToSet(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return null;
        }
        String[] split = str.split(",");
        return new HashSet<>(Arrays.asList(split));
    }

    public static String[] setToStringArray(Set<String> list){
        return list.toArray(new String[list.size()]);
    }
}

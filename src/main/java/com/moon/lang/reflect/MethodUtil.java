package com.moon.lang.reflect;

import com.moon.lang.ClassUtil;
import com.moon.lang.SupportUtil;
import com.moon.lang.ThrowUtil;
import com.moon.lang.ref.WeakCoordinate;
import com.moon.util.FilterUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.moon.lang.ThrowUtil.noInstanceError;
import static com.moon.lang.reflect.Reflection.*;

/**
 * @author ZhangDongMin
 */
public final class MethodUtil {

    private MethodUtil() {
        noInstanceError();
    }

    /*
     * -----------------------------------------------------------------------------------------
     * public methods
     * -----------------------------------------------------------------------------------------
     */

    // 没有方法名，返回所有 public 方法

    private final static WeakCoordinate<Class, Object, List<Method>>
        WEAK = WeakCoordinate.manageOne();

    private final static WeakCoordinate<Class, Object, List<Method>>
        WEAK_PARAMS = WeakCoordinate.manageOne();

    public final static List<Method> getPublicMethods(Class type) {
        return WEAK.get(type, TypeEnum.PUBLIC, (clazz, n) ->
            get(clazz.getMethods()).flipToNot());
    }

    public final static List<Method> getPublicStaticMethods(Class type) {
        return WEAK.get(type, TypeEnum.PUBLIC_STATIC, (clazz, n) ->
            FilterUtil.filter(getPublicMethods(clazz), Asserts.isStatic, get()).flipToNot());
    }

    public final static List<Method> getPublicMemberMethods(Class type) {
        return WEAK.get(type, TypeEnum.PUBLIC_STATIC, (clazz, n) ->
            FilterUtil.filter(getPublicMethods(clazz), Asserts.isMember, get()).flipToNot());
    }

    // 返回所有符合名字的 public 方法

    public final static List<Method> getPublicMethods(Class type, String methodName) {
        return WEAK.get(type, methodName, (clazz, name) ->
            FilterUtil.filter(getPublicMethods(clazz), nameTester(name), get()).flipToNot());
    }

    public final static List<Method> getPublicStaticMethods(Class type, String methodName) {
        return FilterUtil.filter(getPublicMethods(type, methodName), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getPublicMemberMethods(Class type, String methodName) {
        return FilterUtil.filter(getPublicMethods(type, methodName), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getPublicMethod(Class type, String methodName) {
        Method m = SupportUtil.matchOne(getPublicMethods(type, methodName), Asserts.noParams);
        if (m == null) {
            ThrowUtil.throwRuntime("Can not find public method: "
                + type + "." + methodName + "();");
        }
        return m;
    }

    // 返回所有符合名字和参数类型列表的 public 方法

    public final static List<Method> getPublicMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(WEAK.get(type, Arrays.hashCode(parameterTypes),
            () -> matching(getPublicMethods(type, methodName), parameterTypes).flipToNot()),
            nameTester(methodName), get()).flipToNot();
    }

    public final static List<Method> getPublicStaticMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getPublicMethods(type, methodName, parameterTypes), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getPublicMemberMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getPublicMethods(type, methodName, parameterTypes), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getPublicMethod(Class type, String methodName, Class... parameterTypes) {
        List<Method> methods = getPublicMethods(type, methodName, parameterTypes);
        if (methods.isEmpty()) {
            ThrowUtil.throwRuntime("Can not find public method: "
                + type + "." + methodName + "(" + Arrays.toString(parameterTypes) + ");");
        }
        return methods.get(0);
    }

    /*
     * -----------------------------------------------------------------------------------------
     * declared methods
     * -----------------------------------------------------------------------------------------
     */

    // 没有方法名，返回所有 declared 方法

    public final static List<Method> getDeclaredMethods(Class type) {
        return WEAK.get(type, TypeEnum.DECLARED, (clazz, n) ->
            get(clazz.getDeclaredMethods()));
    }

    public final static List<Method> getDeclaredStaticMethods(Class type) {
        return WEAK.get(type, TypeEnum.DECLARED_STATIC, (clazz, n) ->
            FilterUtil.filter(getDeclaredMethods(clazz), Asserts.isStatic, get()).flipToNot());
    }

    public final static List<Method> getDeclaredMemberMethods(Class type) {
        return WEAK.get(type, TypeEnum.DECLARED_STATIC, (clazz, n) ->
            FilterUtil.filter(getDeclaredMethods(clazz), Asserts.isMember, get()).flipToNot());
    }

    // 返回所有符合名字的 declared 方法

    public final static List<Method> getDeclaredMethods(Class type, String methodName) {
        return WEAK.get(type, methodName, (clazz, name) ->
            FilterUtil.filter(getDeclaredMethods(clazz), nameTester(name), get()).flipToNot());
    }

    public final static List<Method> getDeclaredStaticMethods(Class type, String methodName) {
        return FilterUtil.filter(getDeclaredMethods(type, methodName), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getDeclaredMemberMethods(Class type, String methodName) {
        return FilterUtil.filter(getDeclaredMethods(type, methodName), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getDeclaredMethod(Class type, String methodName) {
        Method m = SupportUtil.matchOne(getDeclaredMethods(type, methodName), Asserts.noParams);
        if (m == null) {
            throwErr(type, methodName);
        }
        return m;
    }

    // 返回所有符合名字和参数类型列表的 declared 方法

    public final static List<Method> getDeclaredMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(WEAK.get(type, Arrays.hashCode(parameterTypes),
            () -> matching(getDeclaredMethods(type, methodName), parameterTypes).flipToNot()),
            nameTester(methodName), get()).flipToNot();
    }

    public final static List<Method> getDeclaredStaticMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getDeclaredMethods(type, methodName, parameterTypes), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getDeclaredMemberMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getDeclaredMethods(type, methodName, parameterTypes), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getDeclaredMethod(Class type, String methodName, Class... parameterTypes) {
        List<Method> methods = getDeclaredMethods(type, methodName, parameterTypes);
        if (methods.isEmpty()) {
            throwErr(type, methodName, parameterTypes);
        }
        return methods.get(0);
    }

    /*
     * -----------------------------------------------------------------------------------------
     * merge public and declared methods
     * -----------------------------------------------------------------------------------------
     */

    public final static List<Method> getAllMethods(Class type) {
        return WEAK.get(type, TypeEnum.ALL, (clazz, n) ->
            unmodify(getPublicMethods(type), getDeclaredMethods(type), type).flipToNot());
    }

    public final static List<Method> getAllStaticMethods(Class type) {
        return WEAK.get(type, TypeEnum.ALL_STATIC, (clazz, n) ->
            FilterUtil.filter(getAllMethods(clazz), Asserts.isStatic, get()).flipToNot());
    }

    public final static List<Method> getAllMemberMethods(Class type) {
        return WEAK.get(type, TypeEnum.ALL_STATIC, (clazz, n) ->
            FilterUtil.filter(getAllMethods(clazz), Asserts.isMember, get()).flipToNot());
    }

    // 返回所有符合名字的所有方法

    public final static List<Method> getAllMethods(Class type, String methodName) {
        return WEAK.get(type, methodName, (clazz, name) ->
            FilterUtil.filter(getAllMethods(clazz), nameTester(name), get()).flipToNot());
    }

    public final static List<Method> getAllStaticMethods(Class type, String methodName) {
        return FilterUtil.filter(getAllMethods(type, methodName), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getAllMemberMethods(Class type, String methodName) {
        return FilterUtil.filter(getAllMethods(type, methodName), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getAccessibleMethod(Class type, String methodName) {
        Method m = SupportUtil.matchOne(getAllMethods(type, methodName), Asserts.noParams);
        if (m == null) {
            throwErr(type, methodName);
        }
        return m;
    }

    // 返回所有符合名字和参数类型列表的所有方法

    public final static List<Method> getAllMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(WEAK.get(type, Arrays.hashCode(parameterTypes),
            () -> matching(getAllMethods(type, methodName), parameterTypes).flipToNot()),
            nameTester(methodName), get()).flipToNot();
    }

    public final static List<Method> getAllStaticMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getAllMethods(type, methodName, parameterTypes), Asserts.isStatic, get()).flipToNot();
    }

    public final static List<Method> getAllMemberMethods(Class type, String methodName, Class... parameterTypes) {
        return FilterUtil.filter(getAllMethods(type, methodName, parameterTypes), Asserts.isMember, get()).flipToNot();
    }

    public final static Method getAccessibleMethod(Class type, String methodName, Class... parameterTypes) {
        List<Method> methods = getAllMethods(type, methodName, parameterTypes);
        if (methods.isEmpty()) {
            throwErr(type, methodName, parameterTypes);
        }
        return methods.get(0);
    }

    /*
     * -----------------------------------------------------------------------------------------
     * invokers
     * -----------------------------------------------------------------------------------------
     */

    public static Object invoke(String methodName, Object source, Object... arguments) {
        return invoke(false, methodName, source, arguments);
    }

    public static Object invoke(boolean accessAble, String methodName, Object source, Object... arguments) {
        Class clazz = source.getClass();
        if (arguments.length > 0) {
            return invoke(accessAble,
                getDeclaredMethod(clazz, methodName,
                    ClassUtil.getClasses(arguments)
                ), source, arguments);
        } else {
            Method method = getDeclaredMethod(clazz, methodName);
            return invoke(accessAble, method, source);
        }
    }

    public static Object invoke(Method method, Object source, Object... arguments) {
        return invoke(false, method, source, arguments);
    }

    public static Object invoke(boolean accessAble, Method method, Object source, Object... arguments) {
        try {
            Object ret;
            if (accessAble && !ModifierUtil.isAccessible(method)) {
                method.setAccessible(true);
                ret = method.invoke(source, arguments);
                method.setAccessible(false);
            } else {
                ret = method.invoke(source, arguments);
            }
            return ret;
        } catch (Exception e) {
            return ThrowUtil.throwRuntime(e);
        }
    }

    public static Object invokeStatic(String methodName, Class clazz, Object... arguments) {
        return invokeStatic(false, methodName, clazz, arguments);
    }

    public static Object invokeStatic(boolean accessAble, String methodName, Class clazz, Object... arguments) {
        List<Method> methods = getDeclaredMethods(clazz, methodName, ClassUtil.getClasses(arguments));
        Method method = methods.get(0);
        return invokeStatic(accessAble, method, arguments);
    }

    public static Object invokeStatic(Method method, Object... arguments) {
        return invokeStatic(false, method, arguments);
    }

    public static Object invokeStatic(boolean accessAble, Method method, Object... arguments) {
        return invoke(accessAble, method, null, arguments);
    }
}

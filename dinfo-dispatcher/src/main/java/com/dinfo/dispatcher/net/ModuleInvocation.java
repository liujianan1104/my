package com.dinfo.dispatcher.net;

import com.dinfo.dispatcher.core.DispatcherContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.dinfo.dispatcher.util.ObjectUtil.goodbyeNull;
import static com.dinfo.dispatcher.util.ObjectUtil.isEmpty;

/**
 * @author yangxf
 */
public class ModuleInvocation implements Invocation, Serializable {
    private static final long serialVersionUID = -626939806502007566L;

    private String methodName;
    private String[] argTypeNames;
    private Object[] args;

    @Override
    public Response invoke() {
        if (isEmpty(methodName))
            return SimpleResponse.buildFailure("methodName is empty");

        int last = methodName.lastIndexOf('.'),
                len = methodName.length();
        if (last < 1)
            return SimpleResponse.buildFailure("class name is empty ");

        if (last >= len)
            return SimpleResponse.buildFailure("method simple name is empty");

        String clsName = methodName.substring(0, last),
                methodSimpleName = methodName.substring(last + 1);

        try {
            ClassLoader loader = DispatcherContext.getClassLoader();

            Class<?> cls = loader.loadClass(clsName);

            argTypeNames = goodbyeNull(argTypeNames, new String[0]);
            int argCount;
            Class<?>[] argTypes = new Class[argCount = argTypeNames.length];
            for (int i = 0; i < argCount; i++)
                argTypes[i] = loader.loadClass(argTypeNames[i]);

            Method method = cls.getMethod(methodSimpleName, argTypes);
            Object rtObj;
            if (Modifier.isStatic(method.getModifiers()))
                rtObj = method.invoke(null, goodbyeNull(args, new Object[0]));
            else
                rtObj = method.invoke(cls.newInstance(), goodbyeNull(args, new Object[0]));

            return SimpleResponse.buildSuccess(rtObj);
        } catch (Exception e) {
            return SimpleResponse.buildFailure(e.getClass().getName() + " " + e.getMessage());
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public ModuleInvocation setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String[] getArgTypeNames() {
        return argTypeNames;
    }

    public ModuleInvocation setArgTypeNames(String[] argTypeNames) {
        this.argTypeNames = argTypeNames;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public ModuleInvocation setArgs(Object[] args) {
        this.args = args;
        return this;
    }
}

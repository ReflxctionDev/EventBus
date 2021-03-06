package io.github.revxrsal.eventbus.base;

import io.github.revxrsal.eventbus.EventExceptionHandler;
import io.github.revxrsal.eventbus.EventListener;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;

class MethodHandlesEventBus extends BaseEventBus {

    public MethodHandlesEventBus(EventExceptionHandler exceptionHandler, Executor executor, List<Class<? extends Annotation>> annotations, boolean hierarchicalInvocation) {
        super(exceptionHandler, executor, annotations, hierarchicalInvocation);
    }

    @Override protected <T> EventListener<T> createEventListener(@NotNull Object listenerInstnace, @NotNull Method method) {
        MethodHandle handle;
        try {
            if (!method.isAccessible())
                method.setAccessible(true);
            handle = MethodHandles.lookup().unreflect(method);
            if (!(listenerInstnace instanceof Class)) handle = handle.bindTo(listenerInstnace);
        } catch (Throwable t) {
            throw new IllegalStateException("Cannot make method " + method.getName() + " in " + method.getDeclaringClass() + " accessible reflectively. Maybe make it public?");
        }
        MethodHandle methodHandle = handle;
        return methodHandle::invoke;
    }
}

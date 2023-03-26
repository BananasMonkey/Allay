package cn.allay.component.interfaces;

import cn.allay.api.ApiInstanceHolder;

import java.util.List;

/**
 * Author: daoge_cmd <br>
 * Date: 2023/3/4 <br>
 * Allay Project <br>
 * <p>
 * This interface describes a component injector
 */
public interface ComponentInjector<T> {

    /**
     * Create a new injector
     * @return a new injector
     */
    static <U> ComponentInjector<U> createInjector() {
        return ComponentInjectorFactory.FACTORY.get().create();
    }

    /**
     * Defines the parent class for this injector
     *
     * @param parentClass the parent class
     * @return the injector
     */
    ComponentInjector<T> parentClass(Class<T> parentClass);

    /**
     * Bind a set of implementations for the injector <br/>
     * If there are multiple implementation methods for a method to be injected, they will be executed in the order in the component list <p/>
     * and the return value is the return value of the last executed method
     *
     * @param components components
     * @return the injector
     */
    ComponentInjector<T> withComponent(List<? extends ComponentImpl> components);

    /**
     * Build the class<br/>
     * Note that we guarantee that the returned class implements the {@link ComponentedObject} interface
     *
     * @return the class
     */
    Class<T> inject();

    interface ComponentInjectorFactory {

        ApiInstanceHolder<ComponentInjectorFactory> FACTORY = ApiInstanceHolder.create();

        <R> ComponentInjector<R> create();
    }
}
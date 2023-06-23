package cn.allay.api;

/**
 * @author daoge_cmd <br>
 * @date 2023/3/17 <br>
 * Allay Project <br>
 */
public final class ApiInstanceHolder<T> {
    private T instance;

    private ApiInstanceHolder() {

    }

    public static <T> ApiInstanceHolder<T> of() {
        return new ApiInstanceHolder<>();
    }

    public void set(T instance) {
        if (this.instance == null)
            this.instance = instance;
        else throw new IllegalStateException("Instance already set to " + this.instance);
    }

    public T get() {
        return instance;
    }
}

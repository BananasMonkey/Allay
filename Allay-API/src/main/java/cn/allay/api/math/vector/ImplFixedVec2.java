package cn.allay.api.math.vector;

import lombok.EqualsAndHashCode;

/**
 * @author daoge_cmd <br>
 * @date 2023/3/11 <br>
 * Allay Project <br>
 */
@EqualsAndHashCode
class ImplFixedVec2<T extends Number> implements FixedVec2<T> {
    protected T x;
    protected T z;

    public ImplFixedVec2(T x, T z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public T getX() {
        return x;
    }

    @Override
    public T getZ() {
        return z;
    }
}

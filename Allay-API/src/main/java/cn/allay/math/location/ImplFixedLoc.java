package cn.allay.math.location;

import cn.allay.level.Level;
import lombok.EqualsAndHashCode;

/**
 * Author: daoge_cmd <br>
 * Date: 2023/3/11 <br>
 * Allay Project <br>
 */
@EqualsAndHashCode
class ImplFixedLoc<T extends Number> implements FixedLoc<T> {

    protected T x;
    protected T y;
    protected T z;
    protected double yaw;
    protected double headYaw;
    protected double pitch;
    protected Level level;

    public ImplFixedLoc(T x, T y, T z, Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
    }

    public ImplFixedLoc(T x, T y, T z, double yaw, double pitch, Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.level = level;
    }

    public ImplFixedLoc(T x, T y, T z, double yaw, double headYaw, double pitch, Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.pitch = pitch;
        this.level = level;
    }

    @Override
    public T getX() {
        return x;
    }

    @Override
    public T getY() {
        return y;
    }

    @Override
    public T getZ() {
        return z;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
    public double getHeadYaw() {
        return headYaw;
    }

    @Override
    public double getPitch() {
        return pitch;
    }
}
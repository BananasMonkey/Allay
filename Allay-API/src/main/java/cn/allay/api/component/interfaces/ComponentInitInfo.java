package cn.allay.api.component.interfaces;

/**
 * @author daoge_cmd <br>
 * @date 2023/4/21 <br>
 * Allay Project <br>
 */
public interface ComponentInitInfo {

    ComponentInitInfo EMPTY = new Empty();

    class Empty implements ComponentInitInfo {
    }
}

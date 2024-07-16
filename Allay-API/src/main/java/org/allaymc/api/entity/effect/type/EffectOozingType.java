package org.allaymc.api.entity.effect.type;

import org.allaymc.api.entity.effect.AbstractEffectType;
import org.allaymc.api.utils.Identifier;

/**
 * Allay Project 23/06/2024
 *
 * @author IWareQ
 */
public class EffectOozingType extends AbstractEffectType {
    public static final EffectOozingType OOZING_TYPE = new EffectOozingType();

    private EffectOozingType() {
        super(34, new Identifier("minecraft:oozing"), true);
    }
}

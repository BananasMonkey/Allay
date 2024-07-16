package org.allaymc.api.entity.effect.type;

import org.allaymc.api.entity.effect.AbstractEffectType;
import org.allaymc.api.utils.Identifier;

/**
 * Allay Project 23/06/2024
 *
 * @author IWareQ
 */
public class EffectWindChargedType extends AbstractEffectType {
    public EffectWindChargedType() {
        super(32, new Identifier("minecraft:wind_charged"), true);
    }
}

package org.allaymc.api.eventbus.event.entity;

import org.allaymc.api.entity.Entity;
import org.allaymc.api.entity.effect.EffectInstance;

/**
 * @author daoge_cmd
 */
public class EntityEffectAddEvent extends EntityEffectEvent {
    public EntityEffectAddEvent(Entity entity, EffectInstance effect) {
        super(entity, effect);
    }

    public void setEffect(EffectInstance effect) {
        this.effect = effect;
    }
}

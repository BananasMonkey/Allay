package org.allaymc.server.component.impl;

import org.allaymc.server.component.annotation.Identifier;
import org.allaymc.server.component.interfaces.HealthComponent;

/**
 * @author daoge_cmd
 */
public class SimpleHealthComponent implements HealthComponent {
    @Identifier
    public static final org.allaymc.api.utils.Identifier IDENTIFIER = new org.allaymc.api.utils.Identifier("minecraft:health_component");

    protected final int maxHealth;
    protected int health;

    public SimpleHealthComponent(int maxHealth) {
        this(maxHealth, maxHealth);
    }

    public SimpleHealthComponent(int maxHealth, int health) {
        this.maxHealth = maxHealth;
        this.health = health;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }
}

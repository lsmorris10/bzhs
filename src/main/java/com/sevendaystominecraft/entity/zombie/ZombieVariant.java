package com.sevendaystominecraft.entity.zombie;

public enum ZombieVariant {
    WALKER(4.0, 0.32, 1.0, 200, 1),
    CRAWLER(2.4, 0.4, 0.8, 150, 1),
    FROZEN_LUMBERJACK(6.0, 0.48, 0.9, 250, 1),
    BLOATED_WALKER(8.0, 0.4, 0.7, 300, 3),
    SPIDER_ZOMBIE(4.8, 0.56, 1.8, 300, 5),
    FERAL_WIGHT(12.0, 0.8, 2.5, 350, 7),
    COP(14.0, 0.6, 1.2, 400, 14),
    SCREAMER(3.2, 0.2, 1.5, 250, 7),
    ZOMBIE_DOG(3.2, 0.72, 3.5, 200, 3),
    VULTURE(2.4, 0.48, 4.0, 200, 7),
    DEMOLISHER(32.0, 1.2, 1.0, 800, 21),
    MUTATED_CHUCK(10.0, 0.72, 1.3, 350, 14),
    ZOMBIE_BEAR(24.0, 1.4, 2.0, 500, 14),
    NURSE(4.8, 0.4, 1.0, 250, 7),
    SOLDIER(16.0, 1.0, 1.5, 400, 14),
    CHARGED(0, 0, 0, 550, 21),
    INFERNAL(0, 0, 0, 550, 21),
    RADIATED(0, 0, 0, 500, 28),
    BEHEMOTH(80.0, 2.0, 0.8, 2000, 35);

    private final double baseHP;
    private final double baseDamage;
    private final double baseSpeed;
    private final int xpReward;
    private final int minSpawnDay;

    ZombieVariant(double baseHP, double baseDamage, double baseSpeed, int xpReward, int minSpawnDay) {
        this.baseHP = baseHP;
        this.baseDamage = baseDamage;
        this.baseSpeed = baseSpeed;
        this.xpReward = xpReward;
        this.minSpawnDay = minSpawnDay;
    }

    public double getBaseHP() { return baseHP; }
    public double getBaseDamage() { return baseDamage; }
    public double getBaseSpeed() { return baseSpeed; }
    public int getXpReward() { return xpReward; }
    public int getMinSpawnDay() { return minSpawnDay; }

    public boolean isModifier() {
        return this == CHARGED || this == INFERNAL || this == RADIATED;
    }

    public double getModifierHPMultiplier() {
        return switch (this) {
            case CHARGED -> 1.8;
            case INFERNAL -> 1.8;
            case RADIATED -> 2.0;
            default -> 1.0;
        };
    }

    public double getModifierDamageMultiplier() {
        return switch (this) {
            case CHARGED -> 1.3;
            case INFERNAL -> 1.4;
            case RADIATED -> 1.5;
            default -> 1.0;
        };
    }

    public double getModifierSpeedMultiplier() {
        return switch (this) {
            case CHARGED -> 1.2;
            case INFERNAL -> 1.1;
            case RADIATED -> 1.3;
            default -> 1.0;
        };
    }
}

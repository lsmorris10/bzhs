package com.sevendaystominecraft.entity.zombie;

public enum ZombieVariant {
    WALKER(100, 8, 1.0, 200, 1),
    CRAWLER(60, 10, 0.8, 150, 1),
    FROZEN_LUMBERJACK(150, 12, 0.9, 250, 1),
    BLOATED_WALKER(200, 10, 0.7, 300, 3),
    SPIDER_ZOMBIE(120, 14, 1.8, 300, 5),
    FERAL_WIGHT(300, 20, 2.5, 350, 7),
    COP(350, 15, 1.2, 400, 14),
    SCREAMER(80, 5, 1.5, 250, 7),
    ZOMBIE_DOG(80, 18, 3.5, 200, 3),
    VULTURE(60, 12, 4.0, 200, 7),
    DEMOLISHER(800, 30, 1.0, 800, 21),
    MUTATED_CHUCK(250, 18, 1.3, 350, 14),
    ZOMBIE_BEAR(600, 35, 2.0, 500, 14),
    NURSE(120, 10, 1.0, 250, 7),
    SOLDIER(400, 25, 1.5, 400, 14),
    CHARGED(0, 0, 0, 550, 21),
    INFERNAL(0, 0, 0, 550, 21),
    RADIATED(0, 0, 0, 500, 28),
    BEHEMOTH(2000, 50, 0.8, 2000, 35);

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

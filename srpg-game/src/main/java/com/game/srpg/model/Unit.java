package com.game.srpg.model;

import java.util.UUID;

public class Unit {
    private String id;
    private String name;
    private UnitType type;
    private int maxHp;
    private int currentHp;
    private int attackPower;
    private int moveRange;
    private int attackRange;
    private int x;
    private int y;

    public Unit(String name, UnitType type, int x, int y) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;

        // 유닛 타입에 따른 기본 스탯 설정
        switch (type) {
            case WARRIOR:
                this.maxHp = 100;
                this.attackPower = 20;
                this.moveRange = 3;
                this.attackRange = 1;
                break;
            case ARCHER:
                this.maxHp = 70;
                this.attackPower = 15;
                this.moveRange = 3;
                this.attackRange = 3;
                break;
            case MAGE:
                this.maxHp = 60;
                this.attackPower = 25;
                this.moveRange = 2;
                this.attackRange = 2;
                break;
        }

        this.currentHp = this.maxHp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitType getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(currentHp, maxHp);
        this.currentHp = Math.max(this.currentHp, 0);
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    // 유닛 행동 메서드
    public void attack(Unit target) {
        target.takeDamage(this.attackPower);
    }

    public void takeDamage(int damage) {
        this.currentHp -= damage;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
    }

    public boolean isAlive() {
        return this.currentHp > 0;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // todo:
    public boolean isPlayerUnit() { return true; }
}

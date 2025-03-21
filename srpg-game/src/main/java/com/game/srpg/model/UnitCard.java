package com.game.srpg.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 유닛 카드 클래스
 * Unit 클래스를 확장하여 카드로서의 기능을 추가
 */
public class UnitCard extends Card {
    private UnitType unitType;
    private int maxHp;
    private int attackPower;
    private int moveRange;
    private int attackRange;
    private List<String> traits; // 특성 목록 (도발, 속공, 사격 등)
    
    // 기본 생성자 (JSON 역직렬화용)
    public UnitCard() {
        super();
        this.traits = new ArrayList<>();
    }
    
    // 매개변수가 있는 생성자
    public UnitCard(String name, String description, int cost, String faction, String imageUrl, 
                   boolean isUnique, UnitType unitType, int maxHp, int attackPower, 
                   int moveRange, int attackRange) {
        super(name, description, cost, faction, imageUrl, isUnique);
        this.unitType = unitType;
        this.maxHp = maxHp;
        this.attackPower = attackPower;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.traits = new ArrayList<>();
    }
    
    // Unit 객체로 변환하는 메서드
    public Unit toUnit(int x, int y) {
        Unit unit = new Unit(getName(), getUnitType(), x, y);
        unit.setMaxHp(maxHp);
        unit.setCurrentHp(maxHp);
        unit.setAttackPower(attackPower);
        unit.setMoveRange(moveRange);
        unit.setAttackRange(attackRange);
        return unit;
    }
    
    // 카드 사용 시 실행되는 메서드 (유닛 배치)
    @Override
    public boolean play(GameState gameState, int x, int y) {
        // 해당 위치에 유닛을 배치할 수 있는지 확인
        if (!gameState.canPlaceUnit(x, y)) {
            return false;
        }
        
        // 유닛 생성 및 배치
        Unit unit = toUnit(x, y);
        gameState.addUnit(unit);
        return true;
    }
    
    // 특성 추가 메서드
    public void addTrait(String trait) {
        if (!traits.contains(trait)) {
            traits.add(trait);
        }
    }
    
    // 특성 제거 메서드
    public void removeTrait(String trait) {
        traits.remove(trait);
    }
    
    // 특성 확인 메서드
    public boolean hasTrait(String trait) {
        return traits.contains(trait);
    }
    
    @Override
    @JsonIgnore
    public CardType getCardType() {
        return CardType.UNIT;
    }
    
    // Getter와 Setter 메서드
    public UnitType getUnitType() {
        return unitType;
    }
    
    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
    
    public int getMaxHp() {
        return maxHp;
    }
    
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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
    
    public List<String> getTraits() {
        return traits;
    }
    
    public void setTraits(List<String> traits) {
        this.traits = traits;
    }
    
    @Override
    public String toString() {
        return "UnitCard{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", unitType=" + unitType +
                ", maxHp=" + maxHp +
                ", attackPower=" + attackPower +
                ", moveRange=" + moveRange +
                ", attackRange=" + attackRange +
                ", traits=" + traits +
                ", cost=" + getCost() +
                ", faction='" + getFaction() + '\'' +
                ", isUnique=" + isUnique() +
                '}';
    }
}

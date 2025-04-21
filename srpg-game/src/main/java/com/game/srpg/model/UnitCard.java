package com.game.srpg.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 유닛 카드 클래스
 */
public class UnitCard extends Card {
    private int health;
    private int attack;
    private int moveRange;
    private int attackRange;
    private UnitType unitType;
    private List<String> traits;

    // 기본 생성자 (Jackson 직렬화용)
    public UnitCard() {
        this.traits = new ArrayList<>();
    }

    /**
     * 유닛 카드 생성자
     *
     * @param name 카드 이름
     * @param description 카드 설명
     * @param cost 카드 코스트
     * @param faction 카드 소속 팩션
     * @param imageUrl 카드 이미지 URL
     * @param health 유닛 체력
     * @param attack 유닛 공격력
     * @param moveRange 유닛 이동 범위
     * @param attackRange 유닛 공격 범위
     * @param unitType 유닛 타입
     */
    public UnitCard(String name, String description, int cost, String faction, String imageUrl,
                    int health, int attack, int moveRange, int attackRange, UnitType unitType) {
        super(name, description, cost, faction, imageUrl);
        this.health = health;
        this.attack = attack;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.unitType = unitType;
        this.traits = new ArrayList<>();
    }

    /**
     * 특성 추가
     *
     * @param trait 추가할 특성
     */
    public void addTrait(String trait) {
        if (!traits.contains(trait)) {
            traits.add(trait);
        }
    }

    /**
     * 특성 제거
     *
     * @param trait 제거할 특성
     * @return 제거 성공 여부
     */
    public boolean removeTrait(String trait) {
        return traits.remove(trait);
    }

    /**
     * 특성 보유 여부 확인
     *
     * @param trait 확인할 특성
     * @return 특성 보유 여부
     */
    public boolean hasTrait(String trait) {
        return traits.contains(trait);
    }

    @Override
    @JsonIgnore
    public CardType getCardType() {
        return CardType.UNIT;
    }

    // Getter와 Setter 메서드
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
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

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }
}

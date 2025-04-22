package com.game.srpg.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * 유닛 카드 클래스
 */
@Getter
@Setter
public class UnitCard extends Card {
    private int health;
    private int attack;
    private int moveRange;
    private int attackRange;
    private UnitType unitType;
    private List<String> traits;
    /** 여러 개의 특수 능력(기능) 식별자 리스트 */
    private List<String> abilities;
    /** 카드 등급(e.g., Common, Rare, Epic 등) */
    private String grade;

    // 기본 생성자 (Jackson 직렬화용)
    public UnitCard() {
        this.traits = new ArrayList<>();
        this.abilities = new ArrayList<>();
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

    // Lombok @Getter/@Setter가 자동으로 생성합니다.
}

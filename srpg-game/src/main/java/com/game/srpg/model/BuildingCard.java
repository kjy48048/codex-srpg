package com.game.srpg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * 건물 카드 클래스
 */
@Getter
@Setter
public class BuildingCard extends Card {
    private int health;
    private int constructionTime;
    private int size;
    private String effectType;
    private int effectValue;

    // 기본 생성자 (Jackson 직렬화용)
    public BuildingCard() {
    }

    /**
     * 건물 카드 생성자
     *
     * @param name 카드 이름
     * @param description 카드 설명
     * @param cost 카드 코스트
     * @param faction 카드 소속 팩션
     * @param imageUrl 카드 이미지 URL
     * @param health 건물 체력
     * @param constructionTime 건설 시간(턴)
     * @param size 건물 크기
     * @param effectType 효과 타입 (DEFENSE_BONUS, RESOURCE_GENERATION, UNIT_PRODUCTION 등)
     * @param effectValue 효과 수치
     */
    public BuildingCard(String name, String description, int cost, String faction, String imageUrl,
                        int health, int constructionTime, int size, String effectType, int effectValue) {
        super(name, description, cost, faction, imageUrl);
        this.health = health;
        this.constructionTime = constructionTime;
        this.size = size;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }

    @Override
    @JsonIgnore
    public CardType getCardType() {
        return CardType.BUILDING;
    }
}

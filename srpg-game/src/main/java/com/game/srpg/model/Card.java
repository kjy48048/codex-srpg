package com.game.srpg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 게임에서 사용되는 모든 카드의 기본 클래스
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = UnitCard.class, name = "UNIT"),
        @Type(value = TacticCard.class, name = "TACTIC"),
        @Type(value = BuildingCard.class, name = "BUILDING")
})
@Getter
@Setter
public abstract class Card {
    private String name;
    private String description;
    private int cost;
    private String faction;
    private String imageUrl;

    // 기본 생성자 (Jackson 직렬화용)
    protected Card() {
    }

    /**
     * 카드 생성자
     *
     * @param name 카드 이름
     * @param description 카드 설명
     * @param cost 카드 코스트
     * @param faction 카드 소속 팩션
     * @param imageUrl 카드 이미지 URL
     */
    protected Card(String name, String description, int cost, String faction, String imageUrl) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.faction = faction;
        this.imageUrl = imageUrl;
    }

    /**
     * 카드 유형 반환
     *
     * @return 카드 유형 (UNIT, TACTIC, BUILDING)
     */
    @JsonIgnore
    public abstract CardType getCardType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cost == card.cost &&
                Objects.equals(name, card.name) &&
                Objects.equals(description, card.description) &&
                Objects.equals(faction, card.faction) &&
                Objects.equals(imageUrl, card.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, faction, imageUrl);
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", faction='" + faction + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

package com.game.srpg.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 카드 기본 클래스
 * 모든 카드 유형의 공통 속성을 포함
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "cardType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnitCard.class, name = "UNIT"),
    @JsonSubTypes.Type(value = TacticCard.class, name = "TACTIC"),
    @JsonSubTypes.Type(value = BuildingCard.class, name = "BUILDING")
})
public abstract class Card {
    private String id;
    private String name;
    private String description;
    private int cost;
    private String faction;
    private String imageUrl;
    private boolean isUnique;
    
    // 기본 생성자 (JSON 역직렬화용)
    public Card() {
        this.id = UUID.randomUUID().toString();
    }
    
    // 매개변수가 있는 생성자
    public Card(String name, String description, int cost, String faction, String imageUrl, boolean isUnique) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.faction = faction;
        this.imageUrl = imageUrl;
        this.isUnique = isUnique;
    }
    
    // 카드 유형을 반환하는 추상 메서드
    @JsonIgnore
    public abstract CardType getCardType();
    
    // 카드 사용 시 실행되는 추상 메서드
    public abstract boolean play(GameState gameState, int x, int y);
    
    // Getter와 Setter 메서드
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCost() {
        return cost;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    public String getFaction() {
        return faction;
    }
    
    public void setFaction(String faction) {
        this.faction = faction;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public boolean isUnique() {
        return isUnique;
    }
    
    public void setUnique(boolean unique) {
        isUnique = unique;
    }
    
    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", faction='" + faction + '\'' +
                ", isUnique=" + isUnique +
                '}';
    }
}

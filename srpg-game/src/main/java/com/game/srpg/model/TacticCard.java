package com.game.srpg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 전술(마법) 카드 클래스
 */
public class TacticCard extends Card {
    private String effectType;
    private int effectValue;
    private int range;
    private int duration;
    private boolean isAreaEffect;

    // 기본 생성자 (Jackson 직렬화용)
    public TacticCard() {
    }

    /**
     * 전술 카드 생성자
     *
     * @param name 카드 이름
     * @param description 카드 설명
     * @param cost 카드 코스트
     * @param faction 카드 소속 팩션
     * @param imageUrl 카드 이미지 URL
     * @param effectType 효과 타입 (DAMAGE, HEAL, BUFF, DEBUFF 등)
     * @param effectValue 효과 수치
     * @param range 효과 범위
     * @param duration 효과 지속 시간 (0은 즉시 효과)
     * @param isAreaEffect 광역 효과 여부
     */
    public TacticCard(String name, String description, int cost, String faction, String imageUrl,
                      String effectType, int effectValue, int range, int duration, boolean isAreaEffect) {
        super(name, description, cost, faction, imageUrl);
        this.effectType = effectType;
        this.effectValue = effectValue;
        this.range = range;
        this.duration = duration;
        this.isAreaEffect = isAreaEffect;
    }

    @Override
    @JsonIgnore
    public CardType getCardType() {
        return CardType.TACTIC;
    }

    // Getter와 Setter 메서드
    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(int effectValue) {
        this.effectValue = effectValue;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isAreaEffect() {
        return isAreaEffect;
    }

    public void setAreaEffect(boolean areaEffect) {
        isAreaEffect = areaEffect;
    }
}

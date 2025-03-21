package com.game.srpg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 전술(마법) 카드 클래스
 * 전투 중 일시적인 효과를 제공하는 카드
 */
public class TacticCard extends Card {
    private String effectType;     // 효과 유형 (공격, 방어, 버프, 디버프 등)
    private int effectPower;       // 효과 강도
    private int effectRange;       // 효과 범위
    private int effectDuration;    // 효과 지속 시간 (턴 단위, 0은 즉시 효과)
    private boolean isAreaEffect;  // 범위 효과 여부
    
    // 기본 생성자 (JSON 역직렬화용)
    public TacticCard() {
        super();
    }
    
    // 매개변수가 있는 생성자
    public TacticCard(String name, String description, int cost, String faction, String imageUrl, 
                     boolean isUnique, String effectType, int effectPower, int effectRange, 
                     int effectDuration, boolean isAreaEffect) {
        super(name, description, cost, faction, imageUrl, isUnique);
        this.effectType = effectType;
        this.effectPower = effectPower;
        this.effectRange = effectRange;
        this.effectDuration = effectDuration;
        this.isAreaEffect = isAreaEffect;
    }
    
    // 카드 사용 시 실행되는 메서드 (전술 효과 적용)
    @Override
    public boolean play(GameState gameState, int x, int y) {
        // 해당 위치에 전술 카드를 사용할 수 있는지 확인
        if (!gameState.canUseTactic(x, y, this)) {
            return false;
        }
        
        // 효과 유형에 따라 다른 로직 적용
        switch (effectType) {
            case "DAMAGE":
                // 데미지 효과 적용
                if (isAreaEffect) {
                    return applyAreaDamage(gameState, x, y);
                } else {
                    return applySingleTargetDamage(gameState, x, y);
                }
            case "HEAL":
                // 치유 효과 적용
                if (isAreaEffect) {
                    return applyAreaHeal(gameState, x, y);
                } else {
                    return applySingleTargetHeal(gameState, x, y);
                }
            case "BUFF":
                // 버프 효과 적용
                if (isAreaEffect) {
                    return applyAreaBuff(gameState, x, y);
                } else {
                    return applySingleTargetBuff(gameState, x, y);
                }
            case "DEBUFF":
                // 디버프 효과 적용
                if (isAreaEffect) {
                    return applyAreaDebuff(gameState, x, y);
                } else {
                    return applySingleTargetDebuff(gameState, x, y);
                }
            default:
                return false;
        }
    }
    
    // 단일 대상 데미지 적용
    private boolean applySingleTargetDamage(GameState gameState, int x, int y) {
        Unit targetUnit = gameState.getUnitAt(x, y);
        if (targetUnit == null) {
            return false;
        }
        
        int currentHp = targetUnit.getCurrentHp();
        targetUnit.setCurrentHp(Math.max(0, currentHp - effectPower));
        
        // 유닛이 죽었는지 확인
        if (targetUnit.getCurrentHp() <= 0) {
            gameState.removeUnit(targetUnit);
        }
        
        return true;
    }
    
    // 범위 데미지 적용
    private boolean applyAreaDamage(GameState gameState, int x, int y) {
        boolean affected = false;
        
        // 효과 범위 내의 모든 유닛에 데미지 적용
        for (int i = x - effectRange; i <= x + effectRange; i++) {
            for (int j = y - effectRange; j <= y + effectRange; j++) {
                Unit targetUnit = gameState.getUnitAt(i, j);
                if (targetUnit != null) {
                    int currentHp = targetUnit.getCurrentHp();
                    targetUnit.setCurrentHp(Math.max(0, currentHp - effectPower));
                    
                    // 유닛이 죽었는지 확인
                    if (targetUnit.getCurrentHp() <= 0) {
                        gameState.removeUnit(targetUnit);
                    }
                    
                    affected = true;
                }
            }
        }
        
        return affected;
    }
    
    // 단일 대상 치유 적용
    private boolean applySingleTargetHeal(GameState gameState, int x, int y) {
        Unit targetUnit = gameState.getUnitAt(x, y);
        if (targetUnit == null) {
            return false;
        }
        
        int currentHp = targetUnit.getCurrentHp();
        int maxHp = targetUnit.getMaxHp();
        targetUnit.setCurrentHp(Math.min(maxHp, currentHp + effectPower));
        
        return true;
    }
    
    // 범위 치유 적용
    private boolean applyAreaHeal(GameState gameState, int x, int y) {
        boolean affected = false;
        
        // 효과 범위 내의 모든 아군 유닛에 치유 적용
        for (int i = x - effectRange; i <= x + effectRange; i++) {
            for (int j = y - effectRange; j <= y + effectRange; j++) {
                Unit targetUnit = gameState.getUnitAt(i, j);
                if (targetUnit != null && targetUnit.isPlayerUnit()) {
                    int currentHp = targetUnit.getCurrentHp();
                    int maxHp = targetUnit.getMaxHp();
                    targetUnit.setCurrentHp(Math.min(maxHp, currentHp + effectPower));
                    affected = true;
                }
            }
        }
        
        return affected;
    }
    
    // 단일 대상 버프 적용 (임시 구현)
    private boolean applySingleTargetBuff(GameState gameState, int x, int y) {
        // 실제 구현에서는 버프 효과를 적용하는 로직 추가
        return true;
    }
    
    // 범위 버프 적용 (임시 구현)
    private boolean applyAreaBuff(GameState gameState, int x, int y) {
        // 실제 구현에서는 범위 버프 효과를 적용하는 로직 추가
        return true;
    }
    
    // 단일 대상 디버프 적용 (임시 구현)
    private boolean applySingleTargetDebuff(GameState gameState, int x, int y) {
        // 실제 구현에서는 디버프 효과를 적용하는 로직 추가
        return true;
    }
    
    // 범위 디버프 적용 (임시 구현)
    private boolean applyAreaDebuff(GameState gameState, int x, int y) {
        // 실제 구현에서는 범위 디버프 효과를 적용하는 로직 추가
        return true;
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
    
    public int getEffectPower() {
        return effectPower;
    }
    
    public void setEffectPower(int effectPower) {
        this.effectPower = effectPower;
    }
    
    public int getEffectRange() {
        return effectRange;
    }
    
    public void setEffectRange(int effectRange) {
        this.effectRange = effectRange;
    }
    
    public int getEffectDuration() {
        return effectDuration;
    }
    
    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }
    
    public boolean isAreaEffect() {
        return isAreaEffect;
    }
    
    public void setAreaEffect(boolean areaEffect) {
        isAreaEffect = areaEffect;
    }
    
    @Override
    public String toString() {
        return "TacticCard{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", effectType='" + effectType + '\'' +
                ", effectPower=" + effectPower +
                ", effectRange=" + effectRange +
                ", effectDuration=" + effectDuration +
                ", isAreaEffect=" + isAreaEffect +
                ", cost=" + getCost() +
                ", faction='" + getFaction() + '\'' +
                ", isUnique=" + isUnique() +
                '}';
    }
}

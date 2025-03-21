package com.game.srpg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 건물 카드 클래스
 * 맵에 배치하여 지속적인 효과를 제공하는 카드
 */
public class BuildingCard extends Card {
    private int maxHp;
    private int defenseBonus;
    private int resourceGeneration;
    private int influenceRange;
    private String buildingEffect;  // 건물 효과 유형 (자원 생산, 방어 보너스, 유닛 생산 등)
    
    // 기본 생성자 (JSON 역직렬화용)
    public BuildingCard() {
        super();
    }
    
    // 매개변수가 있는 생성자
    public BuildingCard(String name, String description, int cost, String faction, String imageUrl, 
                       boolean isUnique, int maxHp, int defenseBonus, int resourceGeneration, 
                       int influenceRange, String buildingEffect) {
        super(name, description, cost, faction, imageUrl, isUnique);
        this.maxHp = maxHp;
        this.defenseBonus = defenseBonus;
        this.resourceGeneration = resourceGeneration;
        this.influenceRange = influenceRange;
        this.buildingEffect = buildingEffect;
    }
    
    // 카드 사용 시 실행되는 메서드 (건물 배치)
    @Override
    public boolean play(GameState gameState, int x, int y) {
        // 해당 위치에 건물을 배치할 수 있는지 확인
        if (!gameState.canPlaceBuilding(x, y)) {
            return false;
        }
        
        // 건물 생성 및 배치 (실제 구현에서는 Building 클래스 필요)
        // 임시 구현: Tile에 건물 정보 설정
        Tile tile = gameState.getTileAt(x, y);
        if (tile != null) {
            tile.setOccupied(true);
            tile.setDescription(getName() + " (건물)");
            
            // 건물 효과 적용
            applyBuildingEffect(gameState, x, y);
            return true;
        }
        
        return false;
    }
    
    // 건물 효과 적용 메서드
    private void applyBuildingEffect(GameState gameState, int x, int y) {
        switch (buildingEffect) {
            case "RESOURCE_GENERATION":
                // 자원 생성 효과 (실제 구현에서는 자원 시스템 필요)
                break;
                
            case "DEFENSE_BONUS":
                // 방어 보너스 효과 적용
                applyDefenseBonusEffect(gameState, x, y);
                break;
                
            case "UNIT_PRODUCTION":
                // 유닛 생산 효과 (실제 구현에서는 유닛 생산 시스템 필요)
                break;
                
            case "HEALING":
                // 치유 효과 적용
                applyHealingEffect(gameState, x, y);
                break;
                
            default:
                // 기본 효과 없음
                break;
        }
    }
    
    // 방어 보너스 효과 적용 메서드
    private void applyDefenseBonusEffect(GameState gameState, int x, int y) {
        // 영향 범위 내의 모든 아군 유닛에 방어 보너스 적용
        // (실제 구현에서는 유닛에 방어력 속성 추가 필요)
        for (int i = x - influenceRange; i <= x + influenceRange; i++) {
            for (int j = y - influenceRange; j <= y + influenceRange; j++) {
                Unit unit = gameState.getUnitAt(i, j);
                if (unit != null && unit.isPlayerUnit()) {
                    // 방어 보너스 적용 (임시 구현)
                    // unit.setDefense(unit.getDefense() + defenseBonus);
                }
            }
        }
    }
    
    // 치유 효과 적용 메서드
    private void applyHealingEffect(GameState gameState, int x, int y) {
        // 영향 범위 내의 모든 아군 유닛에 치유 효과 적용
        for (int i = x - influenceRange; i <= x + influenceRange; i++) {
            for (int j = y - influenceRange; j <= y + influenceRange; j++) {
                Unit unit = gameState.getUnitAt(i, j);
                if (unit != null && unit.isPlayerUnit()) {
                    int currentHp = unit.getCurrentHp();
                    int maxHp = unit.getMaxHp();
                    // 매 턴마다 일정량 회복 (실제 구현에서는 턴 시스템과 연동 필요)
                    unit.setCurrentHp(Math.min(maxHp, currentHp + 5));
                }
            }
        }
    }
    
    @Override
    @JsonIgnore
    public CardType getCardType() {
        return CardType.BUILDING;
    }
    
    // Getter와 Setter 메서드
    public int getMaxHp() {
        return maxHp;
    }
    
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    
    public int getDefenseBonus() {
        return defenseBonus;
    }
    
    public void setDefenseBonus(int defenseBonus) {
        this.defenseBonus = defenseBonus;
    }
    
    public int getResourceGeneration() {
        return resourceGeneration;
    }
    
    public void setResourceGeneration(int resourceGeneration) {
        this.resourceGeneration = resourceGeneration;
    }
    
    public int getInfluenceRange() {
        return influenceRange;
    }
    
    public void setInfluenceRange(int influenceRange) {
        this.influenceRange = influenceRange;
    }
    
    public String getBuildingEffect() {
        return buildingEffect;
    }
    
    public void setBuildingEffect(String buildingEffect) {
        this.buildingEffect = buildingEffect;
    }
    
    @Override
    public String toString() {
        return "BuildingCard{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", maxHp=" + maxHp +
                ", defenseBonus=" + defenseBonus +
                ", resourceGeneration=" + resourceGeneration +
                ", influenceRange=" + influenceRange +
                ", buildingEffect='" + buildingEffect + '\'' +
                ", cost=" + getCost() +
                ", faction='" + getFaction() + '\'' +
                ", isUnique=" + isUnique() +
                '}';
    }
}

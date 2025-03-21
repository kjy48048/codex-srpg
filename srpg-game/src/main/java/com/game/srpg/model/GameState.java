package com.game.srpg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameState {
    private String id;
    private int width;
    private int height;
    private Map<String, Tile> tiles;
    private List<Unit> playerUnits;
    private List<Unit> enemyUnits;
    private boolean playerTurn;

    // 플레이어 덱
    private List<Card> playerDeck;

    // 플레이어 손패
    private List<Card> playerHand;

    // 플레이어 사용 가능한 코스트
    private int playerCost;

    // 턴당 최대 코스트
    private int maxCostPerTurn;

    public GameState(int width, int height) {
        this.id = UUID.randomUUID().toString();
        this.width = width;
        this.height = height;
        this.tiles = new HashMap<>();
        this.playerUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.playerTurn = true;

        initializeMap();
        initializeUnits();
    }

    private void initializeMap() {
        // 맵 초기화
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = new Tile(x, y);
                tiles.put(x + "," + y, tile);
            }
        }
    }

    private void initializeUnits() {
        // 플레이어 유닛 초기화
        Unit warrior = new Unit("전사", UnitType.WARRIOR, 1, 1);
        warrior.setMaxHp(100);
        warrior.setCurrentHp(100);
        warrior.setAttackPower(20);
        warrior.setMoveRange(3);
        warrior.setAttackRange(1);
        playerUnits.add(warrior);

        Unit archer = new Unit("궁수", UnitType.ARCHER, 2, 2);
        archer.setMaxHp(70);
        archer.setCurrentHp(70);
        archer.setAttackPower(15);
        archer.setMoveRange(3);
        archer.setAttackRange(3);
        playerUnits.add(archer);

        Unit mage = new Unit("마법사", UnitType.MAGE, 3, 3);
        mage.setMaxHp(60);
        mage.setCurrentHp(60);
        mage.setAttackPower(25);
        mage.setMoveRange(2);
        mage.setAttackRange(2);
        playerUnits.add(mage);

        // 타일에 유닛 배치
        getTile(1, 1).setUnit(warrior);
        getTile(2, 2).setUnit(archer);
        getTile(3, 3).setUnit(mage);

        // 적 유닛 초기화
        Unit enemyWarrior = new Unit("적 전사", UnitType.WARRIOR, 8, 8);
        enemyWarrior.setMaxHp(100);
        enemyWarrior.setCurrentHp(100);
        enemyWarrior.setAttackPower(20);
        enemyWarrior.setMoveRange(3);
        enemyWarrior.setAttackRange(1);
        enemyUnits.add(enemyWarrior);

        Unit enemyArcher = new Unit("적 궁수", UnitType.ARCHER, 7, 7);
        enemyArcher.setMaxHp(70);
        enemyArcher.setCurrentHp(70);
        enemyArcher.setAttackPower(15);
        enemyArcher.setMoveRange(3);
        enemyArcher.setAttackRange(3);
        enemyUnits.add(enemyArcher);

        // 타일에 적 유닛 배치
        getTile(8, 8).setUnit(enemyWarrior);
        getTile(7, 7).setUnit(enemyArcher);
    }

    public Tile getTile(int x, int y) {
        return tiles.get(x + "," + y);
    }

    public void endTurn() {
        playerTurn = !playerTurn;
    }

    public List<Tile> calculateMoveRange(Unit unit) {
        List<Tile> tilesInRange = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();
        List<MoveNode> queue = new ArrayList<>();

        queue.add(new MoveNode(unit.getX(), unit.getY(), 0));
        visited.put(unit.getX() + "," + unit.getY(), true);

        while (!queue.isEmpty()) {
            MoveNode current = queue.remove(0);

            if (current.distance <= unit.getMoveRange()) {
                // 시작 위치가 아니면 이동 범위에 추가
                if (current.x != unit.getX() || current.y != unit.getY()) {
                    tilesInRange.add(getTile(current.x, current.y));
                }

                // 상하좌우 타일 확인
                int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

                for (int[] dir : directions) {
                    int newX = current.x + dir[0];
                    int newY = current.y + dir[1];

                    // 맵 범위 내에 있는지 확인
                    if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                        String key = newX + "," + newY;
                        Tile tile = getTile(newX, newY);

                        // 방문하지 않았고, 이동 가능하며, 유닛이 없는 타일인지 확인
                        if (!visited.containsKey(key) && tile.isWalkable() && tile.getUnit() == null) {
                            visited.put(key, true);
                            queue.add(new MoveNode(newX, newY, current.distance + tile.getMovementCost()));
                        }
                    }
                }
            }
        }

        return tilesInRange;
    }

    // 이동 노드 내부 클래스
    private static class MoveNode {
        int x;
        int y;
        int distance;

        MoveNode(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

    // Getter와 Setter
    public String getId() {
        return id;
    }

    public Map<String, Tile> getTiles() {
        return tiles;
    }

    public List<Unit> getPlayerUnits() {
        return playerUnits;
    }

    public List<Unit> getEnemyUnits() {
        return enemyUnits;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    // 카드 시스템 초기화 메서드
    public void initializeCardSystem() {
        playerDeck = new ArrayList<>();
        playerHand = new ArrayList<>();
        maxCostPerTurn = 6; // 기본값
        resetPlayerCost();
    }

    // 플레이어 코스트 리셋 (턴 시작 시 호출)
    public void resetPlayerCost() {
        playerCost = maxCostPerTurn;
    }

    // 카드 사용 가능 여부 확인
    public boolean canUseCard(Card card) {
        return playerCost >= card.getCost();
    }

    // 카드 사용 (코스트 차감)
    public void useCard(Card card) {
        playerCost -= card.getCost();
    }

    // 덱에 카드 추가
    public void addCardToDeck(Card card) {
        playerDeck.add(card);
    }

    // 덱에서 카드 제거
    public void removeCardFromDeck(Card card) {
        playerDeck.remove(card);
    }

    // 덱에서 카드 드로우
    public Card drawCardFromDeck() {
        if (playerDeck.isEmpty()) {
            return null;
        }

        Card card = playerDeck.remove(0);
        playerHand.add(card);
        return card;
    }

    // 초기 손패 드로우
    public void drawInitialHand(int count) {
        for (int i = 0; i < count && !playerDeck.isEmpty(); i++) {
            drawCardFromDeck();
        }
    }

    // 유닛 배치 가능 여부 확인
    public boolean canPlaceUnit(int x, int y) {
        // 맵 범위 확인
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }

        // 타일 점유 여부 확인
        Tile tile = getTileAt(x, y);
        return tile != null && !tile.isOccupied();
    }

    // 건물 배치 가능 여부 확인
    public boolean canPlaceBuilding(int x, int y) {
        // 맵 범위 확인
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }

        // 타일 점유 여부 확인
        Tile tile = getTileAt(x, y);
        return tile != null && !tile.isOccupied();
    }

    // 전술 카드 사용 가능 여부 확인
    public boolean canUseTactic(int x, int y, TacticCard tacticCard) {
        // 맵 범위 확인
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }

        // 효과 유형에 따라 다른 조건 확인
        switch (tacticCard.getEffectType()) {
            case "DAMAGE":
                // 데미지 효과는 적 유닛이 있는 위치에만 사용 가능
                Unit targetUnit = getUnitAt(x, y);
                return targetUnit != null && !targetUnit.isPlayerUnit();

            case "HEAL":
                // 치유 효과는 아군 유닛이 있는 위치에만 사용 가능
                targetUnit = getUnitAt(x, y);
                return targetUnit != null && targetUnit.isPlayerUnit();

            case "BUFF":
                // 버프 효과는 아군 유닛이 있는 위치에만 사용 가능
                targetUnit = getUnitAt(x, y);
                return targetUnit != null && targetUnit.isPlayerUnit();

            case "DEBUFF":
                // 디버프 효과는 적 유닛이 있는 위치에만 사용 가능
                targetUnit = getUnitAt(x, y);
                return targetUnit != null && !targetUnit.isPlayerUnit();

            default:
                return false;
        }
    }

    // 덱 생성 (팩션 기반)
    public void createDeck(String faction) {
        playerDeck.clear();

        // 팩션별 카드 추가 (예시)
        if ("HUMAN_KINGDOM".equals(faction)) {
            // 인간 왕국 팩션 카드
            addHumanKingdomCards();
        } else if ("BEAST_EMPIRE".equals(faction)) {
            // 수인 제국 팩션 카드
            addBeastEmpireCards();
        } else if ("MAGE_TOWER".equals(faction)) {
            // 마법사 탑 팩션 카드
            addMageTowerCards();
        } else if ("UNDEAD_HORDE".equals(faction)) {
            // 언데드 호드 팩션 카드
            addUndeadHordeCards();
        } else {
            // 기본 카드 (팩션 무관)
            addDefaultCards();
        }

        // 덱 셔플
        shuffleDeck();
    }

    // 덱 셔플
    private void shuffleDeck() {
        // 덱 셔플 로직 구현
        java.util.Collections.shuffle(playerDeck);
    }

    // 인간 왕국 팩션 카드 추가 (예시)
    private void addHumanKingdomCards() {
        // 군주 카드
        UnitCard kingCard = new UnitCard(
                "인간 왕", "인간 왕국의 군주", 5, "HUMAN_KINGDOM", "king.png",
                true, UnitType.RULER, 150, 25, 3, 1
        );
        kingCard.addTrait("LEADERSHIP");
        playerDeck.add(kingCard);

        // 영웅 카드
        UnitCard knightCommanderCard = new UnitCard(
                "기사단장", "왕국 기사단을 이끄는 영웅", 4, "HUMAN_KINGDOM", "knight_commander.png",
                true, UnitType.HERO, 120, 30, 4, 1
        );
        knightCommanderCard.addTrait("CHARGE");
        knightCommanderCard.addTrait("INSPIRE");
        playerDeck.add(knightCommanderCard);

        // 일반 유닛 카드
        for (int i = 0; i < 3; i++) {
            UnitCard knightCard = new UnitCard(
                    "왕국 기사", "충성스러운 기사", 3, "HUMAN_KINGDOM", "knight.png",
                    false, UnitType.WARRIOR, 80, 20, 3, 1
            );
            knightCard.addTrait("CHARGE");
            playerDeck.add(knightCard);
        }

        for (int i = 0; i < 4; i++) {
            UnitCard footmanCard = new UnitCard(
                    "보병", "왕국의 충직한 보병", 2, "HUMAN_KINGDOM", "footman.png",
                    false, UnitType.WARRIOR, 60, 15, 2, 1
            );
            footmanCard.addTrait("TAUNT");
            playerDeck.add(footmanCard);
        }

        for (int i = 0; i < 3; i++) {
            UnitCard archerCard = new UnitCard(
                    "궁수", "숙련된 왕국 궁수", 2, "HUMAN_KINGDOM", "archer.png",
                    false, UnitType.RANGER, 40, 15, 2, 3
            );
            archerCard.addTrait("RANGED");
            playerDeck.add(archerCard);
        }

        // 전술 카드
        for (int i = 0; i < 2; i++) {
            TacticCard rallyCryCard = new TacticCard(
                    "전투 함성", "아군 유닛들의 사기를 높입니다", 2, "HUMAN_KINGDOM", "rally_cry.png",
                    false, "BUFF", 10, 2, 2, true
            );
            playerDeck.add(rallyCryCard);
        }

        TacticCard divineProtectionCard = new TacticCard(
                "신의 가호", "아군 유닛에게 신성한 보호막을 부여합니다", 3, "HUMAN_KINGDOM", "divine_protection.png",
                false, "BUFF", 0, 0, 3, false
        );
        playerDeck.add(divineProtectionCard);

        // 건물 카드
        BuildingCard castleCard = new BuildingCard(
                "성", "강력한 방어 요새", 5, "HUMAN_KINGDOM", "castle.png",
                true, 200, 20, 0, 3, "DEFENSE_BONUS"
        );
        playerDeck.add(castleCard);

        BuildingCard barracksCard = new BuildingCard(
                "병영", "유닛을 훈련시키는 건물", 3, "HUMAN_KINGDOM", "barracks.png",
                false, 100, 5, 1, 2, "UNIT_PRODUCTION"
        );
        playerDeck.add(barracksCard);
    }

    // 수인 제국 팩션 카드 추가 (예시)
    private void addBeastEmpireCards() {
        // 군주 카드
        UnitCard dragonEmperorCard = new UnitCard(
                "용황제", "수인 제국의 군주", 5, "BEAST_EMPIRE", "dragon_emperor.png",
                true, UnitType.RULER, 140, 30, 4, 2
        );
        dragonEmperorCard.addTrait("FLIGHT");
        dragonEmperorCard.addTrait("LEADERSHIP");
        playerDeck.add(dragonEmperorCard);

        // 영웅 카드
        UnitCard monkeyKingCard = new UnitCard(
                "원숭이 왕", "전설적인 무술의 달인", 4, "BEAST_EMPIRE", "monkey_king.png",
                true, UnitType.HERO, 100, 35, 5, 1
        );
        monkeyKingCard.addTrait("SWIFT");
        monkeyKingCard.addTrait("EVASION");
        playerDeck.add(monkeyKingCard);

        // 일반 유닛 카드
        for (int i = 0; i < 3; i++) {
            UnitCard tigerWarriorCard = new UnitCard(
                    "호랑이 전사", "맹렬한 호랑이 전사", 3, "BEAST_EMPIRE", "tiger_warrior.png",
                    false, UnitType.WARRIOR, 70, 25, 4, 1
            );
            tigerWarriorCard.addTrait("SWIFT");
            tigerWarriorCard.addTrait("FEROCITY");
            playerDeck.add(tigerWarriorCard);
        }

        for (int i = 0; i < 4; i++) {
            UnitCard canineInfantryCard = new UnitCard(
                    "견인 보병", "충성스러운 개과 보병", 2, "BEAST_EMPIRE", "canine_infantry.png",
                    false, UnitType.WARRIOR, 60, 15, 3, 1
            );
            canineInfantryCard.addTrait("PACK_TACTICS");
            playerDeck.add(canineInfantryCard);
        }

        for (int i = 0; i < 3; i++) {
            UnitCard centaurArcherCard = new UnitCard(
                    "켄타우로스 궁수", "빠르고 정확한 켄타우로스 궁수", 3, "BEAST_EMPIRE", "centaur_archer.png",
                    false, UnitType.RANGER, 50, 20, 4, 3
            );
            centaurArcherCard.addTrait("RANGED");
            centaurArcherCard.addTrait("SHOOT_AND_MOVE");
            playerDeck.add(centaurArcherCard);
        }

        // 전술 카드
        for (int i = 0; i < 2; i++) {
            TacticCard primalRoarCard = new TacticCard(
                    "원시적 포효", "적 유닛들을 공포에 떨게 합니다", 2, "BEAST_EMPIRE", "primal_roar.png",
                    false, "DEBUFF", 0, 2, 1, true
            );
            playerDeck.add(primalRoarCard);
        }

        TacticCard wildRageCard = new TacticCard(
                "야생의 분노", "아군 유닛에게 광폭한 힘을 부여합니다", 3, "BEAST_EMPIRE", "wild_rage.png",
                false, "BUFF", 15, 0, 2, false
        );
        playerDeck.add(wildRageCard);

        // 건물 카드
        BuildingCard imperialPalaceCard = new BuildingCard(
                "제국 궁전", "수인 제국의 중심지", 5, "BEAST_EMPIRE", "imperial_palace.png",
                true, 180, 15, 2, 3, "RESOURCE_GENERATION"
        );
        playerDeck.add(imperialPalaceCard);

        BuildingCard trainingGroundsCard = new BuildingCard(
                "훈련장", "수인 전사들을 훈련시키는 장소", 3, "BEAST_EMPIRE", "training_grounds.png",
                false, 90, 5, 0, 2, "UNIT_PRODUCTION"
        );
        playerDeck.add(trainingGroundsCard);
    }

    // 마법사 탑 팩션 카드 추가 (예시)
    private void addMageTowerCards() {
        // 임시 구현 (실제 구현 시 확장)
    }

    // 언데드 호드 팩션 카드 추가 (예시)
    private void addUndeadHordeCards() {
        // 임시 구현 (실제 구현 시 확장)
    }

    // 기본 카드 추가 (예시)
    private void addDefaultCards() {
        // 임시 구현 (실제 구현 시 확장)
    }

    // GameState 클래스의 기존 메서드 (참조용)
    public int getWidth() {
        return 10; // 예시 값
    }

    public int getHeight() {
        return 10; // 예시 값
    }

    public Tile getTileAt(int x, int y) {
        // 실제 구현에서는 GameState의 getTileAt 메서드 호출
        return null; // 임시 반환값
    }

    public Unit getUnitAt(int x, int y) {
        // 실제 구현에서는 GameState의 getUnitAt 메서드 호출
        return null; // 임시 반환값
    }

    // Getter와 Setter 메서드
    public List<Card> getPlayerDeck() {
        return playerDeck;
    }

    public void setPlayerDeck(List<Card> playerDeck) {
        this.playerDeck = playerDeck;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(List<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public int getPlayerCost() {
        return playerCost;
    }

    public void setPlayerCost(int playerCost) {
        this.playerCost = playerCost;
    }

    public int getMaxCostPerTurn() {
        return maxCostPerTurn;
    }

    public void setMaxCostPerTurn(int maxCostPerTurn) {
        this.maxCostPerTurn = maxCostPerTurn;
    }

    // todo:
    public void addUnit(Unit unit) {}

    // todo:
    public void removeUnit(Unit unit) {}
}

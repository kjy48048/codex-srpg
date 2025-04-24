package com.game.srpg.service;

import com.game.srpg.config.GameDataProperties;
import com.game.srpg.config.CardTemplateLoader;
import com.game.srpg.model.GameState;
import com.game.srpg.model.Unit;
import org.springframework.stereotype.Component;

/**
 * GameState를 설정 파일 기반으로 생성하는 팩토리
 */
@Component
public class GameStateFactory {
    private final GameDataProperties dataProps;
    private final CardTemplateLoader cardTemplateLoader;

    public GameStateFactory(GameDataProperties dataProps,
                            CardTemplateLoader cardTemplateLoader) {
        this.dataProps = dataProps;
        this.cardTemplateLoader = cardTemplateLoader;
    }

    /**
     * GameState 객체 생성 및 초기화
     */
    public GameState create() {
        GameState state = new GameState(dataProps.getWidth(), dataProps.getHeight());
        // 기존 초기화 삭제
        state.getPlayerUnits().clear();
        state.getEnemyUnits().clear();
        // 모든 타일의 유닛 제거
        state.getTiles().values().forEach(tile -> tile.setUnit(null));

        // 플레이어 유닛 배치
        for (GameDataProperties.UnitDef def : dataProps.getInitialUnits()) {
            Unit unit = new Unit(def.getName(), def.getType(), def.getX(), def.getY());
            unit.setMaxHp(def.getMaxHp());
            unit.setCurrentHp(def.getMaxHp());
            unit.setAttackPower(def.getAttackPower());
            unit.setMoveRange(def.getMoveRange());
            unit.setAttackRange(def.getAttackRange());
            state.getPlayerUnits().add(unit);
            state.getTile(def.getX(), def.getY()).setUnit(unit);
        }
        // 적 유닛 배치
        for (GameDataProperties.UnitDef def : dataProps.getInitialEnemyUnits()) {
            Unit unit = new Unit(def.getName(), def.getType(), def.getX(), def.getY());
            unit.setMaxHp(def.getMaxHp());
            unit.setCurrentHp(def.getMaxHp());
            unit.setAttackPower(def.getAttackPower());
            unit.setMoveRange(def.getMoveRange());
            unit.setAttackRange(def.getAttackRange());
            state.getEnemyUnits().add(unit);
            state.getTile(def.getX(), def.getY()).setUnit(unit);
        }
        // 카드 시스템 초기화 및 템플릿 로딩
        state.initializeCardSystem();
        cardTemplateLoader.getTemplates().forEach(state::addCardToDeck);
        return state;
    }
}
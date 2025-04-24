package com.game.srpg.service;

import com.game.srpg.config.CardTemplateLoader;
import com.game.srpg.config.GameDataProperties;
import com.game.srpg.config.GameDataProperties.UnitDef;
import com.game.srpg.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameStateFactory 단위 테스트
 */
class GameStateFactoryTest {
    private GameDataProperties props;
    private CardTemplateLoader templateLoader;
    private GameStateFactory factory;

    @BeforeEach
    void setUp() {
        props = new GameDataProperties();
        props.setWidth(5);
        props.setHeight(4);
        // 초기 플레이어 유닛 설정
        UnitDef p1 = new UnitDef();
        p1.setName("P1"); p1.setType(com.game.srpg.model.UnitType.WARRIOR);
        p1.setX(0); p1.setY(0);
        UnitDef p2 = new UnitDef();
        p2.setName("P2"); p2.setType(com.game.srpg.model.UnitType.ARCHER);
        p2.setX(1); p2.setY(0);
        props.setInitialUnits(Arrays.asList(p1, p2));
        // 초기 적 유닛 설정
        UnitDef e1 = new UnitDef();
        e1.setName("E1"); e1.setType(com.game.srpg.model.UnitType.MAGE);
        e1.setX(3); e1.setY(2);
        props.setInitialEnemyUnits(Arrays.asList(e1));
        // 템플릿 로더 (실제 YAML 로딩 또는 빈)
        templateLoader = new CardTemplateLoader();
        factory = new GameStateFactory(props, templateLoader);
    }

    @Test
    void testCreateGameState() {
        GameState state = factory.create();
        // 맵 크기
        assertEquals(5, state.getWidth());
        assertEquals(4, state.getHeight());
        // 플레이어 유닛 개수
        assertEquals(2, state.getPlayerUnits().size());
        // 적 유닛 개수
        assertEquals(1, state.getEnemyUnits().size());
        // 유닛 위치에 실제 Unit 객체 존재
        assertNotNull(state.getTile(0, 0).getUnit(), "Player unit at (0,0) should be placed");
        assertNotNull(state.getTile(1, 0).getUnit(), "Player unit at (1,0) should be placed");
        assertNotNull(state.getTile(3, 2).getUnit(), "Enemy unit at (3,2) should be placed");
        // 카드 덱이 템플릿 로더 수와 일치
        List<com.game.srpg.model.Card> templates = templateLoader.getTemplates();
        assertEquals(templates.size(), state.getPlayerDeck().size(), "Deck size should match loaded templates");
    }
}
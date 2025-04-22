package com.game.srpg.config;

import com.game.srpg.model.UnitType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * YAML 기반 게임 초기 데이터 설정
 */
@Component
@Getter
@Setter
public class GameDataProperties {
    /** 맵 너비 */
    private int width;
    /** 맵 높이 */
    private int height;
    /** 초기 플레이어 유닛 리스트 */
    private List<UnitDef> initialUnits;
    /** 초기 적 유닛 리스트 */
    private List<UnitDef> initialEnemyUnits;

    @Getter
    @Setter
    public static class UnitDef {
        private String name;
        private UnitType type;
        private int x;
        private int y;
        private int maxHp;
        private int attackPower;
        private int moveRange;
        private int attackRange;
    }
}

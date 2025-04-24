package com.game.srpg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.srpg.model.Card;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 카드 템플릿(YAML) 파일을 로드하여 Card 객체로 변환하는 로더
 * <p>
 * game-data/templates/*.yml 경로의 모든 YAML 파일을 읽어 들입니다.
 */
@Component
public class CardTemplateLoader {
    private final List<Card> templates;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CardTemplateLoader.class);

    public CardTemplateLoader() {
        this.templates = loadTemplates();
    }

    private List<Card> loadTemplates() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath:game-data/templates/*.yml");
        } catch (IOException e) {
            log.warn("Could not load card template resources from 'classpath:game-data/templates/*.yml', proceeding with empty templates", e);
            return Collections.emptyList();
        }
        List<Card> list = new ArrayList<>();
        for (Resource res : resources) {
            try {
                Card card = mapper.readValue(res.getInputStream(), Card.class);
                list.add(card);
            } catch (IOException e) {
                log.warn("Failed to parse card template: {}", res.getFilename(), e);
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 로드된 모든 카드 템플릿 조회
     *
     * @return 불변 리스트로 반환
     */
    public List<Card> getTemplates() {
        return templates;
    }

    /**
     * 카드 이름(name)으로 템플릿 검색
     *
     * @param name 카드 이름
     * @return 일치하는 템플릿(없으면 null)
     */
    public Card getByName(String name) {
        return templates.stream()
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElse(null);
    }
}

package cn.iocoder.yudao.module.ds.enums;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DsTeamConfigConstants {

    public static final String CONFIG_GROUP_TEAM_REWARD = "TEAM_REWARD";

    public static final String KEY_RELATION_LEVEL_1 = "RELATION_LEVEL_1";
    public static final String KEY_RELATION_LEVEL_2 = "RELATION_LEVEL_2";
    public static final String KEY_RELATION_LEVEL_3 = "RELATION_LEVEL_3";
    public static final String KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD = "TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD";
    public static final String KEY_TEAM_LEADER_LEVEL3_NEAREST = "TEAM_LEADER_LEVEL3_NEAREST";
    public static final String KEY_TEAM_LEADER_LEVEL3_UPPER = "TEAM_LEADER_LEVEL3_UPPER";
    public static final String KEY_SHAREHOLDER_POOL = "SHAREHOLDER_POOL";

    public static final String VALUE_TYPE_INT = "INT";
    public static final String VALUE_TYPE_STRING = "STRING";

    public static final List<String> TEAM_REWARD_KEYS = List.of(
            KEY_RELATION_LEVEL_1,
            KEY_RELATION_LEVEL_2,
            KEY_RELATION_LEVEL_3,
            KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD,
            KEY_TEAM_LEADER_LEVEL3_NEAREST,
            KEY_TEAM_LEADER_LEVEL3_UPPER,
            KEY_SHAREHOLDER_POOL
    );

    private DsTeamConfigConstants() {
    }

    public static Map<String, String> defaultConfigValues() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(KEY_RELATION_LEVEL_1, "1");
        map.put(KEY_RELATION_LEVEL_2, "2");
        map.put(KEY_RELATION_LEVEL_3, "3");
        map.put(KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD, "10");
        map.put(KEY_TEAM_LEADER_LEVEL3_NEAREST, "TEAM_LEADER_LEVEL3_NEAREST");
        map.put(KEY_TEAM_LEADER_LEVEL3_UPPER, "TEAM_LEADER_LEVEL3_UPPER");
        map.put(KEY_SHAREHOLDER_POOL, "SHAREHOLDER_POOL");
        return map;
    }
}

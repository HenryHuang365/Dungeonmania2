package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public final class DungeonResponse {
    private final String dungeonId;
    private final String dungeonName;
    private final List<EntityResponse> entities;
    private final List<ItemResponse> inventory;
    private final List<BattleResponse> battles;
    private final List<String> buildables;
    private final String goals;
    private final JSONObject goalsJson;
    private final List<AnimationQueue> animations;

    // public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
    //         List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables, String goals) {
    //     this(dungeonId, dungeonName, entities, inventory, battles, buildables, goals, new ArrayList<>());
    // }

    // public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
    //         List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables, String goals,
    //         List<AnimationQueue> animations) {
    //     this.dungeonId = dungeonId;
    //     this.dungeonName = dungeonName;
    //     this.entities = entities;
    //     this.inventory = inventory;
    //     this.battles = battles;
    //     this.buildables = buildables;
    //     this.goals = goals;
    //     this.animations = animations;
    // }
    public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
            List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables,
            String goals, JSONObject goalsJson) {
        this(dungeonId, dungeonName, entities, inventory, battles, buildables, goals, goalsJson, new ArrayList<>());
    }

    public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
            List<ItemResponse> inventory, List<BattleResponse> battles, List<String> buildables,
            String goals, JSONObject goalsJson,
            List<AnimationQueue> animations) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.entities = entities;
        this.inventory = inventory;
        this.battles = battles;
        this.buildables = buildables;
        this.goals = goals;
        this.goalsJson = goalsJson;
        this.animations = animations;
    }

    public List<AnimationQueue> getAnimations() {
        return animations;
    }

    public final String getDungeonName() {
        return dungeonName;
    }

    public final List<ItemResponse> getInventory() {
        return inventory;
    }

    public final List<BattleResponse> getBattles() {
        return battles;
    }

    public final List<String> getBuildables() {
        return buildables;
    }

    public final String getGoals() {
        return goals;
    }

    public final JSONObject getGoalsJson() {
        return goalsJson;
    }

    public final String getDungeonId() {
        return dungeonId;
    }

    public final List<EntityResponse> getEntities() {
        return entities;
    }
}

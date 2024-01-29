package dungeonmania.goals.GoalsCondition;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class TreasureGoal implements GoalsConditions {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean isGoldacheived(Game game) {
        return game.getInitialTreasureCount()
                - game.getMap().getEntities(Treasure.class).size()
                - game.getMap().getEntities(SunStone.class).size() >= target;
    }

    public String toString(Game game) {
        return ":treasure";
    }

    public JSONObject toJson(Game game) {
        JSONObject exit = new JSONObject();
        exit.put("goal", "treasure");
        return exit;
    }
}

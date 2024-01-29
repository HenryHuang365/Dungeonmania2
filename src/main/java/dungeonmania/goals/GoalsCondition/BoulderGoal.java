package dungeonmania.goals.GoalsCondition;
import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BoulderGoal implements GoalsConditions {
    @Override
    public boolean isGoldacheived(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    public String toString(Game game) {
        return ":boulders";
    }

    public JSONObject toJson(Game game) {
        JSONObject exit = new JSONObject();
        exit.put("goal", "boulders");
        return exit;
    }
}

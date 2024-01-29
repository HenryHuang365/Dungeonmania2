package dungeonmania.goals.GoalsCondition;

import org.json.JSONObject;

import dungeonmania.Game;

public interface GoalsConditions {
    public boolean isGoldacheived(Game game);
    public String toString(Game game);
    public JSONObject toJson(Game game);
}

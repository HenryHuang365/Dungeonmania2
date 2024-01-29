package dungeonmania.goals.GoalsCondition;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class ANDGoal implements GoalsConditions {

    private Goal goal1;
    private Goal goal2;

    public ANDGoal(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    @Override
    public boolean isGoldacheived(Game game) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    @Override
    public String toString(Game game) {
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

    @Override
    public JSONObject toJson(Game game) {
        JSONObject and = new JSONObject();
        and.put("goal", "AND");
        List<JSONObject> subgoals = new ArrayList<>();
        subgoals.add(goal1.toJson(game));
        subgoals.add(goal2.toJson(game));
        and.put("subgoals", subgoals);
        return and;
    }
}

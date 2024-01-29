package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.goals.GoalsCondition.ANDGoal;
import dungeonmania.goals.GoalsCondition.BoulderGoal;
import dungeonmania.goals.GoalsCondition.EnemyGoals;
import dungeonmania.goals.GoalsCondition.ExitGoal;
import dungeonmania.goals.GoalsCondition.ORGoal;
import dungeonmania.goals.GoalsCondition.TreasureGoal;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        switch (jsonGoal.getString("goal")) {
        case "AND":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new Goal(
                new ANDGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config)
                )
            );
        case "OR":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new Goal(
                new ORGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config)
                )
            );
        case "exit":
            return new Goal(new ExitGoal());
        case "boulders":
            return new Goal(new BoulderGoal());
        case "treasure":
            int treasureGoal = config.optInt("treasure_goal", 1);
            return new Goal(new TreasureGoal(treasureGoal));
        case "enemies":
            int enemiesGoal = config.optInt("enemy_goal", 1);
            return new Goal(new EnemyGoals(enemiesGoal));

        default:
            return null;
        }
    }
}

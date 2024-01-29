package dungeonmania.goals;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.goals.GoalsCondition.GoalsConditions;

public class Goal {
    //private String type;
    private GoalsConditions type;
    //private int target;
    //private Goal goal1;
    //private Goal goal2;
    // private GoalsConditions goal1;
    // private GoalsConditions goal2;

    public Goal(GoalsConditions type) {
        this.type = type;
    }

    // public Goal(GoalsConditions type, int target) {
    //     this.type = type;
    //     this.target = target;
    // }

    // public Goal(GoalsConditions type, GoalsConditions goal1, GoalsConditions goal2) {
    //     this.type = type;
    //     this.goal1 = goal1;
    //     this.goal2 = goal2;
    // }

    public GoalsConditions getType() {
        return type;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        // switch (type) {
        //     case "exit":
        //         Player character = game.getPlayer();
        //         Position pos = character.getPosition();
        //         List<Exit> es = game.getMap().getEntities(Exit.class);
        //         if (es == null || es.size() == 0) return false;
        //         return es
        //             .stream()
        //             .map(Entity::getPosition)
        //             .anyMatch(pos::equals);
        //     case "boulders":
        //         return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
        //     case "treasure":
        //         return game.getInitialTreasureCount() - game.getMap().getEntities(Treasure.class).size() >= target;
        //     case "AND":
        //         return goal1.achieved(game) && goal2.achieved(game);
        //     case "OR":
        //         return goal1.achieved(game) || goal2.achieved(game);
        //     default:
        //         break;
        // }
        return type.isGoldacheived(game);
    }

    public String toString(Game game) {
        if (type.isGoldacheived(game)) return "";
        if (type == null) return "";
        return type.toString(game);
        // switch (type) {
        //     case "exit":
        //         return ":exit";
        //     case "boulders":
        //         return ":boulders";
        //     case "treasure":
        //         return ":treasure";
        //     case "AND":
        //         return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
        //     case "OR":
        //         if (achieved(game)) return "";
        //         else return "(" + goal1.toString(game) + " OR " + goal2.toString(game) + ")";
        //     default:
        //         break;
        // }
        //return "";
    }

    public JSONObject toJson(Game game) {
        return type.toJson(game);
    }
}

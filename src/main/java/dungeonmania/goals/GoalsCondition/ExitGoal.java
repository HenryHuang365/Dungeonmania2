package dungeonmania.goals.GoalsCondition;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class ExitGoal implements GoalsConditions {

    @Override
    public boolean isGoldacheived(Game game) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> es = game.getMap().getEntities(Exit.class);
        if (es == null || es.size() == 0) return false;
        return es
            .stream()
            .map(Entity::getPosition)
            .anyMatch(pos::equals);
    }

    public String toString(Game game) {
        return ":exit";
    }

    public JSONObject toJson(Game game) {
        JSONObject exit = new JSONObject();
        exit.put("goal", "exit");
        return exit;
    }
}

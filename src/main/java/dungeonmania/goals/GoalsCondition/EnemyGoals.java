package dungeonmania.goals.GoalsCondition;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoals implements GoalsConditions {
    private int target;

    public EnemyGoals(int target) {
        this.target = target;
    }

    @Override
    public boolean isGoldacheived(Game game) {
        // return false;
        // System.out.println("1. Initial Spawner Count:" + game.getInitialSpawnersCount());
        // System.out.println("2. Initial Enemy Count:" + game.getInitialEnemyCount());
        // System.out.println("3. Current Enemy Count:" + game.getMap().getEntities(Enemy.class).size());
        // System.out.println("4. Current Spawner Count:" + game.getMap().getEntities(ZombieToastSpawner.class).size());
        // System.out.println("5. Distroyed Enemy Count:" + game.getMap().getEnemiesList().size());
        // System.out.println("5. Distroyed Enemy Count:" + game.getNumEnemiesKilledByPlayer());
        // return (game.getInitialEnemyCount()
        // - game.getMap().getEntities(Enemy.class).size()) >= target
        // && game.getMap().getEntities(ZombieToastSpawner.class).size() == 0;
        // return game.getMap().getEnemiesList().size() >= target
        return game.getNumEnemiesKilledByPlayer() >= target
        && game.getMap().getEntities(ZombieToastSpawner.class).size() == 0;
    }

    public String toString(Game game) {
        return ":enemies";
    }

    public int getTarget() {
        return target;
    }

    public JSONObject toJson(Game game) {
        JSONObject exit = new JSONObject();
        exit.put("goal", "enemies");
        return exit;
    }
}

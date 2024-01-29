package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.EnemyEntity;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.SwampTile;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends EnemyEntity implements Battleable {
    private BattleStatistics battleStatistics;
    private boolean ifStucked = false;
    private SwampTile stuckedTile;
    private boolean allied = false;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER,
                this,
                true);
    }

    public boolean ifStucked() {
        return ifStucked;
    }

    public void setIfStucked(boolean ifStucked) {
        this.ifStucked = ifStucked;
    }

    public boolean isAllied() {
        return allied;
    }

    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    public void setStuckedTile(SwampTile stuckedTile) {
        this.stuckedTile = stuckedTile;
    }

    public SwampTile getStuckedTile() {
        return stuckedTile;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            // law of demeter
            // map.getGame().battle(player, this);
             map.battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public double getHealth(Player player) {
        return getBattleStatistics().getHealth();
    }

    public void setHealth(double health) {
        getBattleStatistics().setHealth(health);
    }

    // @Override
    // public void onMovedAway(GameMap map, Entity entity) {
    //     return;
    // }

    public abstract void move(Game game);

    public List<Position> getCardinallyAdjacentPositions() {
        return getPosition().getCardinallyAdjacentPositions();
    }

    public boolean isCardinallyAdjacentToPlayer(Game game) {
        List<Position> pos = getCardinallyAdjacentPositions();
        GameMap map = game.getMap();
        // System.out.println(map.getPlayerPosition());
        // System.out.println(pos);
        return pos.stream().anyMatch(p -> p.equals(map.getPlayerPosition()));
    }
}

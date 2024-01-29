package dungeonmania.entities.enemies;
import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.enemies.enemymovement.AlliedMovement;
import dungeonmania.entities.enemies.enemymovement.FollowPlayerMove;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean alliedAndFoundPlayer = false;
    private Movement moveStrategy;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied()) return;
        super.onOverlap(map, entity);
    }
    public void setMoveStrategy(Movement moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        setAllied(true);
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;
        bribe(player);
    }

    @Override
    public void move(Game game) {
        /*
        Position nextPos;
        GameMap map = game.getMap();
        if (allied) {
            // Move random
            Random randGen = new Random();
            List<Position> pos = getPosition().getCardinallyAdjacentPositions();
            pos = pos
                .stream()
                .filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
            if (pos.size() == 0) {
                nextPos = getPosition();
                map.moveTo(this, nextPos);
            } else {
                nextPos = pos.get(randGen.nextInt(pos.size()));
                map.moveTo(this, nextPos);
            }
        } else {
            // Follow hostile
            nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
        }
        map.moveTo(this, nextPos);
        */

        //once the player is adjecent to the allied mercenary, mercenary stick the player's movement
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;

        if (isAllied()) {
            if (!alliedAndFoundPlayer) {
                //friendly mercenary hasn't been Cardinally Adjacent To Player
                setMoveStrategy(new FollowPlayerMove());
                if (!ifStucked()) {
                    moveStrategy.moveToByMovement(this, game);
                } else {
                    getStuckedTile().tryToMoveAway(this);
                }
            } else {
                //stick the player's movement
                setMoveStrategy(new AlliedMovement());
                moveStrategy.moveToByMovement(this, game);
            }
        } else {
            setMoveStrategy(new FollowPlayerMove());
            if (!ifStucked()) {
                moveStrategy.moveToByMovement(this, game);
            } else {
                getStuckedTile().tryToMoveAway(this);
            }
        }
        //in case the player does not move
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;
    }

    @Override
    public boolean isInteractable(Player player) {
        return !isAllied() && canBeBribed(player);
    }
}

package dungeonmania.entities.enemies.boss;
import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.enemymovement.AlliedMovement;
import dungeonmania.entities.enemies.enemymovement.FollowPlayerMove;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Assassin extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 8.0;
    public static final double DEFAULT_HEALTH = 25.0;
    public static final double DEFAULT_BRIBE_FAILED_RATE = 0.5;

    private int bribeAmount = Assassin.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Assassin.DEFAULT_BRIBE_RADIUS;
    private double bribeRate = Assassin.DEFAULT_BRIBE_FAILED_RATE;
    private boolean alliedAndFoundPlayer = false;
    private Movement moveStrategy;

    public Assassin(
        Position position,
        double health,
        double attack,
        int bribeAmount,
        int bribeRadius,
        double bribeRate
    ) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.bribeRate = bribeRate;
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
     * check whether the current assassin can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the assassin
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        //ceartain chance that the bribe will fail
        Random radomGenerator = new Random(System.currentTimeMillis());
        long bound = radomGenerator.nextInt(99) + 1; //randomly generate a number between 1 - 99
        long possibility = Math.round(bribeRate * 100);
        // System.out.println("possibility = " + possibility + " radint = " + bound);
        if (bound > possibility) {
            setAllied(true);
        }

        bribe(player);
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;

    }

    @Override
    public void move(Game game) {
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;

        if (isAllied()) {
            if (!alliedAndFoundPlayer) {
                //friendly assassin hasn't been Cardinally Adjacent To Player
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

    // public static void main(String[] args) {
    //     double a = 1.6;
    //     System.out.println(Math.round(a));
    // }
}

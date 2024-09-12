package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.collectables.Bomb;
// import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.boss.Assassin;
//import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.playerState.BaseState;
// import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends OverlapReactEntity implements Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;
    //private Position previousPosition;
    private PlayerState state;
    // private double health;
    private double attack;
    private double defence;

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefence() {
        return defence;
    }

    public void setDefence(double defence) {
        this.defence = defence;
    }

    public Player(Position position, double health, double attack, double defence) {
        super(position);
        battleStatistics = new BattleStatistics(
                health,
                attack,
                defence,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER,
                this,
                true);
        inventory = new Inventory();
        state = new BaseState(this);
        // this.health = health;
        this.attack = attack;
        // state = new PlayerState(this, false, false);
    }

    public PlayerState getState() {
        return state;
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public BattleItem getWeapon() {
        return inventory.getWeapon();
    }

    public List<String> getBuildables(Game g) {
        return inventory.getBuildables(g);
    }

    public boolean build(String entity, EntityFactory factory, Game g) {
        // InventoryItem item = inventory.checkBuildCriteria(this, true, entity.equals("shield"), factory);
        InventoryItem item = inventory.checkBuildCriteria(this, true, entity, factory, g);
        if (item == null) return false;
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        //battle
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied()) return;
            }
            if (entity instanceof Assassin) {
                if (((Assassin) entity).isAllied()) return;
            }
            map.getGame().battle(this, (Enemy) entity);
        }
        //pickup collectable
        if (entity instanceof CollectableEntity) {
            if (!((CollectableEntity) entity).isOkayToCollect()) return;
            if (!pickUp(entity)) return;
            map.destroyEntity(entity);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        return inventory.add((InventoryItem) item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null) inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            // state.transitionBase();
            // PlayerState baseState = new BaseState(state.getPlayer());
            // changeState(baseState);
            // changeState(new PlayerState(this, false, false));
            // state.transitionState();
            // ((BaseState) state).transitionBase();
            state.transitionState(state, this);
            return;
        }
        inEffective = queue.remove();
        // if (inEffective instanceof InvincibilityPotion) {
        //     // state.transitionInvincible();
        //     // PlayerState invisibleState = new InvisibleState(state.getPlayer());
        //     // changeState(invisibleState);
        //     changeState(new PlayerState(this, true, false));
        // } else {
        //     // PlayerState invincibleState = new InvisibleState(state.getPlayer());
        //     // changeState(invincibleState);
        //     // state.transitionInvisible();
        //     changeState(new PlayerState(this, false, true));
        // }
        inEffective.stateTransition(state);
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        // if (state.isInvincible()) {
        //     return BattleStatistics.applyBuff(origin, new BattleStatistics(
        //         0,
        //         0,
        //         0,
        //         1,
        //         1,
        //         true,
        //         true));
        // } else if (state.isInvisible()) {
        //     return BattleStatistics.applyBuff(origin, new BattleStatistics(
        //         0,
        //         0,
        //         0,
        //         1,
        //         1,
        //         false,
        //         false));
        // }
        // battleStatistics = new BattleStatistics(
        //         health,
        //         attack,
        //         0,
        //         BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
        //         BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER,
        //         this,
        //         true);
        return state.applyBuff(origin);
        // return origin;
    }

    public double getHealth(Player player) {
        return getBattleStatistics().getHealth();
    }

    public List<BattleItem> getBattlEntities() {
        return getInventory().getEntities(BattleItem.class);
    }

    public void setHealth(double health) {
        getBattleStatistics().setHealth(health);
    }

    /*
    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }
    */

    public void getWeaponUse(Game game) {
        getInventory().weaponUse(game);
    }

    // public boolean isInvincible() {
    //     return getState().isInvincible();
    // }
}

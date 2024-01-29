package dungeonmania;

// import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// import java.io.Writer;
import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import org.json.JSONObject;
import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class Game {
    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private int initialTreasureCount;
    private int initialEnemyCount;
    private int initialSpawnersCount;
    private EntityFactory entityFactory;
    private boolean isInTick = false;
    private int numEnemiesKilledByPlayer = 0;
    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;

    private int tickCount = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        player = map.getPlayer();
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");
        initialTreasureCount = map.getEntities(Treasure.class).size() + map.getEntities(SunStone.class).size();
        initialEnemyCount = map.getEntities(Enemy.class).size();
        initialSpawnersCount = map.getEntities(ZombieToastSpawner.class).size();
    }

    public Game tick(Direction movementDirection) {
        registerOnce(
            () -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    /**
     * Battle between player and enemy
     * @param player
     * @param enemy
     */
    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(enemy);
            numEnemiesKilledByPlayer++;
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables(this);
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory, this), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(
            () -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return map.countEntities(type);
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public int tick() {
        isInTick = true;
        sub.forEach(s -> s.run());
        isInTick = false;
        sub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub.removeIf(s -> !s.isValid());
        tickCount++;
        // update the weapons/potions duration
        return tickCount;
    }

    public void saveGame(String name, String conName) {
        // create new file store the game state
        DungeonResponse savedGame = ResponseBuilder.getDungeonResponse(this);
        JSONObject goals = savedGame.getGoalsJson();
        List<EntityResponse> entities = savedGame.getEntities();

        List<JSONObject> entitiesJson = new ArrayList<JSONObject>();
        for (EntityResponse entity : entities) {
            JSONObject entityJsonObject = new JSONObject();
            if (entity.getType().equals("key") || entity.getType().equals("door")) {
                entityJsonObject.put("type", entity.getType());
                entityJsonObject.put("x", entity.getPosition().getX());
                entityJsonObject.put("y", entity.getPosition().getY());
                entityJsonObject.put("key", entity.getPosition().getLayer());
            } else {
                entityJsonObject.put("type", entity.getType());
                entityJsonObject.put("x", entity.getPosition().getX());
                entityJsonObject.put("y", entity.getPosition().getY());
            }
            entitiesJson.add(entityJsonObject);
        }

        JSONObject gameState = new JSONObject();
        gameState.put("entities", entitiesJson);
        gameState.put("goal-condition", goals);

        try {
            FileWriter file = new FileWriter("src/main/resources/saveddungeons/" + name + ".json", false);
            // FileWriter file = new FileWriter("src/main/resources/saveddungeons/" + name + ".json");
            file.write(gameState.toString());
            file.close();

            // FileWriter configFile = new FileWriter("src/main/resources/savedconfigs/" + name + ".json");
            FileWriter configFile = new FileWriter("src/main/resources/savedconfigs/" + name + ".json", false);
            configFile.write(loadConfig(conName).toString());
            configFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JSONObject loadConfig(String configName) {
        String configFile = String.format("/configs/%s.json", configName);
        JSONObject config = new JSONObject();
        try {
            config = new JSONObject(FileLoader.loadResourceFile(configFile));
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
        return config;
    }

    public int getTick() {
        return this.tickCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void initRegisterSpawners(Game game) {
        getEntityFactory().spawnSpider(game);
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public int getInitialTreasureCount() {
        return initialTreasureCount;
    }

    public int getInitialEnemyCount() {
        return initialEnemyCount;
    }

    public int getInitialSpawnersCount() {
        return initialSpawnersCount;
    }

    public void removeBow(Bow bow, Player player) {
        player.remove(bow);
    }

    public void removeShield(Shield shield, Player player) {
        player.remove(shield);
    }

    public void removeSword(Sword sword, Player player) {
        player.remove(sword);
    }

    public void moveTo(Enemy e, Position nextPos) {
        getMap().moveTo(e, nextPos);
    }

    public List<Entity> getEntities(Position nextPos) {
        return getMap().getEntities(nextPos);
    }

    public void spawnZombie(Game game, ZombieToastSpawner z) {
        getEntityFactory().spawnZombie(game, z);
    }

    public void destroySpawners(ZombieToastSpawner spawner) {
        getMap().destroyEntity(spawner);
    }

    public int getNumEnemiesKilledByPlayer() {
        return numEnemiesKilledByPlayer;
    }

    public int countZombieToast() {
        return getMap().getEntities(ZombieToast.class).size();
    }

}

package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

//movement factor is set to 2 in 16-1 to 16-5
public class SwampTest {
    @Test
    @Tag("16-1")
    @DisplayName("Test if the player can be stucked on swamp tile")
    public void playerAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_player", "c_swampTest_player");

        //move down and the player is on the swamptile
        res = dmc.tick(Direction.DOWN);
        assertEquals(getSwampPos(res), getPlayerPos(res));

        //move down again, player is not stucked and change the position
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(getSwampPos(res), getPlayerPos(res));

    }

    @Test
    @Tag("16-2")
    @DisplayName("Test if assassin can be stucked on swamptile")
    public void assassinAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_assassinswamp", "c_swampTest_player");
        //swamp movement factor:2
        //move up, player will hit the wall and not move, assassin is stucked in the swamp

        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "assassin"), getSwampPos(res));

        //next tick, assassin is stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "assassin"), getSwampPos(res));

        //next tick, assassin is still stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "assassin"), getSwampPos(res));

        //next tick, assassin will move out from swamp and underneath the player
        res = dmc.tick(Direction.UP);
        assertNotEquals(getEntityPos(res, "assassin"), getSwampPos(res));
        assertEquals(new Position(4, 3), getEntityPos(res, "assassin"));

        //next tick, battle will happened and player lose
        res = dmc.tick(Direction.UP);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());

    }

    @Test
    @Tag("16-3")
    @DisplayName("Test if mercenary can be stucked on swamptile")
    public void mercenaryAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_mercenaryswamp", "c_swampTest_player");
        //swamp movement factor:2
        //move up, player will hit the wall and not move, mercenary is stucked in the swamp

        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "mercenary"), getSwampPos(res));

        //next tick, mercenary is stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "mercenary"), getSwampPos(res));

        //next tick, mercenary is still stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "mercenary"), getSwampPos(res));

        //next tick, mercenary will move out from swamp and underneath the player
        res = dmc.tick(Direction.UP);
        assertNotEquals(getEntityPos(res, "mercenary"), getSwampPos(res));
        assertEquals(new Position(4, 3), getEntityPos(res, "mercenary"));

        //next tick, battle will happened and player wins
        res = dmc.tick(Direction.UP);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());

    }

    @Test
    @Tag("16-4")
    @DisplayName("Test if spider can be stucked on swamptile")
    public void spiderAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_spiderswamp", "c_swampTest_player");
        //swamp movement factor:2
        //move up, player will hit the wall and not move, spider is stucked in the swamp

        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "spider"), getSwampPos(res));

        //next tick, spider is stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "spider"), getSwampPos(res));

        //next tick, spider is still stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "spider"), getSwampPos(res));

        //next tick, spider will move out from swamp
        res = dmc.tick(Direction.UP);
        assertNotEquals(getEntityPos(res, "spider"), getSwampPos(res));
        assertEquals(new Position(5, 4), getEntityPos(res, "spider"));

    }

    @Test
    @Tag("16-5")
    @DisplayName("Test if zombie toast can be stucked on swamptile")
    public void zombieAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_zombie", "c_swampTest_player");
        //swamp movement factor:2
        //move up, player will hit the wall and not move, spider is stucked in the swamp

        while (!getEntityPos(res, "zombie_toast").equals(getSwampPos(res))) {
            //keep moving the tick until the zombie moves on to the swamp tile
            res = dmc.tick(Direction.UP);
        }
        //now zombie just move on the swamp tile
        assertEquals(getEntityPos(res, "zombie_toast"), getSwampPos(res));

        //next tick, zombietoast is stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "zombie_toast"), getSwampPos(res));

        //next tick, zombietoast is still stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "zombie_toast"), getSwampPos(res));

        //next tick, zombietoast will move out from swamp
        res = dmc.tick(Direction.UP);
        assertNotEquals(getEntityPos(res, "zombie_toast"), getSwampPos(res));

    }

    @Test
    @Tag("16-5")
    @DisplayName("Test if hydra can be stucked on swamptile")
    public void hydraAndswamp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_hydra", "c_swampTest_player");
        //swamp movement factor:2
        //move up, player will hit the wall and not move, spider is stucked in the swamp

        while (!getEntityPos(res, "hydra").equals(getSwampPos(res))) {
            //keep moving the tick until the hydra moves on to the swamp tile
            res = dmc.tick(Direction.UP);
        }
        //now hydra just move on the swamp tile
        assertEquals(getEntityPos(res, "hydra"), getSwampPos(res));

        //next tick, hydra is stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "hydra"), getSwampPos(res));

        //next tick, hydra is still stucked did not change the position
        res = dmc.tick(Direction.UP);
        assertEquals(getEntityPos(res, "hydra"), getSwampPos(res));

        //next tick, hydra will move out from swamp
        res = dmc.tick(Direction.UP);
        assertNotEquals(getEntityPos(res, "hydra"), getSwampPos(res));

    }

    @Test
    @Tag("16-6")
    @DisplayName("Test if mercenary can be stucked on swamptile when it is allided")
    public void alliedMercenaryAndswamp() {
        //movement factor is 5
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_mercenaryswampf5", "c_swampTest_player");
        //swamp movement factor:2
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        //bribe the merc, merc become allied, but it is not cardinally adjecent
        //to player, so it still stuck in the swamp.
        assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(getEntityPos(res, "mercenary"), getSwampPos(res));

        //player move near the mercenary to be cardinally adjecent to it
        //mercenary will no longer be stuck
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(getEntityPos(res, "mercenary"), getSwampPos(res));

    }

    @Test
    @Tag("16-7")
    @DisplayName("Test if assassin can be stucked on swamptile when it is allided")
    public void alliedAssassinAndswamp() {
        //movement factor is 5
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_assassinswamp", "c_swampTest_player");
        //swamp movement factor:2
        String mercId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        //bribe the merc, merc become allied, but it is not cardinally adjecent
        //to player, so it still stuck in the swamp.
        assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(getEntityPos(res, "assassin"), getSwampPos(res));

        //player move near the assassin to be cardinally adjecent to it
        //assassin will no longer be stuck
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(getEntityPos(res, "assassin"), getSwampPos(res));

    }

    private Position getSwampPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "swamp_tile").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getEntityPos(DungeonResponse res, String entity) {
        return TestUtils.getEntities(res, entity).get(0).getPosition();
    }
}

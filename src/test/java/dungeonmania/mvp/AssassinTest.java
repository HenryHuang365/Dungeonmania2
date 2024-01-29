package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class AssassinTest {

    @Test
    @Tag("14-1")
    @DisplayName("Test assassin in line with Player moves towards them, player will also kill the assassin")
    public void simpleMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest", "c_assassinTest");

        assertEquals(1, TestUtils.getEntities(res, "assassin").size());
        assertEquals(new Position(11, 4), getAssassinPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(10, 4), getAssassinPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(9, 4), getAssassinPos(res));
        res = dmc.tick(Direction.RIGHT);
        //player kill the assassin
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "assassin").size());
    }

    @Test
    @Tag("14-2")
    @DisplayName("Test assassin stops if they cannot move any closer to the player")
    public void stopMovement() {
        //                  Wall     Wall    Wall
        // P1       P2      Wall      A1     Wall
        //                  Wall     Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin_stopMovement", "c_assassinTest");

        Position startingPos = getAssassinPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getAssassinPos(res));
    }

    @Test
    @Tag("14-3")
    @DisplayName("Test assassin moves around a wall to get to the player")
    public void evadeWall() {
        //                  Wall      A2
        // P1       P2      Wall      A1
        //                  Wall      A2
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_evadeWall", "c_assassinTest");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(new Position(4, 1).equals(getAssassinPos(res))
            || new Position(4, 3).equals(getAssassinPos(res)));
    }

    @Test
    @Tag("14-4")
    @DisplayName("Testing a assassin can not be bribed when failling rate is 100%")
    public void bribeFailed100() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest", "c_assassinTest_setFailRateTo1");

        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up first treasure
        assertEquals(new Position(11, 4), getAssassinPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(10, 4), getAssassinPos(res));

        // attempt bribe, bribe should fail, coin still consumed
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(9, 4), getAssassinPos(res));

        //battle will still occur
        assertEquals(1, TestUtils.getEntities(res, "assassin").size());
        res = dmc.tick(Direction.RIGHT);
        //player kill the assassin
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "assassin").size());

    }

    @Test
    @Tag("14-5")
    @DisplayName("Testing an allied mercenary does not battle the player when set the fail rate to 0")
    public void noBattlebribeSuccess() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest", "c_assassinTest_setFailRateTo0");

        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up first treasure
        assertEquals(new Position(11, 4), getAssassinPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(10, 4), getAssassinPos(res));

        // attempt bribe, bribe should not fail, coin is consumed
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(9, 4), getAssassinPos(res));

        //battle will not occur
        assertEquals(1, TestUtils.getEntities(res, "assassin").size());
        res = dmc.tick(Direction.RIGHT);
        //player will not kill the assassin
        assertEquals(0, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "assassin").size());


    }
    @Test
    @Tag("14-6")
    @DisplayName("Testing the movement of an allied assassin")
    public void alliedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_allyBattle", "c_assassinTest_setFailRateTo0");

        String mercId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into assassin, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        // the bribed assassin
        // should follow the player by jumping to the player's previous location
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (2, 1) Merc should jump to (2, 1)
        assertEquals(new Position(2, 1), getAssassinPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 1) Merc should jump to (3, 1)
        assertEquals(new Position(3, 1), getAssassinPos(res));

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 2) Merc should jump to (3, 2)
        assertEquals(new Position(3, 2), getAssassinPos(res));


    }


    private Position getAssassinPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }
}

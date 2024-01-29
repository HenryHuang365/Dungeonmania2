package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HydraTest {

    @Test
    @Tag("15-1")
    @DisplayName("Testing hydra movement")
    public void movement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_movement", "c_hydraTest_movement");

        assertEquals(1, getHydras(res).size());

        // Teams may assume that random movement includes choosing to stay still, so we should just
        // check that they do move at least once in a few turns
        boolean hydraMoved = false;
        Position prevPosition = getHydras(res).get(0).getPosition();
        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.UP);
            if (!prevPosition.equals(getHydras(res).get(0).getPosition())) {
                hydraMoved = true;
                break;
            }
        }
        assertTrue(hydraMoved);
    }

    @Test
    @Tag("15-2")
    @DisplayName("Testing hydra cannot move through closed doors and walls")
    public void doorsAndWalls() {
        //  W   W   W   W
        //  P   W   Z   W
        //      W   D   W
        //          K
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_doorsAndWalls", "c_hydraTest_doorsAndWalls");
        assertEquals(1, getHydras(res).size());
        Position position = getHydras(res).get(0).getPosition();
        res = dmc.tick(Direction.UP);
        assertEquals(position, getHydras(res).get(0).getPosition());
    }

    @Test
    @Tag("15-3")
    @DisplayName("Testing destroying a hydra with a health increase rate 1")
    public void hydraDestruction1() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_healthincreaserate1", "c_hydraTest_healthincreaserate1");
        assertEquals(1, TestUtils.getEntities(res, "hydra").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // pick up sword
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "hydra").size());
    }

    @Test
    @Tag("15-4")
    @DisplayName("Testing destroying a hydra with a health increase rate 0")
    public void hydraDestruction0() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_healthincreaserate0", "c_hydraTest_healthincreaserate0");
        assertEquals(1, TestUtils.getEntities(res, "hydra").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // pick up sword
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "hydra").size());
    }

    private List<EntityResponse> getHydras(DungeonResponse res) {
        return TestUtils.getEntities(res, "hydra");
    }
}

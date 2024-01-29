package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.playerState.InvisibleState;
// import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvisibilityPotion(Position position, int duration) {
        super(position, duration);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                1,
                1,
                // false,
                // new InvisibleState(new Player(getPosition(), DOOR_LAYER, CHARACTER_LAYER)),
                origin.getPlayer(),
                false));
    }

    public void stateTransition(PlayerState state) {
        // state.transitionInvisible();
        // state.transitionState();
        Player player = state.getPlayer();
        state.transitionState(new InvisibleState(player), player);
    }

}

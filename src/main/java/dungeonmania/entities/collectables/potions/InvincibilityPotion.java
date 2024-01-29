package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.playerState.InvincibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvincibilityPotion(Position position, int duration) {
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
                // true,
                // new InvincibleState(new Player(getPosition(), DOOR_LAYER, CHARACTER_LAYER)),
                origin.getPlayer(),
                true));
    }

    public void stateTransition(PlayerState state) {
        // state.transitionInvincible();
        // state.transitionState();
        Player player = state.getPlayer();
        state.transitionState(new InvincibleState(player), player);
    }
}

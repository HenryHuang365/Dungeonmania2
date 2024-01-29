package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public abstract class PlayerState {
    private Player player;
    private boolean isInvincible = false;
    private boolean isInvisible = false;

    // public PlayerState(Player player, boolean isInvincible, boolean isInvisible) {
    public PlayerState(Player player) {
        this.player = player;
        // this.isInvincible = isInvincible;
        // this.isInvisible = isInvisible;
    }

    public boolean isInvincible() {
        return isInvincible;
    };

    public boolean isInvisible() {
        return isInvisible;
    };


    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);
    public abstract void transitionState(PlayerState state, Player player);
}

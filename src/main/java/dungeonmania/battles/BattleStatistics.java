package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.boss.Hydra;
import dungeonmania.entities.playerState.PlayerState;

public class BattleStatistics {
    public static final double DEFAULT_DAMAGE_MAGNIFIER = 1.0;
    public static final double DEFAULT_PLAYER_DAMAGE_REDUCER = 10.0;
    public static final double DEFAULT_ENEMY_DAMAGE_REDUCER = 5.0;

    private double health;
    private double attack;
    private double defence;
    private double magnifier;
    private double reducer;
    private boolean invincible = false;
    private boolean enabled = true;
    private Player player;
    private Enemy enemy;

    public BattleStatistics(
            double health,
            double attack,
            double defence,
            double attackMagnifier,
            double damageReducer) {
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.magnifier = attackMagnifier;
        this.reducer = damageReducer;
        // this.invincible = false;
        // this.enabled = true;
    }

    public BattleStatistics(
            double health,
            double attack,
            double defence,
            double attackMagnifier,
            double damageReducer,
            Enemy enemy,
            boolean isEnabled) {
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.magnifier = attackMagnifier;
        this.reducer = damageReducer;
        // this.invincible = false;
        this.enemy = enemy;
        this.enabled = true;
    }

    public BattleStatistics(
            double health,
            double attack,
            double defence,
            double attackMagnifier,
            double damageReducer,
            // boolean isInvincible,
            Player player,
            boolean isEnabled) {
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.magnifier = attackMagnifier;
        this.reducer = damageReducer;
        this.player = player;
        // this.invincible = isInvincible;
        this.enabled = isEnabled;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerState getState() {
        return player.getState();
    }

    public boolean getInvincible() {
        // return player.isInvincible();
        return getState().isInvincible();
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public static List<BattleRound> battle(BattleStatistics self, BattleStatistics target) {
        List<BattleRound> rounds = new ArrayList<>();
        // if (self.invincible ^ target.invincible) {
        //     double damageOnSelf = (self.invincible) ? 0 : self.getHealth();
        //     double damageOnTarget = (target.invincible) ? 0 : target.getHealth();
        //     self.setHealth((self.invincible) ? self.getHealth() : 0);
        //     target.setHealth((target.invincible) ? target.getHealth() : 0);
        //     rounds.add(new BattleRound(-damageOnSelf, -damageOnTarget));
        //     return rounds;
        // }
        if (self.getInvincible() ^ target.invincible) {
        // if (self.getPlayer().getState().equals(target.getPlayer().getState())) {
            double damageOnSelf = (self.getInvincible()) ? 0 : self.getHealth();
            double damageOnTarget = (target.invincible) ? 0 : target.getHealth();
            self.setHealth((self.getInvincible()) ? self.getHealth() : 0);
            target.setHealth((target.invincible) ? target.getHealth() : 0);
            rounds.add(new BattleRound(-damageOnSelf, -damageOnTarget));
            return rounds;
        }

        while (self.getHealth() > 0 && target.getHealth() > 0) {
            double damageOnSelf = target.getMagnifier() * (target.getAttack() - self.getDefence()) / self.getReducer();
            double damageOnTarget = self.getMagnifier() * (self.getAttack() - target.getDefence())
                    / target.getReducer();

            self.setHealth(self.getHealth() - damageOnSelf);
            if (target.getEnemy() instanceof Hydra) {
                if (((Hydra) target.getEnemy()).isIncreaseHealth()) {
                    target.setHealth(target.getHealth() + ((Hydra) target.getEnemy()).getHealthincreaseamount());
                } else {
                    target.setHealth(target.getHealth() - damageOnTarget);
                }
            } else {
                target.setHealth(target.getHealth() - damageOnTarget);
            }
            // target.setHealth(target.getHealth() - damageOnTarget);
            rounds.add(new BattleRound(-damageOnSelf, -damageOnTarget));
        }
        return rounds;
    }

    public static BattleStatistics applyBuff(BattleStatistics origin, BattleStatistics buff) {
        return new BattleStatistics(
                origin.health + buff.health,
                origin.attack + buff.attack,
                origin.defence + buff.defence,
                origin.magnifier,
                origin.reducer,
                buff.getPlayer(),
                buff.isEnabled());
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

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

    public double getMagnifier() {
        return magnifier;
    }

    public void setMagnifier(double magnifier) {
        this.magnifier = magnifier;
    }

    public double getReducer() {
        return reducer;
    }

    public void setReducer(double reducer) {
        this.reducer = reducer;
    }

    public boolean isInvincible() {
        return this.invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}


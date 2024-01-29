package dungeonmania.entities.buildables;


import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.InventoryItem;


public class MidnightArmour extends Buildable {

    public static final double DEFAULT_EXTRA_ATTACK = 3.0;
    public static final double DEFAULT_EXTRA_DEFENCE = 3.0;

    private double extraAttack = DEFAULT_EXTRA_ATTACK;
    private double extraDefence = DEFAULT_EXTRA_DEFENCE;

    public MidnightArmour(double extraAttack, double extraDefence) {
        super(null);
        this.extraAttack = extraAttack;
        this.extraDefence = extraDefence;
    }


    public double getExtraAttack() {
        return extraAttack;
    }



    public double getExtraDefence() {
        return extraDefence;
    }



    @Override
    public void use(Game game) {
        //infinite durability
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            extraAttack,
            extraDefence,
            1,
            1,
            origin.getPlayer(),
            true));
    }

    @Override
    public int getDurability() {
        //infinite
        return 1;
    }

    public static boolean isBuildable(int swordscount, int sunstonecount, Game g) {
        long zombieToastcount = g.countEntities(ZombieToast.class);
        return swordscount >= 1 && sunstonecount >= 1 && zombieToastcount < 1;
    }

    public static void build(List<InventoryItem> items, List<Sword> swords, List<SunStone> sunstones) {
        items.remove(swords.get(0));
        items.remove(sunstones.get(0));
    }
}

package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Bow extends Buildable  {

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            // law of demeter - fixed
            // game.getPlayer().remove(this);
            game.removeBow(this, game.getPlayer());
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            0,
            2,
            1,
            origin.getPlayer(),
            true)
        );
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public static boolean isBuildable(int woodcount, int arrowcount) {
        // //check if we can build
        // int targetwoodcount = 1;
        // int targetarrowcount = 3;
        // int currwoodcount = woodcount;
        // int currarrowcount = arrowcount;
        // int avaliablesunstone = sunstonecount;

        // //seperate sunstone
        // while (avaliablesunstone != 0) {
        //     if (currwoodcount < targetwoodcount) {
        //         currwoodcount += 1;
        //         avaliablesunstone -= 1;
        //         continue;
        //     }
        //     break;
        // }

        // while (avaliablesunstone != 0) {
        //     if (currarrowcount < targetarrowcount) {
        //         currarrowcount += 1;
        //         avaliablesunstone -= 1;
        //         continue;
        //     }
        //     break;
        // }
        // return currwoodcount >= targetwoodcount && currarrowcount >= targetarrowcount;
        return woodcount >= 1 && arrowcount >= 3;
    }

    public static void build(List<InventoryItem> items, List<Arrow> arrows, List<Wood> wood) {
        int maxwoodcount = 1;
        int maxarrowcount = 3;
        int currwoodcount = wood.size();
        int currarrowcount = arrows.size();

        //consume material
        int i = 0;
        while (i < maxwoodcount && i < currwoodcount) {
            items.remove(wood.get(i));
            i += 1;
        }
        i = 0;
        while (i < maxarrowcount && i < currarrowcount) {
            items.remove(arrows.get(i));
            i += 1;
        }
    }
}

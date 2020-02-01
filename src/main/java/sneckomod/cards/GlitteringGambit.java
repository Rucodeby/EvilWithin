package sneckomod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sneckomod.SneckoMod;
import sneckomod.actions.ChangeGoldAction;

public class GlitteringGambit extends AbstractSneckoCard {

    public final static String ID = makeID("GlitteringGambit");

    //stupid intellij stuff SKILL, SELF, RARE

    private static final int MAGIC = 85;
    private static final int UPG_MAGIC = 5;

    public GlitteringGambit() {
        super(ID, "Beta", 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SneckoMod.SNEKPROOF);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = getRandomNum(magicNumber, 120) / 100;
        int y = AbstractDungeon.player.gold * x;
        atb(new ChangeGoldAction(y-x));
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MAGIC);
        }
    }
}
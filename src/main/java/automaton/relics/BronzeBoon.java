package automaton.relics;

import automaton.AutomatonMod;
import automaton.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static champ.ChampMod.makeRelicOutlinePath;
import static champ.ChampMod.makeRelicPath;

public class BronzeBoon extends CustomRelic {

    public static final String ID = AutomatonMod.makeID("BronzeBoon");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Barbell.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Barbell.png"));

    public BronzeBoon() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}

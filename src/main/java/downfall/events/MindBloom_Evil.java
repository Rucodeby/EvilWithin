package downfall.events;


import automaton.AutomatonChar;
import basemod.ReflectionHacks;
import champ.ChampChar;
import collector.CollectorChar;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.BronzeAutomaton;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import downfall.downfallMod;
import downfall.monsters.GremlinMirror;
import downfall.monsters.SneckoMirror;
import downfall.patches.ui.campfire.AddBustKeyButtonPatches;
import downfall.relics.HeartBlessingBlue;
import downfall.relics.HeartBlessingGreen;
import downfall.relics.HeartBlessingRed;
import gremlin.characters.GremlinCharacter;
import guardian.characters.GuardianCharacter;
import slimebound.characters.SlimeboundCharacter;
import sneckomod.TheSnecko;
import theHexaghost.TheHexaghost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.lastCombatMetricKey;
import static com.megacrit.cardcrawl.helpers.MonsterHelper.getGremlin;

public class MindBloom_Evil extends AbstractImageEvent {
    public static final String ID = "downfall:MindBloom";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    public static final String[] DESCRIPTIONSALT;
    public static final String[] OPTIONSALT;
    private CurScreen screen;

    public MindBloom_Evil() {
        super(NAME, DESCRIPTIONSALT[0], "images/events/mindBloom.jpg");
        this.screen = CurScreen.INTRO;

        if (downfallMod.isDownfallCharacter(AbstractDungeon.player)) {
            if (Loader.isModLoaded("DownfallExtension"))
                this.imageEventText.setDialogOption(CardCrawlGame.languagePack.getEventString("DownfallExtension:MindBloom").OPTIONS[0]);
            else
                this.imageEventText.setDialogOption(OPTIONSALT[0]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[0]);   //original Mind Bloom if you are not a villain
        }

        if (AddBustKeyButtonPatches.KeyFields.bustedRuby.get(AbstractDungeon.player) && AddBustKeyButtonPatches.KeyFields.bustedEmerald.get(AbstractDungeon.player) && AddBustKeyButtonPatches.KeyFields.bustedSapphire.get(AbstractDungeon.player)) {
            if (AbstractDungeon.player instanceof GremlinCharacter) {
                this.imageEventText.setDialogOption(OPTIONSALT[5]);
            } else {
                this.imageEventText.setDialogOption(OPTIONSALT[2]);
            }
        } else {
            this.imageEventText.setDialogOption(OPTIONSALT[3], true);
        }

        if (AbstractDungeon.floorNum % 50 <= 40) {
            this.imageEventText.setDialogOption(OPTIONSALT[1], CardLibrary.getCopy("Normality"));
        } else {
            this.imageEventText.setDialogOption(OPTIONS[2], CardLibrary.getCopy("Doubt"));
        }

    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONSALT[1]);
                        this.screen = CurScreen.FIGHT;
                        if (AbstractDungeon.player instanceof SlimeboundCharacter) {
                            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Slime Boss");
                        } else if (AbstractDungeon.player instanceof GuardianCharacter) {
                            if (Loader.isModLoaded("DownfallExtension"))
                                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("DownfallExtension:Crowbot");
                            else
                                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("The Guardian");
                        } else if (AbstractDungeon.player instanceof TheHexaghost) {

                            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Hexaghost");
                        } else if (AbstractDungeon.player instanceof ChampChar) {
                            if (Loader.isModLoaded("DownfallExtension"))
                                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("DownfallExtension:Irondead");
                            else {
                                AbstractMonster m = new Champ();
                                m.maxHealth = Math.round(m.maxHealth * .6F);
                                m.currentHealth = m.maxHealth;
                                m.powers.add(new StrengthPower(m, -3));
                                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(m);
                            }
                        } else if (AbstractDungeon.player instanceof AutomatonChar) {
                            if (Loader.isModLoaded("DownfallExtension"))
                                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("DownfallExtension:Crowbot");
                            else {
                                AbstractMonster m = new BronzeAutomaton();
                                m.maxHealth = Math.round(m.maxHealth * .6F);
                                m.currentHealth = m.maxHealth;
                                ReflectionHacks.setPrivate(m, BronzeAutomaton.class, "firstTurn", false);
                                m.powers.add(new StrengthPower(m, -3));
                                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(m);
                            }
                        } else if (AbstractDungeon.player instanceof TheSnecko) {
                            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new SneckoMirror());
                        } else if (AbstractDungeon.player instanceof GremlinCharacter) {
                            lastCombatMetricKey = "Gremlin Mirror";
                            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{spawnGremlin(GremlinLeader.POSX[0], GremlinLeader.POSY[0]), spawnGremlin(GremlinLeader.POSX[1], GremlinLeader.POSY[1]), new GremlinMirror()});
                        } else if (AbstractDungeon.player instanceof CollectorChar) {
                            AbstractMonster m = new TheCollector();
                            m.maxHealth = Math.round(m.maxHealth * .6F);
                            m.currentHealth = m.maxHealth;
                            m.powers.add(new StrengthPower(m, -3));
                            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(m);
                        } else {
                            ArrayList<String> list = new ArrayList();
                            list.add("Slime Boss");
                            list.add("Hexaghost");
                            list.add("The Guardian");

                            Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
                            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter((String) list.get(0));
                        }

                        AbstractDungeon.getCurrRoom().rewards.clear();
                        if (AbstractDungeon.ascensionLevel >= 13) {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(25);
                        } else {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                        }

                        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.RARE);
                        this.enterCombatFromImage();
                        CardCrawlGame.music.fadeOutBGM();
                        CardCrawlGame.music.fadeOutTempBGM();
                        CardCrawlGame.music.playTempBgmInstantly("MINDBLOOM", true);
                        logMetric(ID, "Fight Yourself");
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONSALT[2]);
                        this.screen = CurScreen.LEAVE;
                        int effectCount = 0;
                        List<String> upgradedCards = new ArrayList();

                        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                            if (c.canUpgrade()) {
                                ++effectCount;
                                if (effectCount <= 20) {
                                    float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                                    float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                                }

                                upgradedCards.add(c.cardID);
                                c.upgrade();
                                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                            }
                        }

                        AbstractDungeon.player.loseRelic(HeartBlessingRed.ID);
                        AbstractDungeon.player.loseRelic(HeartBlessingBlue.ID);
                        AbstractDungeon.player.loseRelic(HeartBlessingGreen.ID);

                        AbstractDungeon.player.decreaseMaxHealth(10);
                        logMetricUpgradeCards(ID, "Upgrade", upgradedCards);

                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        break;
                    case 2:
                        if (AbstractDungeon.floorNum % 50 <= 40) {
                            this.imageEventText.updateBodyText(DESCRIPTIONSALT[1]);
                            this.screen = CurScreen.LEAVE;
                            List<String> cardsAdded = new ArrayList<>();
                            cardsAdded.add("Normality");
                            cardsAdded.add("Normality");
                            int goldGain = 999;
                            AbstractDungeon.effectList.add(new RainingGoldEffect(goldGain));
                            AbstractDungeon.player.gainGold(goldGain);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), (float) Settings.WIDTH * 0.6F, (float) Settings.HEIGHT / 2.0F));
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), (float) Settings.WIDTH * 0.3F, (float) Settings.HEIGHT / 2.0F));
                            this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                            logMetric(ID, "Gold", cardsAdded, null, null, null, null, null, null, 0, 0, 0, 0, goldGain, 0);
                        } else {
                            this.imageEventText.updateBodyText(DESCRIPTIONSALT[1]);
                            this.screen = CurScreen.LEAVE;
                            AbstractCard curse = new Doubt();
                            logMetricObtainCardAndHeal(ID, "Heal", curse, AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth);
                            AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        }
                }

                this.imageEventText.clearRemainingOptions();
                break;
            case LEAVE:
                this.openMap();
                break;
            default:
                this.openMap();
        }

    }

    private static AbstractMonster spawnGremlin(float x, float y) {
        ArrayList<String> gremlinPool = new ArrayList<String>();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");
        gremlinPool.add("GremlinWizard");
        return getGremlin((String) gremlinPool.get(AbstractDungeon.miscRng.random(0, gremlinPool.size() - 1)), x, y);
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString("MindBloom");
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        DESCRIPTIONSALT = CardCrawlGame.languagePack.getEventString(ID).DESCRIPTIONS;
        OPTIONSALT = CardCrawlGame.languagePack.getEventString(ID).OPTIONS;
    }

    private static enum CurScreen {
        INTRO,
        FIGHT,
        LEAVE;

        private CurScreen() {
        }
    }
}

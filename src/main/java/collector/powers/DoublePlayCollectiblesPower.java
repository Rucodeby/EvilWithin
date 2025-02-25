package collector.powers;

import basemod.helpers.CardModifierManager;
import collector.cardmods.CollectedCardMod;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DoublePlayCollectiblesPower extends AbstractCollectorPower {
    public static final String NAME = "DoublePlayCollectibles";
    public static final String POWER_ID = makeID(NAME);
    public static final PowerType TYPE = PowerType.DEBUFF;
    public static final boolean TURN_BASED = false;

    public DoublePlayCollectiblesPower() {
        super(NAME, TYPE, TURN_BASED, AbstractDungeon.player, null, 1);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && CardModifierManager.hasModifier(card, CollectedCardMod.ID)) {
            for (int i = 0; i < amount; i++) {
                this.flash();
                AbstractMonster m = null;
                if (action.target != null) {
                    m = (AbstractMonster) action.target;
                }

                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            }
        }
    }
}
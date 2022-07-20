package pridemod.patches.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.DistilledChaosPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import pridemod.util.TextureLoader;

@SpirePatch(
    clz=AbstractPotion.class,
    method=SpirePatch.CONSTRUCTOR,
    paramtypez={
        String.class,
        String.class,
        AbstractPotion.PotionRarity.class,
        AbstractPotion.PotionSize.class,
        AbstractPotion.PotionEffect.class,
        Color.class,
        Color.class,
        Color.class
    }
)
public class RainbowPotions
{
    private static Texture rainbow_T = TextureLoader.getTexture("pridemod/ui/potions/rainbowLiquid_t.png");
    private static Texture rainbow_M = TextureLoader.getTexture("pridemod/ui/potions/rainbowLiquid_m.png");
    private static Texture rainbow_C = TextureLoader.getTexture("pridemod/ui/potions/rainbowLiquid_CARD.png");

    public static void Postfix(AbstractPotion __instance) {
        if (__instance instanceof DistilledChaosPotion)
            ReflectionHacks.setPrivate(__instance, AbstractPotion.class, "liquidImg", rainbow_T);
        else if (__instance instanceof EntropicBrew)
            ReflectionHacks.setPrivate(__instance, AbstractPotion.class, "liquidImg", rainbow_M);
        else if (__instance instanceof DuplicationPotion)
            ReflectionHacks.setPrivate(__instance, AbstractPotion.class, "liquidImg", rainbow_C);
        else return;
        ReflectionHacks.setPrivateFinal(__instance, AbstractPotion.class, "p_effect", AbstractPotion.PotionEffect.NONE);
    }
}
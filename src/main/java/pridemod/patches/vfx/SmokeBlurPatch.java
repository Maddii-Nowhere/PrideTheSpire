package pridemod.patches.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;


public class SmokeBlurPatch
{
    @SpirePatch(clz=SmokeBlurEffect.class,method= SpirePatch.CONSTRUCTOR)
    public static class RainbowSmoke
    {
        @SpirePostfixPatch
        public static void Postfix(SmokeBlurEffect __instance)
        {
            ReflectionHacks.setPrivate(__instance, SmokeBlurEffect.class, "color", Color.WHITE);
        }
    }
}

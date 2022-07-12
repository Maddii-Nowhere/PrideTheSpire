package pridemod.patches.ui;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.splash.SplashScreen;

@SpirePatch(clz = SplashScreen.class, method= SpirePatch.CONSTRUCTOR)
public class MegaCritLogoPatch
{
    @SpirePostfixPatch
    public static void Postfix(SplashScreen __instance)
    {
        ReflectionHacks.setPrivate(__instance, SplashScreen.class, "img", ImageMaster.loadImage(("pridemod/ui/megaCrit.png")));
    }

}

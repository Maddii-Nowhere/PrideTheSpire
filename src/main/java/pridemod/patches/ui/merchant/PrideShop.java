package pridemod.patches.ui.merchant;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;

import static pridemod.PrideMain.defaultLanguage;
import static pridemod.PrideMain.getLangString;

public class PrideShop
{
    @SpirePatch(clz= ShopScreen.class,method="init")
    public static class RainbowShop
    {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards)
        {
            Texture rug = ImageMaster.loadImage("pridemod/npcs/rugs/" + defaultLanguage.toLowerCase() + ".png");
            if (!defaultLanguage.equals(getLangString())) {
                rug = ImageMaster.loadImage("pridemod/npcs/rugs/" + getLangString().toLowerCase() + ".png");;
        }
            ReflectionHacks.setPrivate(__instance, ShopScreen.class, "rugImg", rug);
        }
    }
}

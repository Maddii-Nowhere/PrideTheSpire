package pridemod.patches.ui.titlebg;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.scenes.TitleBackground;
import com.megacrit.cardcrawl.scenes.TitleCloud;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import pridemod.PrideMain;
import pridemod.util.TextureLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static pridemod.PrideMain.resourcePath;

public class PrideSpire
{
    public static Texture flag = TextureLoader.getTexture(resourcePath("flags/" + PrideMain.getFlag().toLowerCase() +".png"));
    public static Texture prideRock = TextureLoader.getTexture(resourcePath("ui/mainmenu/prideRock.png"));

    @SpirePatch(clz=TitleBackground.class, method=SpirePatch.CONSTRUCTOR)
    public static class RainbowSpire
    {
        public static TextureAtlas.AtlasRegion get()
        {
            Texture t = TextureLoader.getTexture("pridemod/ui/mainmenu/mg3Top.png");
            return new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight());
        }
        @SpireInstrumentPatch
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(FieldAccess f) throws CannotCompileException
                {
                    if (f.getFieldName().equals("mg3Top"))
                    {

                        f.replace("$proceed(pridemod.patches.ui.titlebg.PrideSpire.RainbowSpire.get());");
                    }
                }
            };
        }
    }

    @SpirePatch(clz=TitleBackground.class, method="render")
    public static class Flag
    {
        @SpireInsertPatch(locator=Locator.class)
        public static void Insert(TitleBackground __instance, SpriteBatch spriteBatch)
        {
            spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            spriteBatch.setColor(Color.WHITE);

            spriteBatch.draw(flag, 1620 * Settings.xScale, -45.0F * Settings.scale * __instance.slider + 400 * Settings.yScale, 240 * Settings.xScale, 144 * Settings.yScale, 0, 0, flag.getWidth(), flag.getHeight(), true, false);
            spriteBatch.draw(prideRock, 1500 * Settings.xScale, -45.0F * Settings.scale * __instance.slider + -40 * Settings.yScale, prideRock.getWidth() * Settings.xScale, prideRock.getHeight() * Settings.yScale);
        }

        private static class Locator extends SpireInsertLocator {

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {

                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");

                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[3]};
            }
        }
    }

}
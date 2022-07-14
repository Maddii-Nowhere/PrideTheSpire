package pridemod.patches.ui.toppanel;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.DistilledChaosPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import pridemod.PrideMain;
import pridemod.util.TextureLoader;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.potions.AbstractPotion.PotionEffect.NONE;
import static pridemod.PrideMain.resourcePath;

public class PridePanel
{
    public static Texture flag = TextureLoader.getTexture(resourcePath("flags/" + PrideMain.getFlag().toLowerCase() +".png"));

    @SpirePatch(clz= TopPanel.class,method="renderHP")
    public static class HPHeart
    {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(TopPanel __instance, SpriteBatch spriteBatch)
        {
            FrameBuffer MaskBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
            spriteBatch.end();

            MaskBuffer.begin();
            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glColorMask(false, false, false, true);
            spriteBatch.begin();
            spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);

            float ICON_W = 64.0F * Settings.scale;
            float ICON_Y = Settings.isMobile ? (Settings.HEIGHT - ICON_W - 12.0F * Settings.scale) : (Settings.HEIGHT - ICON_W);

            float hpIconX = ReflectionHacks.getPrivate(__instance, TopPanel.class, "hpIconX");

            if (__instance.hpHb.hovered) {
                spriteBatch.draw(ImageMaster.TP_HP, hpIconX - 32.0F + 32.0F * Settings.xScale, ICON_Y - 32.0F + 32.0F * Settings.yScale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 64, 64, false, false);
            } else {
                spriteBatch.draw(ImageMaster.TP_HP, hpIconX - 32.0F + 32.0F * Settings.xScale, ICON_Y - 32.0F + 32.0F * Settings.yScale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            Gdx.gl20.glColorMask(true, true, true, true);
            spriteBatch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ZERO);

            if (__instance.hpHb.hovered) {
                spriteBatch.draw(flag, hpIconX + 1f * Settings.xScale, ICON_Y + 19f * Settings.yScale, 60 * Settings.xScale, 30 * Settings.yScale);
            } else {
                spriteBatch.draw(flag, hpIconX + 6.5f * Settings.xScale, ICON_Y + 21.5f * Settings.yScale, 50 * Settings.xScale, 25 * Settings.yScale);
            }

            spriteBatch.setBlendFunction(770,771);
            spriteBatch.end();
            MaskBuffer.end();

            spriteBatch.begin();
            TextureRegion t = new TextureRegion(MaskBuffer.getColorBufferTexture());
            t.flip(false, true);
            spriteBatch.draw(t, 0, 0, 0, 0, MaskBuffer.getWidth(), MaskBuffer.getHeight(), 1f, 1f, 0f);

            FontHelper.renderFontLeftTopAligned(spriteBatch, FontHelper.topPanelInfoFont, AbstractDungeon.player.currentHealth + "/" + AbstractDungeon.player.maxHealth, hpIconX + 60.0F * Settings.scale,  Settings.isMobile ? (Settings.HEIGHT - 36.0F * Settings.scale) : (Settings.HEIGHT - 24.0F * Settings.scale), Color.SALMON);
            __instance.hpHb.render(spriteBatch);
            MaskBuffer.dispose();
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz=AbstractPotion.class,method="render")
    public static class RainbowPotion
    {
        private static Texture rainbow_T = ImageMaster.loadImage("pridemod/ui/potions/rainbowLiquid_t.png");
        private static Texture rainbow_M = ImageMaster.loadImage("pridemod/ui/potions/rainbowLiquid_m.png");
        private static Texture rainbow_C = ImageMaster.loadImage("pridemod/ui/potions/rainbowLiquid_CARD.png");
        @SpirePostfixPatch()
        public static void Postfix(AbstractPotion __instance, SpriteBatch spriteBatch)
        {
            float angle = ReflectionHacks.getPrivate(__instance, AbstractPotion.class, "angle");
            if (__instance instanceof DistilledChaosPotion)
            {
                spriteBatch.draw(rainbow_T, __instance.posX - 32.0F, __instance.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, __instance.scale, __instance.scale, angle, 0, 0, 64, 64, false, false);
            }
            if (__instance instanceof EntropicBrew)
            {
                spriteBatch.draw(rainbow_M, __instance.posX - 32.0F, __instance.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, __instance.scale, __instance.scale, angle, 0, 0, 64, 64, false, false);
            }
            if (__instance instanceof DuplicationPotion)
            {
                spriteBatch.draw(rainbow_C, __instance.posX - 32.0F, __instance.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, __instance.scale, __instance.scale, angle, 0, 0, 64, 64, false, false);
            }
        }
    }
}
package pridemod.ui;

import basemod.IUIElement;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.Set;
import java.util.function.Consumer;

public class PrideCheckBox implements IUIElement
{
    float x,y,w,h;
    ModPanel parent;
    Hitbox hitbox;
    public boolean enabled;
    BitmapFont font;
    String text;
    Consumer<PrideCheckBox> prideCheckBoxConsumer;

    public PrideCheckBox(float xPos, float yPos, ModPanel parent, String text, boolean enabled, BitmapFont font, Consumer<PrideCheckBox> prideCheckBoxConsumer)
    {
        this.x = xPos;
        this.y = yPos;
        this.w = (float)ImageMaster.OPTION_TOGGLE.getWidth() * Settings.xScale;
        this.h = (float)ImageMaster.OPTION_TOGGLE.getHeight() * Settings.yScale;
        this.parent = parent;
        this.font = font;
        this.text = text;
        this.enabled = enabled;

        this.prideCheckBoxConsumer = prideCheckBoxConsumer;
        this.hitbox = new Hitbox(this.x, this.y, (this.w * Settings.scale) + (FontHelper.getSmartWidth(font, text, 1000, 0) + this.h * Settings.scale + 12.0F), this.h * Settings.scale);
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        if (this.hitbox.hovered) {
            spriteBatch.setColor(Color.CYAN);
        } else if (this.enabled) {
            spriteBatch.setColor(Color.LIGHT_GRAY);
        } else {
            spriteBatch.setColor(Color.WHITE);
        }
        spriteBatch.draw(ImageMaster.OPTION_TOGGLE, this.x, this.y, this.w / 2.0F, this.h / 2.0F, this.w, this.h, 1.0F, 1.0F, 0, 0, 0, ImageMaster.OPTION_TOGGLE.getWidth(), ImageMaster.OPTION_TOGGLE.getHeight(), false, false);
        if (this.enabled) {
            spriteBatch.setColor(Color.WHITE);
            spriteBatch.draw(ImageMaster.OPTION_TOGGLE_ON, this.x, this.y, this.w / 2.0F, this.h / 2.0F, this.w, this.h, 1.0F, 1.0F, 0, 0, 0, ImageMaster.OPTION_TOGGLE_ON.getWidth(), ImageMaster.OPTION_TOGGLE_ON.getHeight(), false, false);
        }
        this.hitbox.render(spriteBatch);
        FontHelper.renderFontLeftTopAligned(spriteBatch, this.font, this.text, this.x  + ImageMaster.OPTION_TOGGLE.getWidth() * Settings.xScale, this.y + ImageMaster.OPTION_TOGGLE.getHeight() * Settings.yScale, Color.WHITE);
    }

    @Override
    public void update()
    {
        this.hitbox.update();
        if (this.hitbox.justHovered)
        {
            CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
        }
        if (this.hitbox.hovered && InputHelper.justClickedLeft)
        {
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
            this.hitbox.clickStarted = true;
        }
        if (this.hitbox.clicked)
        {
            this.hitbox.clicked = false;
            this.enabled = !this.enabled;
            this.prideCheckBoxConsumer.accept(this);
        }
    }

    @Override
    public int renderLayer() {
        return 0;
    }

    @Override
    public int updateOrder() {
        return 0;
    }
}

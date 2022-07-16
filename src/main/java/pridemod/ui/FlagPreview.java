package pridemod.ui;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import pridemod.PrideMain;
import pridemod.util.TextureLoader;

import static pridemod.PrideMain.resourcePath;

public class FlagPreview implements IUIElement
{
    private static Texture flag = TextureLoader.getTexture(resourcePath("flags/" + PrideMain.getFlag().toLowerCase() +".png"));

    float x,y;

    public FlagPreview(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public static void setFlag(String s)
    {
        flag = TextureLoader.getTexture(resourcePath("flags/" + s.toLowerCase() +".png"));
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(flag, x, y, (flag.getWidth() / 3f) * Settings.xScale, (flag.getHeight() / 3f) * Settings.yScale);
    }

    @Override
    public void update()
    {

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

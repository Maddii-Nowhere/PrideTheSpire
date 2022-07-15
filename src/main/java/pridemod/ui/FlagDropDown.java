package pridemod.ui;

import basemod.IUIElement;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Label;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
import pridemod.PrideMain;
import pridemod.patches.ui.titlebg.PrideSpire;
import pridemod.patches.ui.toppanel.PridePanel;
import pridemod.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static pridemod.PrideMain.resourcePath;

public class FlagDropDown implements DropdownMenuListener, IUIElement
{
    static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PrideMain.UIStringIDs.FLAG_EXPLANATION.ID);
    private Consumer<FlagDropDown> flagDropDownConsumer;
    private Consumer<Integer> integerConsumer;
    private DropdownMenu dropdownMenu;
    private Label label;
    private Label explanation;
    private float x;
    private float y;
    private float w;
    private float h;
    private ModPanel parent;
    public String selection;

    public FlagDropDown(List<String> options, float xPos, float yPos, ModPanel parent, Label label, Consumer<FlagDropDown> flagDropDownConsumer, Consumer<Integer> integerConsumer)
    {
        this.x = xPos;
        this.y = yPos;
        this.parent = parent;
        this.dropdownMenu = new DropdownMenu(this, (ArrayList<String>) options, FontHelper.buttonLabelFont, Color.CORAL);
        this.label = label;
        this.explanation = new Label(FontHelper.tipBodyFont, getExplanationText(uiStrings.TEXT_DICT.get(PrideMain.getFlag().toLowerCase())), 1200 * Settings.xScale, 500 * Settings.yScale, 0, 1, Color.WHITE);
        this.flagDropDownConsumer = flagDropDownConsumer;
        this.integerConsumer = integerConsumer;

        this.dropdownMenu.setSelectedIndex(PrideMain.getIndex());
    }

    @Override
    public void changedSelectionTo(DropdownMenu dropdownMenu, int i, String s)
    {
        selection = s;
        PrideSpire.flag = TextureLoader.getTexture(resourcePath("flags/" + s.toLowerCase() +".png"));
        PridePanel.flag = TextureLoader.getTexture(resourcePath("flags/" + s.toLowerCase() +".png"));

        this.explanation = new Label(FontHelper.tipBodyFont, getExplanationText(uiStrings.TEXT_DICT.get(s.toLowerCase())), 1200 * Settings.xScale, 500 * Settings.yScale, 0, 1, Color.WHITE);
        flagDropDownConsumer.accept(this);
        integerConsumer.accept(i);
    }

    private String getExplanationText(String s)
    {
        String[] explanationText = s.split("\\s");
        String resultText = "";
        for (int j = 0; j < explanationText.length; j++)
        {
            if (j % 10 == 9)
            {
                resultText += "\n";
            }
            resultText += explanationText[j] + " ";
        }
        return resultText;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        label.render(spriteBatch);
        explanation.render(spriteBatch);
        dropdownMenu.render(spriteBatch, x, y);
    }

    @Override
    public void update() {
        dropdownMenu.update();
    }

    @Override
    public int renderLayer() {
        return 0;
    }

    @Override
    public int updateOrder() {
        return 0;
    }

    @Override
    public void set(float xPos, float yPos) {
        this.x = xPos;
        this.y = yPos;
    }
}

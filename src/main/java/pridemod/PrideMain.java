package pridemod;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.Label;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import pridemod.ui.FlagDropDown;
import pridemod.ui.FlagPreview;
import pridemod.ui.PrideCheckBox;
import pridemod.util.GeneralUtils;
import pridemod.util.KeywordInfo;
import pridemod.util.TextureLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.megacrit.cardcrawl.helpers.ImageMaster.loadImage;


/*
Centennial
 */
@SpireInitializer
public class PrideMain implements
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber {
    public static ModInfo info;
    public static String modID;
    private static SpireConfig modConfig = null;
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "pridemod";

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new PrideMain();
    }


    public PrideMain() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");

        Properties pridemodDefaultSettings = new Properties();
        pridemodDefaultSettings.setProperty(ConfigField.FLAG.id, "progress");
        pridemodDefaultSettings.setProperty(ConfigField.INDEX.id, String.valueOf(22));
        pridemodDefaultSettings.setProperty(ConfigField.RARE_BANNER.id, String.valueOf(true));
        try
        {
            modConfig = new SpireConfig(modID, "PrideModConfig", pridemodDefaultSettings);
        }
        catch (IOException e)
        {
            logger.error("Pride Mod config initialisation failed:");
            e.printStackTrace();
        }
    }

    public enum ConfigField
    {
        INDEX("Index"),
        FLAG("Flag"),
        RARE_BANNER("Banner");

        final String id;
        ConfigField(String val)
        {
            this.id = val;
        }
    }

    public static boolean getBannerConfig()
    {
        if (modConfig == null) return true;
        return modConfig.getBool(ConfigField.RARE_BANNER.id);
    }
    public static int getIndex()
    {
        if (modConfig == null) return 0;
        return modConfig.getInt(ConfigField.INDEX.id);
    }

    public static String getFlag()
    {
        if (modConfig == null) return "Error";
        return modConfig.getString(ConfigField.FLAG.id);
    }

    private ModPanel settingsPanel;

    @Override
    public void receivePostInitialize() {

        settingsPanel = new ModPanel();

        settingsPanel.addUIElement(new FlagPreview(455 * Settings.xScale, 300 * Settings.yScale));
        settingsPanel.addUIElement(new FlagDropDown(new ArrayList<String>() {{
            add("Agender");
            add("Aromantic");
            add("Asexual");
            add("Bigender");
            add("Bisexual");
            add("Bxy");
            add("Demiboy");
            add("Demigirl");
            add("Gay");
            add("Gendercreative");
            add("Genderfluid");
            add("Genderflux");
            add("Genderqueer");
            add("Gxrl");
            add("Intersex");
            add("Lesbian");
            add("Neutrois");
            add("Nonbinary");
            add("Nxnbinary");
            add("Pangender");
            add("Pansexual");
            add("Polysexual");
            add("Progress");
            add("Transgender");
            add("TransIntersex");
        }}, 455 * Settings.xScale, 763 * Settings.yScale, settingsPanel,
                new Label(FontHelper.buttonLabelFont, "Flag: ", 400 * Settings.xScale, 750 * Settings.yScale, 0, 1, Color.WHITE),
                dropdown -> {
                    modConfig.setString(ConfigField.FLAG.id, dropdown.selection);
                    FlagPreview.setFlag(dropdown.selection);
                    saveConfig();
                },
                index -> {
                    modConfig.setInt(ConfigField.INDEX.id, index);
                    saveConfig();
                }));
        settingsPanel.addUIElement(new PrideCheckBox(1000 * Settings.xScale, 725 * Settings.yScale, settingsPanel, "Enable Rare Rainbow Banners", getBannerConfig(), FontHelper.charDescFont, prideCheckBox ->
        {
            modConfig.setBool(ConfigField.RARE_BANNER.id, prideCheckBox.enabled);
            saveConfig();
            switchRareBanner();
        }));

        saveConfig();

        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);

        ImageMaster.MERCHANT_RUG_IMG = loadImage("pridemod/npcs/merchant/merchantObjects.png");
        Texture bigBlur = loadImage("pridemod/vfx/smoke/bigBlur.png");
        ImageMaster.EXHAUST_L = new TextureAtlas.AtlasRegion(bigBlur, 0, 0, bigBlur.getWidth(), bigBlur.getHeight());
        Texture smallBlur = loadImage("pridemod/vfx/smoke/smallBlur.png");
        ImageMaster.EXHAUST_S = new TextureAtlas.AtlasRegion(smallBlur, 0, 0, smallBlur.getWidth(), smallBlur.getHeight());

        largeDefaultBanner = loadImage("pridemod/ui/cardui/banner_rareL.png");
        smallDefaultBanner = loadImage("pridemod/ui/cardui/banner_rareS.png");
        largeRainbowRareBanner = loadImage("pridemod/ui/cardui/banner_rare1024.png");
        smallRainbowRareBanner = loadImage("pridemod/ui/cardui/banner_rare512.png");
        switchRareBanner();
    }

    private static Texture smallRainbowRareBanner;
    private static Texture largeRainbowRareBanner;
    private static Texture smallDefaultBanner;
    private static Texture largeDefaultBanner;
    public void switchRareBanner()
    {
        if (getBannerConfig())
        {
            ImageMaster.CARD_BANNER_RARE = new TextureAtlas.AtlasRegion(smallRainbowRareBanner, 0, 0, smallRainbowRareBanner.getWidth(), smallRainbowRareBanner.getHeight());
            ImageMaster.CARD_BANNER_RARE_L = new TextureAtlas.AtlasRegion(largeRainbowRareBanner, 0, 0, largeRainbowRareBanner.getWidth(), largeRainbowRareBanner.getHeight());
        }
        else
        {
            ImageMaster.CARD_BANNER_RARE = new TextureAtlas.AtlasRegion(smallDefaultBanner, 0, 0, smallDefaultBanner.getWidth(), smallDefaultBanner.getHeight());
            ImageMaster.CARD_BANNER_RARE_L = new TextureAtlas.AtlasRegion(largeDefaultBanner, 0, 0, largeDefaultBanner.getWidth(), largeDefaultBanner.getHeight());
        }
    }

    public enum UIStringIDs
    {
        FLAG_EXPLANATION(modID + ":Pride Text");
        public final String ID;
        UIStringIDs(String id)
        {
            this.ID = id;
        }
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    public static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    public static final String defaultLanguage = "eng";

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            loadLocalization(getLangString());
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
//        BaseMod.loadCustomStringsFile(CardStrings.class,
//                localizationPath(lang, "CardStrings.json"));
//        BaseMod.loadCustomStringsFile(CharacterStrings.class,
//                localizationPath(lang, "CharacterStrings.json"));
//        BaseMod.loadCustomStringsFile(EventStrings.class,
//                localizationPath(lang, "EventStrings.json"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class,
//                localizationPath(lang, "OrbStrings.json"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class,
//                localizationPath(lang, "PotionStrings.json"));
//        BaseMod.loadCustomStringsFile(PowerStrings.class,
//                localizationPath(lang, "PowerStrings.json"));
//        BaseMod.loadCustomStringsFile(RelicStrings.class,
//                localizationPath(lang, "RelicStrings.json"));
        //TODO langauges
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath("eng", "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String resourcePath(String file) {
        return resourcesFolder + "/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/relics/" + file;
    }


    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(PrideMain.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    private void saveConfig() {
        try {
            modConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

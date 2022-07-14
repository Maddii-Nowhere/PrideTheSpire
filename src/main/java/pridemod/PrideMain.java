package pridemod;

import basemod.BaseMod;
import basemod.IUIElement;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
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
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import jdk.internal.loader.Resource;
import jdk.internal.module.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import pridemod.ui.FlagDropDown;
import pridemod.util.GeneralUtils;
import pridemod.util.KeywordInfo;
import pridemod.util.TextureLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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

        try
        {
            Properties defaults = new Properties();
            defaults.put("Flag", "progress");
            modConfig = new SpireConfig(modID, "Config", defaults);
        }
        catch (IOException e)
        {
            logger.warn(e);
        }
    }

    public static String getFlag()
    {
        if (modConfig == null) return "Error";
        return modConfig.getString("Flag");
    }

    private ModPanel settingsPanel;

    @Override
    public void receivePostInitialize() {

        settingsPanel = new ModPanel();

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
            modConfig.setString("Flag", dropdown.selection);
            saveConfig();
                }));


        saveConfig();
        //settingsPanel.addUIElement

        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);

        ImageMaster.MERCHANT_RUG_IMG = loadImage("pridemod/npcs/merchant/merchantObjects.png");


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

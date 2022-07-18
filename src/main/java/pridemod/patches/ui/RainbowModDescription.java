package pridemod.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import pridemod.PrideMain;

@SpirePatch2(
        cls = "com.evacipated.cardcrawl.modthespire.patches.modsscreen.ModsScreen",
        method = "update"
)
public class RainbowModDescription
{
    private static int save = -1;

    public static void Prefix(int ___selectedMod)
    {
        save = ___selectedMod;
    }

    public static void Postfix(int ___selectedMod)
    {
        if (___selectedMod != save) {
            PrideMain.randomizeDescriptionColors();
        }
    }
}

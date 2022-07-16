package pridemod.patches.scenes;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class CutscenePatch
{
    @SpirePatch(clz= Cutscene.class,method= SpirePatch.CONSTRUCTOR)
    public static class RainbowCutscene
    {
        private static int count = 0;
        @SpireInstrumentPatch
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(NewExpr e) throws CannotCompileException
                {
                    count++;
                    if (e.getClassName().equals(CutscenePanel.class.getName()))
                    {
                        if (count == 9)
                        {
                            e.replace("$_ = new com.megacrit.cardcrawl.cutscenes.CutscenePanel(\"" + "pridemod/scenes/defect2.png" + "\");");
                        }
                        else if (count == 3)
                        {
                            e.replace("$_ = new com.megacrit.cardcrawl.cutscenes.CutscenePanel(\"" + "pridemod/scenes/ironclad2.png" + "\");");
                        }
                        else if (count == 6)
                        {
                            e.replace("$_ = new com.megacrit.cardcrawl.cutscenes.CutscenePanel(\"" + "pridemod/scenes/silent2.png" + "\");");
                        }
                        else if (count == 12)
                        {
                            e.replace("$_ = new com.megacrit.cardcrawl.cutscenes.CutscenePanel(\"" + "pridemod/scenes/watcher2.png" + "\");");
                        }
                    }
                }
            };
        }
    }
}

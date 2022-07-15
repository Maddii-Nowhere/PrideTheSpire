package pridemod.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import pridemod.PrideMain;

public class TransientPatch
{
    @SpirePatch(clz=Transient.class,method=SpirePatch.CONSTRUCTOR)
    public static class TransTransient
    {
        @SpireInstrumentPatch
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("loadAnimation"))
                    {
                        m.replace("loadAnimation(\"pridemod/monsters/transient/skeleton.atlas\", \"pridemod/monsters/transient/skeleton.json\", 1.0F);");
                    }
                }
            };
        }
    }
}

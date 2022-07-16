//package pridemod.patches.ui.cards;
//
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.helpers.ImageMaster;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.NewExpr;
//import pridemod.PrideMain;
//
//public class PrideRareBanner
//{
//    @SpirePatch(clz= ImageMaster.class,method="initialize")
//    public static class CardUIPatch
//    {
//        protected static int count = 0;
//        @SpireInstrumentPatch
//        public static ExprEditor Instrument()
//        {
//            return new ExprEditor()
//            {
//                public void edit(NewExpr e) throws CannotCompileException
//                {
//                    if (e.getClassName().equals(TextureAtlas.class.getName()) && PrideMain.getBannerConfig())
//                    {
//                        if (count < 1)
//                        {
//                            count++;
//                        }
//                        else
//                        {
//                            e.replace("if (pridemod.PrideMain.getBannerConfig()) {" +
//                                    "$_ = new " + TextureAtlas.class.getName() + "(\"pridemod/ui/cardui/cardui.atlas\");" +
//                                    "} else {" +
//                                    "$_ = $proceed($$);" +
//                                    "}");
//                        }
//                    }
//                }
//            };
//        }
//    }
//}

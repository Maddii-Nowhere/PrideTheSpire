package pridemod.patches.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import pridemod.PrideMain;

public class PrideLasers
{
	private static ShaderProgram shader = null;

	private static void initShader()
	{
		if (shader == null) {
			try {
				shader = new ShaderProgram(
						Gdx.files.internal(PrideMain.resourcePath("shaders/rainbowLaser.vs")),
						Gdx.files.internal(PrideMain.resourcePath("shaders/rainbowLaser.fs"))
				);
			} catch (GdxRuntimeException e) {
				System.out.println("ERROR: Failed to load rainbow laser shader:");
				e.printStackTrace();
			}
		}
	}

	@SpirePatch2(
			clz = MindblastEffect.class,
			method = "render"
	)
	public static class PlayerLasers
	{
		public static void Prefix(SpriteBatch sb, float ___x, boolean ___flipHorizontal)
		{
			initShader();
			if (shader != null) {
				sb.setShader(shader);
				ShaderProgram.pedantic = true;
				shader.setUniformf("u_xStart", ___x);
				shader.setUniformf("u_xEnd", ___flipHorizontal ? 0 : Settings.WIDTH);
			}
		}

		public static void Postfix(SpriteBatch sb)
		{
			sb.setShader(null);
		}
	}

	@SpirePatch2(
			clz = LaserBeamEffect.class,
			method = "render"
	)
	public static class AutomatonLaser
	{
		public static void Prefix(SpriteBatch sb, float ___x)
		{
			initShader();
			if (shader != null) {
				sb.setShader(shader);
				ShaderProgram.pedantic = true;
				shader.setUniformf("u_xStart", ___x);
				shader.setUniformf("u_xEnd", 0);
			}
		}

		public static void Postfix(SpriteBatch sb)
		{
			sb.setShader(null);
		}
	}

	@SpirePatch2(
			clz = SmallLaserEffect.class,
			method = "render"
	)
	public static class BronzeOrbLaser
	{
		public static void Prefix(SpriteBatch sb, float ___dX, float ___sX)
		{
			initShader();
			if (shader != null) {
				sb.setShader(shader);
				ShaderProgram.pedantic = true;
				// Bronze Orbs treat the player as the "source" and themselves
				// as the "destination" of this vfx for some reason
				shader.setUniformf("u_xStart", ___dX);
				shader.setUniformf("u_xEnd", ___sX);
			}
		}

		public static void Postfix(SpriteBatch sb)
		{
			sb.setShader(null);
		}
	}
}

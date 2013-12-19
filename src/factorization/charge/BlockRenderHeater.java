package factorization.charge;

import net.minecraft.client.renderer.RenderBlocks;

import org.lwjgl.opengl.GL11;

import factorization.common.BlockIcons;
import factorization.shared.FactorizationBlockRender;
import factorization.shared.FactoryType;
import factorization.shared.FzConfig;

public class BlockRenderHeater extends FactorizationBlockRender {

    @Override
    public void render(RenderBlocks rb) {
        float d = 0.5F / 32F;
        if (!world_mode || !FzConfig.renderTEs) {
            float c = 0.1F;
            GL11.glColor4f(c, c, c, 1F);
            //Tessellator.instance.setColorOpaque_F(c, c, c);
            renderPart(rb, BlockIcons.heater_heat, d, d, d, 1 - d, 1 - d, 1 - d);
            GL11.glColor4f(1, 1, 1, 1);
        }
        renderNormalBlock(rb, getFactoryType().md);
    }

    @Override
    public FactoryType getFactoryType() {
        return FactoryType.HEATER;
    }

}
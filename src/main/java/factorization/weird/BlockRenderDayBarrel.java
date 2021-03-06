package factorization.weird;

import factorization.api.FzOrientation;
import factorization.util.DataUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import factorization.api.Quaternion;
import factorization.common.BlockIcons;
import factorization.common.FactoryType;
import factorization.shared.BlockRenderHelper;
import factorization.shared.Core;
import factorization.shared.FactorizationBlockRender;

public class BlockRenderDayBarrel extends FactorizationBlockRender {

    @Override
    public boolean render(RenderBlocks rb) {
        if (world_mode) {
            return doRender(rb, 0);
        } else {
            doRender(rb, 0);
            GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            doRender(rb, 1);
            GL11.glPopAttrib();
            return true;
        }
    }
    
    @Override
    public boolean renderSecondPass(RenderBlocks rb) {
        //NOTE: We can almost get away with doing this in the first render pass.
        //But GL_BLEND is not consistently enabled.
        return doRender(rb, 1);
        //We can also almost get away with enabling GL_BLEND in this ISBRH.
        //But then my conscience attacks.
    }
    
    boolean doRender(RenderBlocks rb, int pass) {
        BlockRenderHelper block = Core.registry.blockRender;
        TileEntityDayBarrel barrel;
        if (world_mode) {
            barrel = (TileEntityDayBarrel) te;
        } else {
            barrel = (TileEntityDayBarrel) FactoryType.DAYBARREL.getRepresentative();
            barrel.loadFromStack(is);
        }
        if (rb.hasOverrideBlockTexture()) {
            block.useTexture(rb.overrideBlockTexture);
        } else {
            if (pass == 0) {
                for (int i = 0; i < 6; i++) {
                    block.setTexture(i, barrel.getIcon(ForgeDirection.getOrientation(i)));
                }
            } else {
                BlockIcons.BarrelTextureset set;
                switch (barrel.type) {
                case HOPPING: set = BlockIcons.hopping; break;
                case SILKY: set = BlockIcons.silky; break;
                case STICKY: set = BlockIcons.sticky; break;
                default: set = BlockIcons.normal; break;
                }
                block.useTexture(set.side);
                IIcon top = set.top;
                Block log = DataUtil.getBlock(barrel.woodLog);
                if (log != null && log.getMaterial() != Material.wood) {
                    top = set.top_metal;
                }
                block.setTexture(0, top);
                block.setTexture(1, top);
                block.setTexture(4, set.front);
            }
        }
        float blockOffset = pass == 0 ? 0 : -1F/512F;
        block.setBlockBoundsOffset(blockOffset, blockOffset, blockOffset);
        if (world_mode) {
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(w, x, y, z));
            FzOrientation fzo = barrel.orientation.getSwapped();
            Quaternion q = Quaternion.fromOrientation(fzo);
            if (barrel.notice_target == barrel) {
                block.simpleCull(fzo, w, x, y, z);
            }
            block.beginWithMirroredUVs();
            block.rotateMiddle(q);
            return block.renderRotated(Tessellator.instance, x, y, z);
        } else {
            block.renderForInventory(rb);
            final float d = 1F/64F;
            block.setBlockBoundsOffset(d, 0, d);
            return true;
        }
    }
    
    
    @Override
    public FactoryType getFactoryType() {
        return FactoryType.DAYBARREL;
    }

}

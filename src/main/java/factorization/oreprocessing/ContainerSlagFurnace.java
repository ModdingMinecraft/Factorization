package factorization.oreprocessing;

import factorization.common.ContainerFactorization;
import factorization.shared.TileEntityFactorization;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerSlagFurnace extends ContainerFactorization {
    int lastBurnTime = -1, lastFuelItemBurnTime = -1, lastCookTime = -1;
    TileEntitySlagFurnace furnace;

    public ContainerSlagFurnace(EntityPlayer entityplayer, TileEntityFactorization factory) {
        super(entityplayer, factory);
        furnace = (TileEntitySlagFurnace) factory;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (ICrafting crafter : (Iterable<ICrafting>) crafters) {
            if (furnace.furnaceBurnTime != lastBurnTime) {
                crafter.sendProgressBarUpdate(this, 0, furnace.furnaceBurnTime);
            }
            if (furnace.furnaceCookTime != lastCookTime) {
                crafter.sendProgressBarUpdate(this, 1, furnace.furnaceCookTime);
            }
            if (furnace.currentFuelItemBurnTime != lastFuelItemBurnTime) {
                crafter.sendProgressBarUpdate(this, 2, furnace.currentFuelItemBurnTime);
            }
        }
        lastBurnTime = furnace.furnaceBurnTime;
        lastCookTime = furnace.furnaceCookTime;
        lastFuelItemBurnTime = furnace.currentFuelItemBurnTime;
    }

    @Override // -- stupid server
    public void updateProgressBar(int index, int val) {
        switch (index) {
        case 0:
            furnace.furnaceBurnTime = val;
            break;
        case 1:
            furnace.furnaceCookTime = val;
            break;
        case 2:
            furnace.currentFuelItemBurnTime = val;
            break;
        }
    }
}

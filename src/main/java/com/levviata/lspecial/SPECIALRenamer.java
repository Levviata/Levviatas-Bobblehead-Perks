package com.levviata.lspecial;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class SPECIALRenamer {
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!LSPECIALConfig.bobbleheadRename) return;
        ItemStack stack = event.getItemStack();

        if (stack.getItem() == Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead"))) {

            String stat = null;

            switch (stack.getMetadata()) {
                case 1:
                    stat = "Strength";
                    break;
                case 2:
                    stat = "Perception";
                    break;
                case 3:
                    stat = "Endurance";
                    break;
                case 4:
                    stat = "Charisma";
                    break;
                case 5:
                    stat = "Intelligence";
                    break;
                case 6:
                    stat = "Agility";
                    break;
                case 7:
                    stat = "Luck";
                    break;
            }

            if (stat == null || event.getToolTip().isEmpty()) {
                return;
            }

            String tooltipName = event.getToolTip().get(0);

            int index = tooltipName.lastIndexOf(" (#");

            if (index != -1) {
                tooltipName = tooltipName.substring(0, index)
                        + " (" + stat + ")"
                        + tooltipName.substring(index);
            } else {
                tooltipName += " (" + stat + ")";
            }

            event.getToolTip().set(0, tooltipName);
        }
    }
}

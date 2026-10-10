package com.levviata.lspecial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Objects;
import static com.levviata.lspecial.SPECIALScoreboard.inst;

public class BobbleheadUse {
    private int augLimit = 0;
    private static final String USES_TAG = "lspecialUses";

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        Item bobblehead = Item.getByNameOrId("hbm:bobblehead");
        if (bobblehead == null) return;
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || stack.getItem() != bobblehead || !player.isSneaking()) return;

        inst.scoreboard = player.getEntityWorld().getScoreboard();
        inst.startBoards();
        inst.startScores(player);

        String mode = LSPECIALConfig.bobbleheadConsumption;
        if (!"ON_USE".equals(mode) && stack.getCount() > 1) {
            ItemStack individual = stack.splitStack(1);
            if (!player.inventory.addItemStackToInventory(individual)) player.dropItem(individual, false);
            stack = individual;
        }
        int meta = stack.getMetadata();
        boolean granted = false;
        switch (meta) {
            case 1: granted = grant(inst.getStrengthScore(), LSPECIALConfig.strength, stack, player); break;
            case 2: granted = grant(inst.getPerceptionScore(), LSPECIALConfig.perception, stack, player); break;
            case 3: granted = grantWithPerk(inst.getEnduranceScore(), inst.getEnduranceBonusScore(), LSPECIALConfig.endurance, LSPECIALConfig.enduranceBonus, stack, player); break;
            case 4: granted = grantWithPerk(inst.getCharismaScore(), inst.getCharismaBonusScore(), LSPECIALConfig.charisma, LSPECIALConfig.charismaBonus, stack, player); break;
            case 5: granted = grant(inst.getIntelligenceScore(), LSPECIALConfig.intelligence, stack, player); break;
            case 6: granted = grant(inst.getAgilityScore(), LSPECIALConfig.agility, stack, player); break;
            case 7: granted = grant(inst.getLuckScore(), LSPECIALConfig.luck, stack, player); break;
            default:
                if (meta > 7 && LSPECIALConfig.statLimit && inst.getLimitScore().getScorePoints() < 2147483647) {
                    if ("NEVER".equals(mode) && uses(stack) >= 1) break;
                    inst.getLimitScore().setScorePoints(inst.getLimitScore().getScorePoints() + 1);
                    showAugLimit(player);
                    granted = true;
                    consumeAccordingToMode(stack, player);
                }
        }
        if (granted && player instanceof EntityPlayerMP) LSPECIALMod.syncStats((EntityPlayerMP) player);
    }

    private boolean grant(Score score, boolean enabled, ItemStack stack, EntityPlayer player) {
        if (!enabled) return false;
        if (LSPECIALConfig.statLimit && score.getScorePoints() >= inst.getLimitScore().getScorePoints()) {
            message(player, LSPECIALConfig.limitMessage); return false;
        }
        if ("NEVER".equals(LSPECIALConfig.bobbleheadConsumption) && uses(stack) >= 1) return false;
        int gain = LSPECIALConfig.statsPerBobblehead;
        if (LSPECIALConfig.statLimit) gain = Math.min(gain, Math.max(0, inst.getLimitScore().getScorePoints() - score.getScorePoints()));
        if (gain <= 0) { message(player, LSPECIALConfig.limitMessage); return false; }
        score.setScorePoints(score.getScorePoints() + gain);
        consumeAccordingToMode(stack, player);
        return true;
    }

    private boolean grantWithPerk(Score score, Score bonus, boolean enabled, boolean bonusEnabled, ItemStack stack, EntityPlayer player) {
        if (!grant(score, enabled, stack, player)) return false;
        if (bonusEnabled && score.getScorePoints() >= 4 && bonus.getScorePoints() != 1) bonus.setScorePoints(1);
        return true;
    }

    private int uses(ItemStack stack) { return stack.hasTagCompound() ? stack.getTagCompound().getInteger(USES_TAG) : 0; }
    private void consumeAccordingToMode(ItemStack stack, EntityPlayer player) {
        String mode = LSPECIALConfig.bobbleheadConsumption;
        if ("ON_USE".equals(mode)) { stack.shrink(1); return; }
        int uses = uses(stack) + 1;
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(USES_TAG, uses);
        if ("EVERY_X_USES".equals(mode) && uses >= LSPECIALConfig.bobbleheadUsesPerConsumption) stack.shrink(1);
    }
    private void message(EntityPlayer player, String text) { if (LSPECIALConfig.hotbarMessages && text != null && !text.isEmpty()) player.sendStatusMessage(new TextComponentString(text), true); }
    private void showAugLimit(EntityPlayer player) {
        if (!LSPECIALConfig.statLimit) return;
        augLimit++;
        if (!LSPECIALConfig.variedAugLimitMessages) { message(player, LSPECIALConfig.augLimitMessage); return; }
        if (augLimit >= 20) message(player, LSPECIALConfig.aug20);
        else if (augLimit >= 10) message(player, LSPECIALConfig.aug10);
        else if (augLimit >= 5) message(player, LSPECIALConfig.aug5);
        else message(player, LSPECIALConfig.augUnder5);
    }
}

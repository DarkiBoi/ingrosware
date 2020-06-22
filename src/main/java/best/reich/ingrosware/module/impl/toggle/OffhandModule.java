package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Bind;
import best.reich.ingrosware.setting.annotation.Clamp;
import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.event.impl.other.KeyPressEvent;
import best.reich.ingrosware.module.ModuleCategory;


/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
@Toggleable(label = "Offhand", category = ModuleCategory.COMBAT, color = 0xffff3f0f, bind = Keyboard.KEY_NONE)
public class OffhandModule extends ToggleableModule {

    @Setting("Toggle-Totem")
    public boolean toggletotem = true;

    @Setting("Crystal-Check")
    public boolean crystalCheck = true;

    @Setting("Hole-Check")
    public boolean holeCheck = true;

    @Setting("Gap-Swap")
    public boolean gapSwap = true;

    @Bind(pressed = false)
    @Setting("Gapple-Bind")
    public int gappleBind = Keyboard.KEY_NONE;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Gapple-Health")
    public int gappleHealth = 20;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Gapple-Hole-Health")
    public int gappleHoleHealth = 8;

    @Bind(pressed = false)
    @Setting("Crystal-Bind")
    public int crystalBind = Keyboard.KEY_NONE;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Crystal-Health")
    public int crystalHealth = 20;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Crystal-Hole-Health")
    public int crystalHoleHealth = 6;

    @Bind(pressed = false)
    @Setting("Obsidian-Bind")
    public int obsidianBind = Keyboard.KEY_NONE;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Obsidian-Health")
    public int obsidianHealth = 20;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Obsidian-Hole-Health")
    public int obsidianHoleHealth = 8;

    @Bind(pressed = false)
    @Setting("Web-Bind")
    public int webBind = Keyboard.KEY_NONE;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Web-Health")
    public int webHealth = 20;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("Web-Hole-Health")
    public int webHoleHealth = 8;

    @Clamp(minimum = "1")
    @Setting("TargetRange")
    public int targetRange = 10;

    public Mode mode = Mode.CRYSTALS;
    public Mode oldMode = Mode.CRYSTALS;
    private int oldSlot = -1;
    private boolean swapToTotem = false, eatingApple = false, oldSwapToTotem = false;

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        if (crystalBind == event.getKey()) {
            if (mode == Mode.CRYSTALS) {
                setSwapToTotem(!isSwapToTotem());
            } else setSwapToTotem(false);
            setMode(Mode.CRYSTALS);
        }
        if (gappleBind == event.getKey()) {
            if (mode == Mode.GAPPLES) {
                setSwapToTotem(!isSwapToTotem());
            } else setSwapToTotem(false);
            setMode(Mode.GAPPLES);
        }
        if (obsidianBind == event.getKey()) {
            if (mode == Mode.OBSIDIAN) {
                setSwapToTotem(!isSwapToTotem());
            } else setSwapToTotem(false);
            setMode(Mode.OBSIDIAN);
        }
        if (webBind == event.getKey()) {
            if (mode == Mode.WEBS) {
                setSwapToTotem(!isSwapToTotem());
            } else setSwapToTotem(false);
            setMode(Mode.WEBS);
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof GuiContainer)
            return;
        setSuffix(getRenderSuffix());
        if (gapSwap) {
            if (!(getSlot(Mode.GAPPLES) == -1 && mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) && mc.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                setMode(Mode.GAPPLES);
                eatingApple = true;
                swapToTotem = false;
            } else {
                if (eatingApple) {
                    setMode(oldMode);
                    swapToTotem = oldSwapToTotem;
                    eatingApple = false;
                } else {
                    oldMode = mode;
                    oldSwapToTotem = swapToTotem;
                }
            }
        }
        if (!shouldTotem()) {
            if (!(mc.player.getHeldItemOffhand() != ItemStack.EMPTY && isItemInOffhand())) {
                final int slot = getSlot(mode) < 9 ? getSlot(mode) + 36 : getSlot(mode);
                if (getSlot(mode) != -1) {
                    if (oldSlot != -1) {
                        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                        mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
                    }
                    oldSlot = slot;
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                }
            }
        } else if (!eatingApple && !(mc.player.getHeldItemOffhand() != ItemStack.EMPTY && mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
            final int slot = getTotemSlot() < 9 ? getTotemSlot() + 36 : getTotemSlot();
            if (getTotemSlot() != -1) {
                mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
                oldSlot = -1;
            }
        }
    }

    private boolean noNearbyPlayers() {
        return mode == Mode.CRYSTALS && mc.world.playerEntities.stream().noneMatch(e -> e != mc.player && !IngrosWare.INSTANCE.getFriendManager().isFriend(e.getUniqueID()) && mc.player.getDistanceToEntity(e) <= targetRange);
    }

    private boolean isItemInOffhand() {
        switch (mode) {
            case GAPPLES:
                return mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
            case CRYSTALS:
                return mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            case OBSIDIAN:
                return mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock) mc.player.getHeldItemOffhand().getItem()).getBlock() == Blocks.OBSIDIAN;
            case WEBS:
                return mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock) mc.player.getHeldItemOffhand().getItem()).getBlock() == Blocks.WEB;
        }
        return false;
    }

    private boolean isHeldInMainHand() {
        switch (mode) {
            case GAPPLES:
                return mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE;
            case CRYSTALS:
                return mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL;
            case OBSIDIAN:
                return mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock) mc.player.getHeldItemMainhand().getItem()).getBlock() == Blocks.OBSIDIAN;
            case WEBS:
                return mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock) mc.player.getHeldItemMainhand().getItem()).getBlock() == Blocks.WEB;
        }
        return false;
    }

    private boolean shouldTotem() {
        if (isHeldInMainHand() || isSwapToTotem()) return true;
        if (holeCheck && isInHole(mc.player)) {
            return (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= getHoleHealth() || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || mc.player.fallDistance >= 3 || noNearbyPlayers() || (crystalCheck && isCrystalsAABBEmpty());
        }
        return (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= getHealth() || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || mc.player.fallDistance >= 3 || noNearbyPlayers() || (crystalCheck && isCrystalsAABBEmpty());
    }

    private boolean isNotEmpty(BlockPos pos) {
        return mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof EntityEnderCrystal);
    }

    private float getHealth() {
        switch (mode) {
            case CRYSTALS:
                return crystalHealth;
            case GAPPLES:
                return gappleHealth;
            case OBSIDIAN:
                return obsidianHealth;
        }
        return webHealth;
    }

    private float getHoleHealth() {
        switch (mode) {
            case CRYSTALS:
                return crystalHoleHealth;
            case GAPPLES:
                return gappleHoleHealth;
            case OBSIDIAN:
                return obsidianHoleHealth;
        }
        return webHoleHealth;
    }

    private boolean isCrystalsAABBEmpty() {
        return isNotEmpty(mc.player.getPosition().add(1, 0, 0)) || isNotEmpty(mc.player.getPosition().add(-1, 0, 0)) || isNotEmpty(mc.player.getPosition().add(0, 0, 1)) || isNotEmpty(mc.player.getPosition().add(0, 0, -1)) || isNotEmpty(mc.player.getPosition());
    }

    int getStackSize() {
        int size = 0;
        if (shouldTotem()) {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    size += mc.player.inventory.getStackInSlot(i).getCount();
                }
            }
        } else if (mode == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == Blocks.OBSIDIAN) {
                    size += mc.player.inventory.getStackInSlot(i).getCount();
                }
            }
        } else if (mode == Mode.WEBS) {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == Blocks.WEB) {
                    size += mc.player.inventory.getStackInSlot(i).getCount();
                }
            }
        } else {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == (mode == Mode.CRYSTALS ? Items.END_CRYSTAL : Items.GOLDEN_APPLE)) {
                    size += mc.player.inventory.getStackInSlot(i).getCount();
                }
            }
        }
        return size;
    }

    int getSlot(Mode m) {
        int slot = -1;
        if (m == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == Blocks.OBSIDIAN) {
                    slot = i;
                    break;
                }
            }
        } else if (m == Mode.WEBS) {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == Blocks.WEB) {
                    slot = i;
                    break;
                }
            }
        } else {
            for (int i = 45; i > 0; i--) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == (m == Mode.CRYSTALS ? Items.END_CRYSTAL : Items.GOLDEN_APPLE)) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; i--) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }

    private boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    private boolean isBlockValid(BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }

    private boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }

        return true;
    }

    private boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }

        return true;
    }

    private boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

    public String getRenderSuffix() {
        return getModeStr() + "," + getStackSize();
    }

    private String getModeStr() {
        if (!shouldTotem()) {
            switch (mode) {
                case GAPPLES:
                    return "G";
                case WEBS:
                    return "W";
                case OBSIDIAN:
                    return "O";
                default:
                    return "C";
            }
        }
        return "T";
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isSwapToTotem() {
        return swapToTotem;
    }

    public void setSwapToTotem(boolean swapToTotem) {
        this.swapToTotem = swapToTotem;
    }

    public enum Mode {
        CRYSTALS,
        GAPPLES,
        OBSIDIAN,
        WEBS
    }
}
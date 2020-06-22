package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.entity.MoveStateEvent;
import best.reich.ingrosware.module.ModuleCategory;

@Toggleable(label = "InvWalk", category = ModuleCategory.MOVEMENT,color = 0xff72B3ff,bind = Keyboard.KEY_NONE)
public class InvWalkModule extends ToggleableModule {

    @Subscribe
    public void onMoveState(MoveStateEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            mc.player.rotationYaw += Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 4 : Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? -4 : 0;
            mc.player.rotationPitch += (Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 4 : Keyboard.isKeyDown(Keyboard.KEY_UP) ? -4 : 0) * 0.75;
            mc.player.rotationPitch = MathHelper.clamp(mc.player.rotationPitch, -90, 90);
            mc.player.movementInput.moveStrafe = 0.0f;
            mc.player.movementInput.moveForward = 0.0f;
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                ++mc.player.movementInput.moveForward;
                mc.player.movementInput.forwardKeyDown = true;
            } else {
                mc.player.movementInput.forwardKeyDown = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                --mc.player.movementInput.moveForward;
                mc.player.movementInput.backKeyDown = true;
            } else {
                mc.player.movementInput.backKeyDown = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                ++mc.player.movementInput.moveStrafe;
                mc.player.movementInput.leftKeyDown = true;
            } else {
                mc.player.movementInput.leftKeyDown = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                --mc.player.movementInput.moveStrafe;
                mc.player.movementInput.rightKeyDown = true;
            } else {
                mc.player.movementInput.rightKeyDown = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                mc.player.movementInput.jump = true;
            }
        }
    }
}
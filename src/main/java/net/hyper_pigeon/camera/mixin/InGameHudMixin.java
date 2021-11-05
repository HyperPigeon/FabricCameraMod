package net.hyper_pigeon.camera.mixin;

import net.hyper_pigeon.camera.Camera;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

//   @Shadow
//   private MinecraftClient client;
//
//    @Inject(at = {@At("TAIL")}, method = {"render"})
//    public void renderCameraView(MatrixStack matrices, float tickDelta, CallbackInfo ci){
//        ItemStack itemStack = this.client.player.inventory.getMainHandStack();
//        if (this.client.options.getPerspective().isFirstPerson() && (this.client.player.isUsingItem() && this.client.player.getActiveItem().getItem().equals(Camera.CAMERA_ITEM))) {
//            this.client.options.hudHidden = true;
//        }
//    }
}

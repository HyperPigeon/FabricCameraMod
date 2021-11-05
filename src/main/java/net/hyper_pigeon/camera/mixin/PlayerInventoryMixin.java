package net.hyper_pigeon.camera.mixin;


import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.client.render.CameraOverlay;
import net.hyper_pigeon.camera.client.render.CameraScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
//    @Inject(at = {@At("HEAD")},
//            method = {"scrollInHotbar(D)V"},
//            cancellable = true)
//    private void onScrollInHotbar(double scrollAmount, CallbackInfo ci)
//    {
//        if(MinecraftClient.getInstance().player.getActiveItem() != null) {
//            if(MinecraftClient.getInstance().player.getActiveItem().getItem().equals(Camera.CAMERA_ITEM)) {
//                ci.cancel();
//            }
//        }
//
//    }
}

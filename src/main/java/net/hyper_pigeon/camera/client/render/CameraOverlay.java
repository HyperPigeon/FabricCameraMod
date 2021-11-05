package net.hyper_pigeon.camera.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;

public class CameraOverlay extends Overlay {
    public final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    public final double defaultFOV;
    public double currentFOV;
    public final Overlay previousOverlay;

    public CameraOverlay(double fov, Overlay overlay) {
        super();
        defaultFOV = fov;
        currentFOV = defaultFOV;
        previousOverlay = overlay;
    }


    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        minecraftClient.options.hudHidden = true;
    }

    public boolean pausesGame() {
        return false;
    }

    public void close(){
        minecraftClient.options.hudHidden = false;
        minecraftClient.options.fov = defaultFOV;
        minecraftClient.setOverlay(previousOverlay);
        //minecraftClient.setOverlay(null);
    }

}

package net.hyper_pigeon.camera.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "camera")
public class CameraConfig implements ConfigData {
    public boolean saveImagesOnServer = false;
    public String serverImageDirectoryPath = "";
}

package com.big_Xplosion.blazeInstaller;

import com.big_Xplosion.blazeInstaller.gui.InstallerPanel;
import com.big_Xplosion.blazeInstaller.util.OS;

public class BlazeInstaller
{
    public static void main(String[] args)
    {
        launchGui();
    }

    private static void launchGui()
    {
        InstallerPanel panel = new InstallerPanel(OS.getMinecraftDir());
        panel.run();
    }
}
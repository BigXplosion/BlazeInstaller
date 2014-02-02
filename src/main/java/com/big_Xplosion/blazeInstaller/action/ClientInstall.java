package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

public class ClientInstall implements IInstallerAction
{
    @Override
    public boolean install(File target) throws IOException
    {
        File versionRoot = new File(target, "versions");
        File libRoot = new File(target, "libraries");

        if (!downloadBL(target))
            return false;

        return true;
    }

    @Override
    public boolean isPathValid(File target)
    {
        if (target.exists())
        {
            File profiles = new File(target, "launcher_profiles.json");
            return profiles.exists();
        }

        return false;
    }

    @Override
    public String getSuccessMessage()
    {
        return "Successfully installed BlazeLoader.";
    }

    @Override
    public String getFileErrorMessage(File targetFile)
    {
        if (targetFile.exists())
            return "The directory is missing a launcher profile. Please run the minecraft launcher first";
        else
            return "There is no minecraft directory set up. Either choose an alternative, or run the minecraft launcher to create one";
    }

    private boolean downloadBL(File target)
    {
        // TODO: implement when we have a proper way of doing it.
        return true;
    }
}

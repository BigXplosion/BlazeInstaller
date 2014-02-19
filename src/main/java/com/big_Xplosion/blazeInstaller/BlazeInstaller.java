package com.big_Xplosion.blazeInstaller;

import com.big_Xplosion.blazeInstaller.action.InstallType;
import com.big_Xplosion.blazeInstaller.config.InstallerPreferences;
import com.big_Xplosion.blazeInstaller.gui.InstallerGUI;
import com.big_Xplosion.blazeInstaller.gui.InstallerPanel;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.util.Arrays.asList;

public class BlazeInstaller
{
    public static void main(String[] args)
    {
        //TODO: More options
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("mcp", "m"), "Install BlazeLoader in MCP.").withRequiredArg().ofType(File.class);
        parser.acceptsAll(asList("client", "c"), "Install BlazeLoader in a client Minecraft profile.").withRequiredArg().ofType(File.class);
        parser.acceptsAll(asList("blazeloader", "bl"), "If true this will setup MCP for developing blazeloader itself, if false it will setup MCP for making mods with BlazeLoader.").withRequiredArg().ofType(Boolean.class);
        parser.acceptsAll(asList("gui", "g"), "Force to open GUI even though running via the command line").withOptionalArg().ofType(Boolean.class);
        parser.acceptsAll(asList("forge", "f"), "Use Minecraft Forge").withRequiredArg().ofType(String.class);
        OptionSet options = parser.parse(args);


        if (options.specs().size() > 0)
        {
            InstallerPreferences.USE_GUI = false;
            handleOptions(options);
        }
        else
        {
            InstallerPreferences.USE_GUI = true;
            launchGui();
        }
    }

    private static void handleOptions(OptionSet options)
    {
        if (options.has("gui"))
        {
            if (options.hasArgument("gui") ? (Boolean) options.valueOf("gui") : true)
            {
                launchGui();
                return;
            }
        }

        if (options.has("mcp"))
        {
            File mcp = options.hasArgument("mcp") ? (File) options.valueOf("mcp") : new File(".");

            if (options.has("blazeloader") && (Boolean) options.valueOf("blazeloader"))
                InstallerPreferences.BL_DEV = true;
            else
                InstallerPreferences.BL_DEV = false;

            try
            {
                InstallType.MCP.install(mcp);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            return;
        }

        if (options.has("client"))
        {
            File client = options.hasArgument("client") ? (File) options.valueOf("client") : new File(".");

            try
            {
                InstallType.MCP.install(client);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static void launchGui()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                InstallerGUI gui = new InstallerGUI();
            }
        });
    }
}
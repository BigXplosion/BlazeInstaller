package com.big_Xplosion.blazeInstaller;

import com.big_Xplosion.blazeInstaller.action.InstallType;
import com.big_Xplosion.blazeInstaller.gui.InstallerPanel;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;

import static java.util.Arrays.asList;

public class BlazeInstaller
{
    public static void main(String[] args)
    {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("mcp", "m"), "Install BlazeLoader in MCP.").withRequiredArg().ofType(File.class);
        parser.acceptsAll(asList("client", "c"), "Install BlazeLoader in a client Minecraft profile.").withRequiredArg().ofType(File.class);
        OptionSet options = parser.parse(args);

        if (options.specs().size() > 0)
        {
            System.setProperty("bli.gui", "false");
            handleOptions(options);
        }
        else
        {
            System.setProperty("bli.gui", "true");
            launchGui();
        }
    }

    private static void handleOptions(OptionSet options)
    {
        if (options.has("mcp"))
        {
            File mcp = options.hasArgument("mcp") ? (File) options.valueOf("mcp") : new File(".");

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
        InstallerPanel panel = new InstallerPanel(new File(System.getProperty("user.dir")));
        panel.run();
    }
}
package com.big_Xplosion.blazeInstaller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.big_Xplosion.blazeInstaller.util.InstallType;

public class BlazeInstaller
{
	public static void main(String[] args)
	{
		OptionParser parser = new OptionParser();
		parser.acceptsAll(Arrays.asList("?", "h", "help"), "Show the help");
		ArgumentAcceptingOptionSpec<File> clientSpec = parser.acceptsAll(Arrays.asList("c", "client"), "Install BL on the client").withRequiredArg().ofType(File.class);
		ArgumentAcceptingOptionSpec<File> mcpSpec = parser.acceptsAll(Arrays.asList("m", "mcp"), "Install BL in a MCP evironment").withRequiredArg().ofType(File.class);

		OptionSet options = parser.parse(args);

		handleOptions(parser, options, clientSpec, mcpSpec);
	}

	private static void handleOptions(OptionParser parser, OptionSet options, ArgumentAcceptingOptionSpec<File> clientSpec, ArgumentAcceptingOptionSpec<File> mcpSpec)
	{
		if (options.has(mcpSpec))
		{
			File file = mcpSpec.value(options);

			try
			{
				InstallType.MCP.install(file);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
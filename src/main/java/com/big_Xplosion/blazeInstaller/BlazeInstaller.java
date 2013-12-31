package com.big_Xplosion.blazeInstaller;

import java.util.Arrays;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class BlazeInstaller
{
	public static void main(String[] args)
	{
		OptionParser parser = new OptionParser();
		parser.acceptsAll(Arrays.asList("?", "h", "help"), "Show the help");
		parser.acceptsAll(Arrays.asList("c", "client"), "Install BL on the client");
		parser.acceptsAll(Arrays.asList("m", "mcp"), "Install BL in a MCP evironment");

		OptionSet options = parser.parse(args);

		handleOption(parser, options);
	}

	private static void handleOption(OptionParser parser, OptionSet options)
	{

	}
}
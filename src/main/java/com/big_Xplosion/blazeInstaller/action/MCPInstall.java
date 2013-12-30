package com.big_Xplosion.blazeInstaller.action;

import java.io.File;

public class MCPInstall implements IInstallerAction
{
	@Override
	public boolean install(File mcpTarget)
	{
		if (isMCPInstalled(mcpTarget))
			System.out.println(String.format("MCP is already installed in %s, skipping", mcpTarget));
		else if (!downloadMCP(mcpTarget))
		{
			System.out.println("failed to download MCP, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		System.out.println("Successfully downloaded MCP");

		return false;
	}

	@Override
	public String getSuccesMessage()
	{
		return "Successfully installed BlazeLoader in the MCP environment.";
	}

	public boolean isMCPInstalled(File target)
	{
		if (target.isDirectory())
		{
			File file = new File(target, "/reobfuscate_srg.sh");

			if (file.exists())
				return true;
		}

		return false;
	}

	public boolean downloadMCP(File target)
	{

		return false;
	}
}

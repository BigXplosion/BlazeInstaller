package com.big_Xplosion.blazeInstaller.action;

import java.io.File;
import java.io.IOException;

import com.big_Xplosion.blazeInstaller.lib.LibNames;
import com.big_Xplosion.blazeInstaller.lib.LibURL;
import com.big_Xplosion.blazeInstaller.unresolved.UnresolvedString;
import com.big_Xplosion.blazeInstaller.unresolved.resolve.VersionResolver;
import com.big_Xplosion.blazeInstaller.util.DownloadUtil;
import com.big_Xplosion.blazeInstaller.util.ExtractUtil;

public class MCPInstall implements IInstallerAction
{
	@Override
	public boolean install(File mcpTarget) throws IOException
	{
		if (!mcpTarget.exists())
		{
			mcpTarget.mkdirs();
		}

		if (isMCPInstalled(mcpTarget))
			System.out.println(String.format("MCP is already installed in %s, skipped download and extraction.", mcpTarget));
		else if (isMCPDownloaded(mcpTarget))
		{
			if (!unpackMCPZip(mcpTarget))
				return false;
		}
		else
		{
			if (!downloadMCP(mcpTarget))
				return false;

			if (!unpackMCPZip(mcpTarget))
				return false;
		}

		System.out.println("Successfully downloaded and unpacked MCP");

		return false;
	}

	@Override
	public String getSuccesMessage()
	{
		return "Successfully installed BlazeLoader in the MCP environment.";
	}

	public boolean isMCPDownloaded(File target)
	{
		if (target.isDirectory())
		{
			File zipFile = new File(target, new UnresolvedString(LibNames.MCP_NAME, new VersionResolver()).call() + ".zip");

			if (zipFile.exists())
				return true;
		}

		return false;
	}

	public boolean isMCPInstalled(File target)
	{
		if (target.isDirectory())
		{
			File reobfFile = new File(target, "/reobfuscate_srg.sh");

			if (reobfFile.exists())
				return true;
		}

		return false;
	}

	public boolean downloadMCP(File targetFile)
	{
		String mcpURL = new UnresolvedString(LibURL.MCP_DOWNLOAD_URL, new VersionResolver()).call();

		if (!DownloadUtil.downloadFile("MCP", targetFile, mcpURL))
		{
			System.out.println("Failed to download MCP, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		return true;
	}

	public boolean unpackMCPZip(File mcpTarget)
	{
		System.out.println("Extracting MCP.");
		String zipName = new UnresolvedString(LibNames.MCP_NAME, new VersionResolver()).call() + ".zip";
		File mcpZip = new File(mcpTarget, zipName);

		if (!ExtractUtil.extractZip(mcpZip, mcpTarget))
		{
			System.out.println("Failed to extract MCP zip file, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		mcpZip.delete();

		return true;
	}
}
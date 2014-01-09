package com.big_Xplosion.blazeInstaller.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.big_Xplosion.blazeInstaller.lib.LibNames;
import com.big_Xplosion.blazeInstaller.lib.LibURL;
import com.big_Xplosion.blazeInstaller.unresolved.UnresolvedString;
import com.big_Xplosion.blazeInstaller.unresolved.resolve.VersionResolver;
import com.big_Xplosion.blazeInstaller.util.DownloadUtil;
import com.big_Xplosion.blazeInstaller.util.ExtractUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

public class MCPInstall implements IInstallerAction
{
	@Override
	public boolean install(File mcpTarget) throws IOException
	{
		Files.createParentDirs(mcpTarget);

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

			System.out.println("Successfully downloaded and unpacked MCP");
		}

		if (!downloadBL(mcpTarget))
			return false;

		if (!unpackBLZip(mcpTarget))
			return false;

		System.out.println("SuccessFully donwloaded and unpacked BlazeLoader-src");

		return true;
	}

	@Override
	public boolean isPathValid(File targetFile)
	{
		return true;
	}

	@Override
	public String getSuccessMessage()
	{
		return "Successfully installed BlazeLoader in the MCP environment.";
	}

	@Override
	public String getFileErrorMessage(File targetFile)
	{
		return "there is no directory here please specify another directory";
	}

	public boolean isMCPDownloaded(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File zipFile = new File(targetFile, new UnresolvedString(LibNames.MCP_NAME, new VersionResolver()).call() + ".zip");

			if (zipFile.exists())
				return true;
		}

		return false;
	}

	public boolean isMCPInstalled(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File reobfFile = new File(targetFile, "/reobfuscate_srg.sh");

			if (reobfFile.exists())
				return true;
		}

		return false;
	}

	public boolean isBLDownloaded(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File blZip = new File(targetFile, "BlazeLoader.zip");

			if (!blZip.exists())
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

	public boolean downloadBL(File targetFile) throws IOException
	{
		URL versionURL = new URL(""); // Put URL here
		BufferedReader reader = new BufferedReader(new InputStreamReader(versionURL.openStream()));
		String line;
		String[] parts = new String[3];

		while ((line = reader.readLine()) != null)
		{
			if (line.isEmpty() || line.startsWith("#") || line == null)					//This is just a test case it probably won't be like this.
				continue;

			parts = Iterables.toArray(Splitter.on('|').split(line), String.class);
		}

		if (!DownloadUtil.downloadFile("BlazeLoader-src", new File(targetFile, "BlazeLoader.zip"), parts[2]))
		{
			System.out.println(String.format("Failed to download the BlazeLoader src version: %s from %s, please try again and if this still doesn't work the site may be down or you can contact a dev.", parts[0], parts[2]));
			return false;
		}

		return true;
	}

	public boolean unpackBLZip(File targetFile)
	{
		File blZip = new File(targetFile, "BlazeLoader.zip");

		if (!ExtractUtil.extractZip(blZip, targetFile))
		{
			System.out.println("Failed to extract the BlazeLoader zip, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		blZip.delete();

		return true;
	}
}
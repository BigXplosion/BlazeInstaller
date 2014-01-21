package com.big_Xplosion.blazeInstaller.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

import com.big_Xplosion.blazeInstaller.lib.LibNames;
import com.big_Xplosion.blazeInstaller.lib.LibURL;
import com.big_Xplosion.blazeInstaller.unresolved.UnresolvedString;
import com.big_Xplosion.blazeInstaller.unresolved.resolve.VersionResolver;
import com.big_Xplosion.blazeInstaller.util.DownloadUtil;
import com.big_Xplosion.blazeInstaller.util.ExtractUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class MCPInstall implements IInstallerAction
{
	private VersionResolver versionResolver;
	private String mcVersion;
	private File blRoot;

	@Override
	public boolean install(File mcpTarget) throws IOException
	{
		Files.createParentDirs(mcpTarget);
		blRoot = new File(mcpTarget, "BlazeLoader");
		Files.createParentDirs(blRoot);

		if (isBLInstalled(mcpTarget))
			System.out.println("BlazeLoader is already installed, skipping donwloading and exctracting.");
		else if (isBLDownloaded(mcpTarget))
		{
			System.out.println("BlazeLoader is already donwloaded, skipping.");

			if (!unpackBLZip(mcpTarget))
				return false;
		}
		else
		{
			if (!downloadBL(mcpTarget))
				return false;

			if (!unpackBLZip(mcpTarget))
				return false;

			System.out.println("SuccessFully donwloaded and unpacked BlazeLoader-src");
		}

		versionResolver = new VersionResolver(new File(blRoot, "BLVersion.properties"));

		if (isMCPInstalled(mcpTarget))
			System.out.println(String.format("MCP is already installed, skipped download and extraction.", mcpTarget));
		else if (isMCPDownloaded(mcpTarget))
		{
			System.out.println("MCP is already donwloaded, skipping.");

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

		mcVersion = new UnresolvedString("{MC_VERSION}", versionResolver).call();
		File jarsDir = new File(mcpTarget, "jars");
		File libDir = new File(jarsDir, "libraries");
		File versionDir = new File(new File(jarsDir, "versions"), mcVersion);
		File devJson = new File(new File(blRoot, "json"), mcVersion + "-dev.json");
		Files.createParentDirs(libDir);
		Files.createParentDirs(versionDir);

		downloadLibraries(libDir, devJson);

		if (!checkVersionFiles(versionDir))
			return false;

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

	private boolean isMCPDownloaded(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File zipFile = new File(targetFile, new UnresolvedString(LibNames.MCP_NAME, versionResolver).call() + ".zip");

			if (zipFile.exists())
				return true;
		}

		return false;
	}

	private boolean isMCPInstalled(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File reobfFile = new File(targetFile, "reobfuscate_srg.sh");

			if (reobfFile.exists())
				return true;
		}

		return false;
	}

	private boolean isBLDownloaded(File targetFile)
	{
		if (targetFile.isDirectory())
		{
			File blZip = new File(targetFile, "BlazeLoader.zip");

			if (blZip.exists())
				return true;
		}

		return false;
	}

	private boolean isBLInstalled(File targetFile)
	{
		if (targetFile.exists())
		{
			File blDir = new File(blRoot, "BLVersion.properties");

			if (blDir.exists())
				return true;
		}

		return false;
	}

	private boolean downloadMCP(File targetFile)
	{
		String mcpURL = new UnresolvedString(LibURL.MCP_DOWNLOAD_URL, versionResolver).call();

		if (!DownloadUtil.downloadFile("MCP", targetFile, mcpURL, false))
		{
			System.out.println("Failed to download MCP, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		return true;
	}

	private boolean unpackMCPZip(File mcpTarget)
	{
		System.out.println("Extracting MCP.");
		String zipName = new UnresolvedString(LibNames.MCP_NAME, versionResolver).call() + ".zip";
		File mcpZip = new File(mcpTarget, zipName);

		if (!ExtractUtil.extractZip(mcpZip, mcpTarget))
		{
			System.out.println("Failed to extract MCP zip file, please try again and if it still doesn't work contact a dev.");
			return false;
		}

		mcpZip.delete();

		return true;
	}

	private boolean downloadBL(File targetFile) throws IOException
	{
		// TODO: change when we have a proper way of doing it.

		URL versionURL = new URL(""); // Put URL here
		BufferedReader reader = new BufferedReader(new InputStreamReader(versionURL.openStream()));
		String line;
		String[] parts = new String[3];

		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("#") || Strings.isNullOrEmpty(line))
				continue;

			parts = Iterables.toArray(Splitter.on('|').split(line), String.class);

			break;
		}

		reader.close();

		if (!DownloadUtil.downloadFile("BlazeLoader-src", new File(targetFile, "BlazeLoader.zip"), parts[2], false))
		{
			System.out.println(String.format("Failed to download the BlazeLoader src version: %s from %s, please try again and if this still doesn't work the site may be down or you can contact a dev.", parts[0], parts[2]));
			return false;
		}

		return true;
	}

	private boolean unpackBLZip(File targetFile)
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

	private boolean checkVersionFiles(File versionTarget)
	{
		String mcJar = mcVersion + ".jar";
		String mcJson = mcVersion + ".json";
		File jarFile = new File(versionTarget, mcJar);
		File jsonFile = new File(versionTarget, mcJson);

		if (!jarFile.exists())
		{
			if (!DownloadUtil.downloadFile(mcJar, jarFile, new UnresolvedString(LibURL.MC_DOWNLOAD_JAR_URL, versionResolver).call(), true))
			{
				System.out.println(String.format("Failed donwloading the minecraft %s, please try again and if it still doesn't work contact a dev.", mcJar));
				return false;
			}
		}

		if (!jsonFile.exists())
		{
			if (!DownloadUtil.downloadFile(mcJson, jsonFile, new UnresolvedString(LibURL.MC_JSON_FILE_URL, versionResolver).call(), true))
			{
				System.out.println(String.format("Failed donwloading the minecraft %s, please try again and if it still doesn't work contact a dev.", mcJson));
				return false;
			}
		}

		return true;
	}

	private void downloadLibraries(File libTarget, File devJson)
	{
		JdomParser parser = new JdomParser();
		List<JsonNode> libraries = null;
		List<JsonNode> failed = Lists.newArrayList();

		try
		{
			JsonRootNode data = parser.parse(new FileReader(devJson));
			libraries = data.getArrayNode("libraries");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		DownloadUtil.downLoadLibraries(InstallType.MCP, libTarget, libraries, failed);

		if (failed.size() > 0)
		{
			List<JsonNode> problems = Lists.newArrayList();

			for (JsonNode fail : failed)
			{
				if (!fail.isBooleanValue("mcpdownload") || fail.getBooleanValue("mcpdownload"))
					continue;
				else
					problems.add(fail);
			}

			if (problems.size() > 0)
				JOptionPane.showMessageDialog(null, String.format("failed to download %s. These files aren't donwloaded by MCP, you could try again or install them manually.", Joiner.on(", ").join(problems)), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
}
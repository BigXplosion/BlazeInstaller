package com.big_Xplosion.blazeInstaller.util;

import argo.jdom.JsonNode;
import com.big_Xplosion.blazeInstaller.action.InstallType;
import com.big_Xplosion.blazeInstaller.lib.LibURL;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadUtil
{
	public static void downLoadLibraries(InstallType type, File libDir, List<JsonNode> libs)
	{
		for (JsonNode lib : libs)
		{
			String name = lib.getStringValue("name");
			String[] parts = Iterables.toArray(Splitter.on(':').split(name), String.class);

			parts[0] = parts[0].replace('.', '/');
			String jarName = parts[1] + "-" + parts[2] + ".jar";
			String path = parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + jarName;

			File libPath = new File(libDir, path.replace('/', File.separatorChar));

			if (isLibInstalled(libPath))
				continue;

			if (!libPath.getParentFile().exists() && !libPath.getParentFile().mkdirs())
				System.out.println("Unable to create or find libraries path!");

			String libURL = LibURL.MC_DOWNLOAD_LIB_ROOT_URL;
			libURL += path;

			if (!downloadFile(name, libPath, libURL))
				if (type.equals(InstallType.CLIENT) && libURL.startsWith(LibURL.MC_DOWNLOAD_LIB_ROOT_URL))
					System.out.println(String.format("failed to download %s, minecraft launcher will download these on the next run.", name));
				else if (type.equals(InstallType.MCP) && libURL.startsWith(LibURL.MC_DOWNLOAD_LIB_ROOT_URL))
					System.out.println(String.format("failed to download %s, mcp will download these later.", name));
				else
					System.out.println(String.format("failed to download %s, try again and if it still fails try contacting an author.", name));
			else
				System.out.println(String.format("Donwloaded library: %s", name));
		}
	}

	public static boolean isLibInstalled(File libJar)
	{
		if (libJar.exists())
			return true;

		return false;
	}

	public static boolean downloadFile(String name, File path, String downloadUrl)
	{
		System.out.println(String.format("Downloading package: %s", name));

		try
		{
			URL url = new URL(downloadUrl);
			final URLConnection connection = url.openConnection();
			connection.setConnectTimeout(6000);
			connection.setReadTimeout(6000);

			InputSupplier<InputStream> urlSupplier = new InputSupplier<InputStream>()
			{
				@Override
				public InputStream getInput() throws IOException
				{
					return connection.getInputStream();
				}
			};

			Files.copy(urlSupplier, new File(path, downloadUrl.substring(downloadUrl.lastIndexOf("/"), downloadUrl.length())));

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}
	}
}
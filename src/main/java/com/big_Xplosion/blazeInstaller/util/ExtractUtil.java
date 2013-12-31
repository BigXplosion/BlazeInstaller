package com.big_Xplosion.blazeInstaller.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class ExtractUtil
{
	public static boolean extractZip(File zip, File outputFile)
	{
		try
		{
			ZipFile zipFile = new ZipFile(zip);

			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
			{
				ZipEntry entry = e.nextElement();
				final InputStream inStream = zipFile.getInputStream(entry);

				InputSupplier<InputStream> supplier = new InputSupplier<InputStream>()
				{
					@Override
					public InputStream getInput() throws IOException
					{
						return inStream;
					}
				};

				Files.copy(supplier, outputFile);
			}

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}
	}
}
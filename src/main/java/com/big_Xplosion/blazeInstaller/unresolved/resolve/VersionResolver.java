package com.big_Xplosion.blazeInstaller.unresolved.resolve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class VersionResolver implements IResolver
{
    private File versionFile;
    private HashMap<String, String> versions;

    public VersionResolver(File versionFile)
    {
        this.versionFile = versionFile;
        this.versions = readVersionFile();
    }

    @Override
    public String resolve(String input)
    {
        input = input.replace("{MCP_VERSION}", versions.get("mcp"));
        input = input.replace("{MC_VERSION}", versions.get("mc"));
        input = input.replace("{FORGE_VERSION}", versions.get("forge"));

        return input;
    }

    private HashMap<String, String> readVersionFile()
    {
        HashMap<String, String> versions = Maps.newHashMap();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(this.versionFile));
            String line;

            while ((line = reader.readLine()) != null)
            {
                if (Strings.isNullOrEmpty(line) || line.startsWith("#"))
                    continue;

                String[] parts = Iterables.toArray(Splitter.on('=').limit(2).split(line), String.class);
                versions.put(parts[0].toLowerCase(), parts[1]);
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return versions;
    }
}
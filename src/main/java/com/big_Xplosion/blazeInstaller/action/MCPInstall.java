package com.big_Xplosion.blazeInstaller.action;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import com.big_Xplosion.blazeInstaller.lib.LibNames;
import com.big_Xplosion.blazeInstaller.lib.LibURL;
import com.big_Xplosion.blazeInstaller.unresolved.UnresolvedString;
import com.big_Xplosion.blazeInstaller.unresolved.resolve.VersionResolver;
import com.big_Xplosion.blazeInstaller.util.DownloadUtil;
import com.big_Xplosion.blazeInstaller.util.ExecutionUtil;
import com.big_Xplosion.blazeInstaller.util.ExtractUtil;
import com.big_Xplosion.blazeInstaller.util.FileUtil;
import com.big_Xplosion.blazeInstaller.util.OS;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.big_Xplosion.blazeInstaller.util.MessageUtil.postErrorMessage;

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
            System.out.println("> BlazeLoader is already installed, skipping donwloading and exctracting.");
        else if (isBLDownloaded(mcpTarget))
        {
            System.out.println("> BlazeLoader is already donwloaded, skipping.");

            if (!unpackBLZip(mcpTarget))
                return false;
        }
        else
        {
            if (!downloadBL(mcpTarget))
                return false;

            if (!unpackBLZip(mcpTarget))
                return false;

            System.out.println("> SuccessFully donwloaded and unpacked BlazeLoader-src");
        }

        versionResolver = new VersionResolver(new File(blRoot, "BLVersion.properties"));

        if (isMCPInstalled(mcpTarget))
            System.out.println(String.format("> MCP is already installed, skipped download and extraction.", mcpTarget));
        else if (isMCPDownloaded(mcpTarget))
        {
            System.out.println("> MCP is already donwloaded, skipping.");

            if (!unpackMCPZip(mcpTarget))
                return false;
        }
        else
        {
            //TODO: change back on stable MCP release!
            //if (!downloadMCP(mcpTarget))
            //return false;

            //if (!unpackMCPZip(mcpTarget))
            //return false;

            //System.out.println("> Successfully downloaded and unpacked MCP");

            postErrorMessage("MCP can't be downloaded automaticly, please download the MCP903 manually");
            return false;
        }

        mcVersion = new UnresolvedString("{MC_VERSION}", versionResolver).call();
        File jarsDir = new File(mcpTarget, "jars");
        File libDir = new File(jarsDir, "libraries");
        File versionDir = new File(new File(jarsDir, "versions"), mcVersion);
        File devJson = new File(new File(blRoot, "json"), String.format("BL-%s-dev.json", mcVersion));
        Files.createParentDirs(libDir);
        Files.createParentDirs(versionDir);

        downloadLibraries(libDir, devJson);

        if (!checkVersionFiles(versionDir))
            return false;

        if (!applyAT(mcpTarget))
            return false;

        if (!decompile(mcpTarget))
            return false;

        if (!copySources(mcpTarget))
            return false;

        if (!finalizeInstall(mcpTarget))
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
            File zipFile = new File(targetFile,/* new UnresolvedString(LibNames.MCP_NAME, versionResolver).call() + */"mcp903.zip"); //TODO: change back to original on stable MCP release!

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
            postErrorMessage("Failed to download MCP, please try again and if it still doesn't work contact a dev.");
            return false;
        }

        return true;
    }

    private boolean unpackMCPZip(File mcpTarget)
    {
        System.out.println("> Extracting MCP.");
        @SuppressWarnings("unused")
        String zipName = new UnresolvedString(LibNames.MCP_NAME, versionResolver).call() + ".zip"; //TODO: change to real name when there is a stable release of MCP!
        File mcpZip = new File(mcpTarget, "mcp903.zip");

        if (!ExtractUtil.extractZip(mcpZip, mcpTarget))
        {
            postErrorMessage("Failed to extract MCP zip file, please try again and if it still doesn't work contact a dev.");
            return false;
        }

        mcpZip.delete();

        return true;
    }

    private boolean downloadBL(File targetFile) throws IOException
    {
        // TODO: change when we have a proper way of doing it.
        //acomputerdog: Why should we make a proper way when we can make github do all the work? :)
        /*
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
            System.out.println(String.format("Failed to download the BlazeLoader src version: %s from %s, please try again and if this still doesn't work the site may be down or you can contact a dev.", parts[1], parts[2]));

        */
        if (!DownloadUtil.downloadFile("BlazeLoader-src", new File(targetFile, "BlazeLoader.zip"), "https://github.com/warriordog/BlazeLoader/archive/master.zip", true))
        {
            postErrorMessage(String.format("Failed to download the BlazeLoader src from %s, please try again and if this still doesn't work the site may be down or you can contact a dev.", "http://github.com/warriordog/BlazeLoader/archive/master.zip"));
            return false;
        }

        return true;
    }

    private boolean unpackBLZip(File targetFile) throws IOException
    {
        System.out.println("> Extracting BlazeLoader");
        File blZip = new File(targetFile, "BlazeLoader.zip");

        if (!ExtractUtil.extractZip(blZip, targetFile))
        {
            postErrorMessage("Failed to extract the BlazeLoader zip, please try again and if it still doesn't work contact a dev.");
            return false;
        }

        blZip.delete();

        File blMaster = new File(targetFile, "BlazeLoader-master");
        File blDir = new File(targetFile, "BlazeLoader");
        Files.move(blMaster, blDir);

        return true;
    }

    private boolean checkVersionFiles(File versionTarget) throws IOException
    {
        String mcJar = mcVersion + ".jar";
        String mcJson = mcVersion + ".json";
        File mcVersionFile = new File(new File(OS.getMinecraftDir(), "versions"), mcVersion);
        File mcJarFile = new File(mcVersionFile, mcJar);
        File mcJsonFile = new File(mcVersionFile, mcJson);
        File mcpJarFile = new File(versionTarget, mcJar);
        File jsonFile = new File(versionTarget, mcJson);
        Files.createParentDirs(mcpJarFile);
        Files.createParentDirs(jsonFile);

        if (!mcpJarFile.exists())
        {
            if (mcJarFile.exists())
            {
                System.out.println("> the MC jar already exists in the Minecraft evironment, copying");
                Files.copy(mcJarFile, mcpJarFile);
            }
            else if (!DownloadUtil.downloadFile(mcJar, mcpJarFile, new UnresolvedString(LibURL.MC_DOWNLOAD_JAR_URL, versionResolver).call(), true))
            {
                postErrorMessage(String.format("Failed donwloading the minecraft %s, please try again and if it still doesn't work contact a dev.", mcJar));
                return false;
            }
        }

        if (!jsonFile.exists())
        {
            if (mcJsonFile.exists())
            {
                System.out.println("> the MC jar already exists in the Minecraft evironment, copying");
                Files.copy(mcJsonFile, jsonFile);
            }
            else if (!DownloadUtil.downloadFile(mcJson, jsonFile, new UnresolvedString(LibURL.MC_JSON_FILE_URL, versionResolver).call(), true))
            {
                postErrorMessage(String.format("Failed donwloading the minecraft %s, please try again and if it still doesn't work contact a dev.", mcJson));
                return false;
            }
        }

        return true;
    }

    private void downloadLibraries(File libTarget, File devJson) throws IOException
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
                postErrorMessage(String.format("failed to download %s. These files aren't donwloaded by MCP, you could try again or install them manually.", Joiner.on(", \n").join(problems)));
        }
    }

    private boolean applyAT(File mcpTarget)
    {
        File bin = new File(blRoot, "bin");
        bin.mkdir();
        System.out.println("> Compiling AccessTransformer");

        if (!ExecutionUtil.compileJava(mcpTarget, "/BlazeLoader/source/core/net/acomputerdog/BlazeLoader/launcher/BLAccessTransformer.java", "/BlazeLoader/bin", "/jars/libraries/net/minecraft/launchwrapper/1.9/launchwrapper-1.9.jar", "/jars/libraries/org/ow2/asm/asm-debug-all/4.1/asm-debug-all-4.1.jar", "/BlazeLoader/bl_at.cfg"))
        {
            postErrorMessage("Failed to compile the AccessTransformer, are you sure you have the java JDK installed and your path is setup correctly.");
            return false;
        }

        System.out.println("> Running AccessTransformer");

        if (!ExecutionUtil.runJava(mcpTarget, "net/acomputerdog/BlazeLoader/launcher/BLAccessTransformer", String.format("%s/jars/versions/%s/%s.jar %s/BlazeLoader/bl_at.cfg", mcpTarget.getAbsolutePath(), mcVersion, mcVersion, mcpTarget.getAbsolutePath()), "/BlazeLoader/bin", "/jars/libraries/net/minecraft/launchwrapper/1.9/launchwrapper-1.9.jar", "/jars/libraries/org/ow2/asm/asm-debug-all/4.1/asm-debug-all-4.1.jar"))
        {
            postErrorMessage("Failed to run the AccessTransformer, are you sure your java is installed right.");
            return false;
        }

        return true;
    }

    private boolean decompile(File mcpTarget)
    {
        System.out.println("> Decompiling minecraft");
        ExecutionUtil.runShellOrBat(mcpTarget, "decompile");

        return true;
    }

    private boolean copySources(File mcpTarget) throws IOException
    {
        File source = new File(blRoot, "source");
        File mcSources = new File(new File(mcpTarget, "src"), "minecraft");

        System.out.println("> Copying and overwriting edited MC sources");
        FileUtil.copyAndOverwriteSources(new File(source, "vanilla"), new File(new File(mcpTarget, "src"), "minecraft"));

        System.out.println("> Copying BlazeLoader sources");
        FileUtil.copySources(new File(source, "core"), mcSources);

        return true;
    }

    private boolean finalizeInstall(File mcpTarget)
    {
        boolean dev = Boolean.parseBoolean(System.getProperty("bli.bldev"));

        if (dev)
        {
            System.out.println("> BL dev environment, recompiling");
            ExecutionUtil.runShellOrBat(mcpTarget, "recompile");
        }
        else
        {
            System.out.println("> Normal dev environment, updating MD5s");
            ExecutionUtil.runShellOrBat(mcpTarget, "updatemd5");
        }

        return true;
    }
}
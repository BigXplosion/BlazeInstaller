package com.big_Xplosion.blazeInstaller.lib;

public class LibURL
{
    public static final String MC_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/";

    public static final String MC_DOWNLOAD_ROOT_VERSION_URL = MC_DOWNLOAD_URL + "versions/";
    public static final String MC_DOWNLOAD_VERSION_URL = MC_DOWNLOAD_ROOT_VERSION_URL + "{MC_VERSION}/";

    public static final String MC_INDEX_URL = MC_DOWNLOAD_URL + "indexes/";

    public static final String MC_DOWNLOAD_LIB_ROOT_URL = "https://libraries.minecraft.net/";

    public static final String MC_DOWNLOAD_JAR_URL = MC_DOWNLOAD_VERSION_URL + "{MC_VERSION}.jar";

    public static final String MCP_DOWNLOAD_URL = "http://mcp.ocean-labs.de/files/archive/mcp{MCP_VERSION}.zip";

    public static final String FORGE_DOWNLOAD_ROOT_URL = "http://files.minecraftforge.net";
    public static final String FORGE_MAVEN_URL = FORGE_DOWNLOAD_ROOT_URL + "/maven";

    public static final String FORGE_SRC_URL = FORGE_MAVEN_URL + "/net/minecraftforge/forge/{MC_VERSION}-{FORGE-VERSION}/forge-{MC_VERSION}-{FORGE-VERSION}-src.zip";
}
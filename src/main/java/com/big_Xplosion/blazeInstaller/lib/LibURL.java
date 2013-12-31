package com.big_Xplosion.blazeInstaller.lib;

public class LibURL
{
	public static final String MC_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/";

	public static final String MC_DOWNLOAD_ROOT_VERSION_URL = MC_DOWNLOAD_URL + "versions/";
	public static final String MC_DOWNLOAD_VERSION_URL = MC_DOWNLOAD_ROOT_VERSION_URL + "{MC_VERSION}/";

	public static final String MC_INDEX_URL = MC_DOWNLOAD_URL + "indexes/";

	public static final String MC_DOWNLOAD_LIB_ROOT_URL = "https://libraries.minecraft.net/";

	public static final String MC_DOWNLOAD_JAR_URL = MC_DOWNLOAD_VERSION_URL + "{MC_VERSION}.jar";
	public static final String MC_DOWNLOAD_SERVER_JAR_URL = MC_DOWNLOAD_VERSION_URL + "minecraft_server.{MC_VERSION}.jar";

	public static final String MC_ASSET_INDEX_URL = MC_INDEX_URL + "{ASSET_INDEX}.json";

	public static final String MCP_DOWNLOAD_URL = "http://mcp.ocean-labs.de/files/archive/mcp{MCP_VERSION}.zip";
}
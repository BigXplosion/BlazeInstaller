package com.big_Xplosion.blazeInstaller.unresolved.resolve;

import com.big_Xplosion.blazeInstaller.unresolved.IResolver;

public class VersionResolver implements IResolver
{
	@Override
	public String resolve(String input)
	{
		input = input.replace("{MCP_VERION}", "804"); // TODO: get better way to get versions, still 804 so I can test it more easy.

		return input;
	}
}
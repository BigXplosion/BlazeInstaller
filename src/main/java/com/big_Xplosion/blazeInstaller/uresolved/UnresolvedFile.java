package com.big_xplosion.blazebuilder.unresolved;

import java.io.File;

public class UnresolvedFile extends UnresolvedBase<File>
{
	public UnresolvedFile(String input, IResolver... resolvers)
	{
		super(input, resolvers);
	}

	@Override
	public File call()
	{
		if (resolved == null)
			resolved = new File(this.resolve());

		return resolved;
	}
}
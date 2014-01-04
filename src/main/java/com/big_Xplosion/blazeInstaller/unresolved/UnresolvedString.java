package com.big_Xplosion.blazeInstaller.unresolved;

import com.big_Xplosion.blazeInstaller.unresolved.resolve.IResolver;

public class UnresolvedString extends UnresolvedBase<String>
{
	public UnresolvedString(String input, IResolver... resolvers)
	{
		super(input, resolvers);
	}

	@Override
	public String call()
	{
		if (resolved == null)
			resolved = this.resolve();

		return this.resolved;
	}
}
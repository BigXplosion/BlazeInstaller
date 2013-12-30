package com.big_xplosion.blazebuilder.unresolved;

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
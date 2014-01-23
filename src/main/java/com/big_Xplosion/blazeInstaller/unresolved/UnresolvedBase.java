package com.big_Xplosion.blazeInstaller.unresolved;

import com.big_Xplosion.blazeInstaller.unresolved.resolve.IResolver;

public abstract class UnresolvedBase<V>
{
    private String input;
    private IResolver[] resolvers;
    protected V resolved;

    public UnresolvedBase(String input, IResolver... resolvers)
    {
        this.input = input;
        this.resolvers = resolvers;
    }

    public abstract V call();

    public String resolve()
    {
        for (IResolver resolver : resolvers)
            input = resolver.resolve(input);

        return input;
    }
}
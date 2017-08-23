package com.milancrnjak.sel.function.impl;

import com.milancrnjak.sel.function.Function;

import java.util.List;

/**
 * Power function (Math.pow).
 *
 * @author Milan Crnjak
 */
public class PowFunction implements Function {

    @Override
    public Object execute(List<Object> args) {
        if (args == null || args.size() != 2) {
            throw new IllegalArgumentException("Pow function requires two arguments");
        }

        Number a = (Number) args.get(0);
        Number b = (Number) args.get(1);

        return Math.pow(a.doubleValue(), b.doubleValue());
    }
}

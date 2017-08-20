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

        Double a = (Double) args.get(0);
        Double b = (Double) args.get(1);

        return Math.pow(a, b);
    }
}

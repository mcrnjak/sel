package mc.sel.function.impl;

import mc.sel.function.Function;
import mc.sel.identifier.context.Context;

import java.util.List;

/**
 * Power function (Math.pow).
 *
 * @author Milan Crnjak
 */
public class PowFunction implements Function {

    @Override
    public Object execute(Context context, List<Object> args) {
        if (args == null || args.size() != 2) {
            throw new IllegalArgumentException("Pow function requires two arguments");
        }

        Number a = (Number) args.get(0);
        Number b = (Number) args.get(1);

        return Math.pow(a.doubleValue(), b.doubleValue());
    }
}

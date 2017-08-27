package mc.sel.function.impl;

import mc.sel.function.Function;
import mc.sel.identifier.context.Context;

import java.util.List;

public class ListFunction implements Function {

    @Override
    public Object execute(Context context, List<Object> args) {
        return args;
    }
}

package mc.sel.function.impl;

import mc.sel.function.Function;

import java.util.List;

public class ListFunction implements Function {

    @Override
    public Object execute(List<Object> args) {
        return args;
    }
}

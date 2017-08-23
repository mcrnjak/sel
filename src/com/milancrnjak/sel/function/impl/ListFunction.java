package com.milancrnjak.sel.function.impl;

import com.milancrnjak.sel.function.Function;

import java.util.List;

public class ListFunction implements Function {

    @Override
    public Object execute(List<Object> args) {
        return args;
    }
}

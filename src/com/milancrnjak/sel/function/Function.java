package com.milancrnjak.sel.function;

import java.util.List;

/**
 * Expression function interface.
 *
 * @author Milan Crnjak
 */
public interface Function {

    /**
     * Executes functions logic.
     *
     * @param args function arguments.
     *
     * @return Function result.
     */
    Object execute(List<Object> args);
}

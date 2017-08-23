package com.milancrnjak.sel.function;

import com.milancrnjak.sel.function.impl.DateFunction;
import com.milancrnjak.sel.function.impl.ListFunction;
import com.milancrnjak.sel.function.impl.PowFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions registry. All available functions are registered here. Custom defined functions
 * must be registered using {@link FunctionsRegistry#registerFunction(String, Function)} method.
 *
 * @author Milan Crnjak
 */
public class FunctionsRegistry {

    private static final Map<String, Function> functions = new HashMap<>();

    static {
        registerFunction("pow", new PowFunction());
        registerFunction("date", new DateFunction());
        registerFunction("list", new ListFunction());
    }

    private FunctionsRegistry() {}

    /**
     * Returns the names of the currently registered functions.
     *
     * @return Collection of registered functions' names.
     */
    public static synchronized Collection<String> getRegisteredFunctions() {
        return functions.keySet();
    }

    /**
     * Registers a functions in the registry.
     *
     * @param name function name.
     * @param function function implementation.
     */
    public static synchronized void registerFunction(String name, Function function) {
        functions.put(name, function);
    }

    /**
     * Retrives a function from the registry.
     *
     * @param name function name
     *
     * @return Function object
     */
    public static synchronized Function getFunction(String name) {
        return functions.get(name);
    }
}

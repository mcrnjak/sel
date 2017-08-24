package mc.sel.function.impl;

import mc.sel.function.Function;

import java.util.List;

/**
 * Returns a {@link Class} instance for the given class name.
 * Useful for invoking static methods.
 *
 * <pre>
 *     class('java.lang.Math').pow(2.0, 3.0)
 * </pre>
 *
 * @author Milan Crnjak
 */
public class ClassFunction implements Function {

    @Override
    public Object execute(List<Object> args) {
        try {
            return Class.forName((String) args.get(0));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

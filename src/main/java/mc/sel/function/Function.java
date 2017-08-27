package mc.sel.function;

import mc.sel.identifier.context.Context;

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
     * @param context expression context
     * @param args function arguments.
     *
     * @return Function result.
     */
    Object execute(Context context, List<Object> args);
}

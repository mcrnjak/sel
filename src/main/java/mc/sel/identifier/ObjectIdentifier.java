package mc.sel.identifier;

import mc.sel.identifier.context.Context;
import mc.sel.identifier.context.ContextObject;

/**
 * Object identifier interface.
 *
 * @author Milan Crnjak
 */
public interface ObjectIdentifier {

    /**
     * Executes object identifier logic.
     *
     * @param ctx Context
     * @return Evaluated context object
     */
    ContextObject execute(Context ctx);
}

package mc.sel.identifier.impl;

import mc.sel.identifier.ObjectIdentifier;
import mc.sel.identifier.context.Context;
import mc.sel.identifier.context.ContextObject;

/**
 * 'This' object identifier.
 *
 * @author Milan Crnjak
 */
public class ThisIdentifier implements ObjectIdentifier {

    @Override
    public ContextObject execute(Context ctx) {
        return ctx.getContextObject();
    }
}

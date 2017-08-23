package com.milancrnjak.sel.identifier.impl;

import com.milancrnjak.sel.identifier.ObjectIdentifier;
import com.milancrnjak.sel.identifier.context.Context;
import com.milancrnjak.sel.identifier.context.ContextObject;

import java.util.ArrayList;
import java.util.Arrays;

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

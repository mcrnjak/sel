package com.milancrnjak.sel.function;

import com.milancrnjak.sel.function.impl.ThisIdentifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectsRegistry {

    private static final Map<String, ObjectIdentifier> objectIdentifiers = new HashMap<>();

    static {
        registerObjectIdentifier("this", new ThisIdentifier());
    }

    public static synchronized void registerObjectIdentifier(String name, ObjectIdentifier objectIdentifier) {
        objectIdentifiers.put(name, objectIdentifier);
    }

    public static synchronized Collection<String> getRegisteredObjects() {
        return objectIdentifiers.keySet();
    }

}

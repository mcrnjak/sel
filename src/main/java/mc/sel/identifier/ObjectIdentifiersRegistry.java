package mc.sel.identifier;

import mc.sel.identifier.impl.ThisIdentifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Object identifiers registry. All available identifiers are registered here. Custom defined identifiers
 * must be registered using {@link ObjectIdentifiersRegistry#registerObjectIdentifier(String, ObjectIdentifier)} method.
 *
 * @author Milan Crnjak
 */
public final class ObjectIdentifiersRegistry {

    private static final Map<String, ObjectIdentifier> objectIdentifiers = new HashMap<>();

    static {
        registerObjectIdentifier("this", new ThisIdentifier());
    }

    private ObjectIdentifiersRegistry() {}

    /**
     * Registers an object identifier in the registry.
     *
     * @param name Object identifier name
     * @param objectIdentifier Object identifier
     */
    public static synchronized void registerObjectIdentifier(String name, ObjectIdentifier objectIdentifier) {
        objectIdentifiers.put(name, objectIdentifier);
    }

    /**
     * Retrieves an object identifier from the registry.
     * @param name Object identifier name
     * @return Object identifier
     */
    public static synchronized ObjectIdentifier getObjectIdentifier(String name) {
        return objectIdentifiers.get(name);
    }

    /**
     * Returns the names of the currently registered object identifiers.
     *
     * @return Collection of registered identifiers names.
     */
    public static synchronized Collection<String> getRegisteredObjectIdentifiers() {
        return objectIdentifiers.keySet();
    }

}

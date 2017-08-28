package mc.sel.identifier.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Default expression context implementation.
 *
 * @author Milan Crnjak
 */
public class DefaultContextImpl implements  Context {

    private final ContextObject rootContextObject;
    private ContextObject contextObject;
    private Map<String, Object> properties = new HashMap<>();

    public DefaultContextImpl(ContextObject rootContextObject) {
        this.rootContextObject = rootContextObject;
        setContextObject(rootContextObject);
    }

    @Override
    public ContextObject getContextObject() {
        return contextObject;
    }

    @Override
    public void setContextObject(ContextObject contextObject) {
        this.contextObject = contextObject;
    }

    @Override
    public ContextObject getRootContextObject() {
        return rootContextObject;
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public void setProperty(String name, Object property) {
        properties.put(name, property);
    }
}

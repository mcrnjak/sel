package mc.sel.identifier.context;

/**
 * Expression context.
 *
 * @author Milan Crnjak
 */
public interface Context {

    ContextObject getContextObject();

    void setContextObject(ContextObject contextObject);

    ContextObject getRootContextObject();

    Object getProperty(String name);

    void setProperty(String name, Object property);
}

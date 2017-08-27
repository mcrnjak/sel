package mc.sel.identifier.context;

/**
 * Expression context.
 *
 * @author Milan Crnjak
 */
public interface Context {

    public ContextObject getContextObject();

    public void setContextObject(ContextObject contextObject);

    Object getProperty(String name);

    void setProperty(String name, Object property);
}

package mc.sel.identifier.context;

/**
 * Expression context object.
 *
 * @author Milan Crnjak
 */
public interface ContextObject {

    Object getProperty(String name);

    void setProperty(String name, Object val);

    Object getObject();
}

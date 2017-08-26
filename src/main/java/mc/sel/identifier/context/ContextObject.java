package mc.sel.identifier.context;

/**
 * Expression context object.
 *
 * @author Milan Crnjak
 */
public interface ContextObject {

    Object getProperty(String name);

    Object getPropertyAtIndex(String name, int index);

    void setProperty(String name, Object val);

    void setPropertyAtIndex(String name, Object val, int index);

    Object getObject();
}

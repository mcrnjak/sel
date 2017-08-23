package mc.sel.identifier.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Expression context object backed by a map.
 *
 * @author Milan Crnjak
 */
public class MapContextObject implements ContextObject {

    private Map<String, Object> map = new HashMap<>();

    public MapContextObject(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public Object getProperty(String name) {
        return map.get(name);
    }

    @Override
    public void setProperty(String name, Object val) {
        map.put(name, val);
    }

    @Override
    public Object getObject() {
        return map;
    }
}

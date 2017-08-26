package mc.sel.identifier.context;

import java.util.HashMap;
import java.util.List;
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
    public Object getPropertyAtIndex(String name, int index) {
        Object propVal = getProperty(name);

        if (propVal instanceof List) {
            List<?> list = (List<?>) propVal;
            return list.get(index);
        } else if (propVal.getClass().isArray()) {
            Object[] array = (Object[]) propVal;
            return array[index];
        } else {
            String msg = String.format("Property '%s' is not a list or array", name);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void setProperty(String name, Object val) {
        map.put(name, val);
    }

    @Override
    public void setPropertyAtIndex(String name, Object val, int index) {
        Object propVal = getProperty(name);

        if (propVal instanceof List) {
            List<? super Object> list = (List<? super Object>) propVal;
            list.set(index, val);
        } else if (propVal.getClass().isArray()) {
            Object[] array = (Object[]) propVal;
            array[index] = val;
        } else {
            String msg = String.format("Property '%s' is not a list or array", name);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public Object getObject() {
        return map;
    }
}

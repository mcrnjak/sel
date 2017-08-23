package mc.sel.util;

/**
 * Holds string utility methods.
 *
 * @author Milan Crnjak
 */
public class StringUtils {

    /**
     * Joins the passed elements using a passed delimiter and produces and new string.
     *
     * @param delimiter delimiter to join elements with
     * @param elements elements to be joined
     * @return Joined elements
     */
    public static String join(String delimiter, Iterable<String> elements) {
        StringBuilder sb = new StringBuilder();

        for (String s : elements) {
            if (sb.length() == 0) {
                sb.append(s);
            } else {
                sb.append(delimiter).append(s);
            }
        }

        return sb.toString();
    }


}

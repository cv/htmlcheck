/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck;

import java.util.Arrays;

import org.jdom.Element;

public class Selector {

    public static Selector from(Element element) {
        StringBuilder selector = new StringBuilder();
        if (element.getParentElement() != null) {
            selector.append(from(element.getParentElement())).append(" > ");
        }

        selector.append(element.getName());

        String id = element.getAttributeValue("id");
        if (id != null) {
            selector.append('#').append(id);
        }

        String[] classes = element.getAttributeValue("class", "").split(" +");
        Arrays.sort(classes);

        for (String clazz : classes) {
            if (!"".equals(clazz)) {
                selector.append(".").append(clazz);
            }
        }

        return new Selector(selector.toString());
    }

    private final String selector;

    private Selector(String selector) {
        this.selector = selector;
    }

    @Override
    public String toString() {
        return selector;
    }
}

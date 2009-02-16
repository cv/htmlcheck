/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoDuplicatedIdAttributesRule implements Rule {

    private final Page page;

    public NoDuplicatedIdAttributesRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Element> allElementsWithId = XPath.selectNodes(page.getRoot(), "//*[@id]");
        Map<String, Element> map = new HashMap<String, Element>();

        for (Element current : allElementsWithId) {
            Element previous = map.get(current.getAttribute("id").getValue());
            if (previous == null) {
                map.put(current.getAttribute("id").getValue(), current);
            } else {
                errors.add(new HtmlCheckError(String.format("DUPLICATED ID: %s has the same id attribute as %s", Selector.from(previous), Selector.from(current))));
            }
        }
    }
}

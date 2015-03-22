/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.Arrays;
import java.util.List;

public class NoInvalidElementsInHeadRule implements Rule {

    private final List<String> allowed = Arrays.asList("meta", "title", "script", "link", "style");

    private final Page page;

    public NoInvalidElementsInHeadRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(page.getRoot(), "/html/head/*");

        for (Element element : elements) {
            if (!allowed.contains(element.getName())) {
                errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: %s is not allowed inside html > head", Selector.from(element))));
            }
        }
    }
}

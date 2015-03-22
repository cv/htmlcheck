/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;
import org.jdom.Attribute;
import org.jdom.xpath.XPath;

import java.util.List;

public class NoInvalidIdAttributesRule implements Rule {

    private final Page page;

    public NoInvalidIdAttributesRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Attribute> ids = XPath.selectNodes(page.getRoot(), "//*/@id");
        for (Attribute id : ids) {
            String value = id.getValue();
            if (!value.matches("[a-z]{1}[a-zA-Z0-9]*")) {
                errors.add(new HtmlCheckError(String.format("INVALID ID: %s has an invalid id: %s", Selector.from(id.getParent()), value)));
            }
        }
    }
}

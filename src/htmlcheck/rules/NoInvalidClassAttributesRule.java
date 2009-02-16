/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoInvalidClassAttributesRule implements Rule {

    private final Page page;

    public NoInvalidClassAttributesRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Attribute> classes = XPath.selectNodes(page.getRoot(), "//*/@class");
        for (Attribute clazzAttr : classes) {
            String value = clazzAttr.getValue();
            for (String clazz : value.split(" +")) {
                if (!"".equals(clazz) && !clazz.matches("[a-z]{1}[a-zA-Z0-9]*")) {
                    errors.add(new HtmlCheckError(String.format("INVALID CLASS: %s has an invalid class: %s", Selector.from(clazzAttr.getParent()), clazz)));
                }
            }
        }
    }
}

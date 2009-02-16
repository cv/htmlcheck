/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

public class NoOrphanLabelsRule implements Rule {

    private final Page page;

    public NoOrphanLabelsRule(Page page) {
        this.page = page;
    }
    
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> labels = XPath.selectNodes(page.getRoot(), "//label[@for]");

        for (Element label : labels) {
            String id = label.getAttributeValue("for");
            Element elem = (Element) XPath.selectSingleNode(page.getRoot(), String.format("//*[@id='%s']", id));
            if(elem == null) {
                errors.add(new HtmlCheckError(String.format("ORPHAN LABEL: %s points to element with id '%s', which doesn't exist in this document", Selector.from(label), id)));
            }
        }
    }

}

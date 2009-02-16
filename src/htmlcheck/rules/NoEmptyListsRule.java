/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoEmptyListsRule implements Rule {

    private final Page page;

    public NoEmptyListsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> emptyLists = XPath.selectNodes(page.getRoot(), "//ul[count(child::*) = 0] | //ol[count(child::*) = 0]");
        for (Element emptyList : emptyLists) {
            errors.add(new HtmlCheckError(String.format("EMPTY LIST: %s cannot be empty", Selector.from(emptyList))));
        }
    }
}

package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoExcessivelyNestedIdsRule implements Rule {

    private final Page page;

    public NoExcessivelyNestedIdsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(page.getRoot(), "//body//*[@id]/*[@id]/*[@id]/*[@id]/*[@id]");

        for (Element element : elements) {
            errors.add(new HtmlCheckError(String.format("ID ABUSE: %s has four or more parents which already have id attributes", Selector.from(element))));
        }
    }
}

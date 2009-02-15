package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.Arrays;
import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoDivsWithSingleBlockLevelChildRule implements Rule {

    private final List<String> BLOCK_LEVEL_ELEMENTS = Arrays.asList("p", "h1", "h2", "h3", "h4", "h5", "h6", "ol", "ul", "pre", "address", "blockquote", "dl", "div", "fieldset", "form", "hr",
            "noscript", "table");

    private final Page page;

    public NoDivsWithSingleBlockLevelChildRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> divsWithSingleChild = XPath.selectNodes(page.getRoot(), "//div[count(child::*) = 1]");
        for (Element divWithSingleChild : divsWithSingleChild) {
            Element child = (Element) divWithSingleChild.getChildren().get(0);
            if (BLOCK_LEVEL_ELEMENTS.contains(child.getName())) {
                errors.add(new HtmlCheckError(String.format("UNNECESSARY DIV: %s contains only one block level element: %s", Selector.from(divWithSingleChild), Selector.from(child))));
            }
        }
    }
}

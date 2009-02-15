package htmlcheck.rules;

import htmlcheck.*;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoRelativeUrlsRule implements Rule {

    private final Page page;

    public NoRelativeUrlsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(page.getRoot(), "//*[@href and not(contains(@href, 'external'))] | //*[@src]");
        
        for (Element element : elements) {
            String val = element.getAttributeValue("href", element.getAttributeValue("src"));
            if(!val.startsWith("http")) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s links to '%s', which is not absolute", Selector.from(element), val)));
            }
        }
    }

}

package htmlcheck.rules;

import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoUnderscoresInUrlsRule implements Rule {

    private final Page page;

    public NoUnderscoresInUrlsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src | //*/@href");

        for (Attribute link : links) {
            if (link.getValue().contains("_")) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains link with underscore in the URL: %s", Selector.from(link.getParent()), link.getValue())));
            }
        }
    }
}

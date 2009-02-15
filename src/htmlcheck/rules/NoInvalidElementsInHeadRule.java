package htmlcheck.rules;

import htmlcheck.*;

import java.util.Arrays;
import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoInvalidElementsInHeadRule implements Rule {

    private final Page page;

    public NoInvalidElementsInHeadRule(Page page) {
        this.page = page;
    }

    private final List<String> allowed = Arrays.asList("meta", "title", "script", "link", "style");

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(this.page.getRoot(), "/html/head/*");

        for (Element element : elements) {
            if (!allowed.contains(element.getName())) {
                errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: %s is not allowed inside html > head", HtmlCheck.toSelector(element))));
            }
        }
    }
}

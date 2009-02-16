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

public class NoUpperCaseCharactersInUrlsRule implements Rule {

    private final Page page;

    public NoUpperCaseCharactersInUrlsRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src | //*[@href and not(contains(@class, 'external'))]/@href");

        for (Attribute link : links) {
            String value = link.getValue().replaceAll("#.*$", "");
            if (!value.toLowerCase().equals(value)) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains upper case characters in href or src attribute: %s", Selector.from(link.getParent()), value)));
            }
        }
    }
}

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoInlineScriptElementRule implements Rule {

    private final Page page;

    public NoInlineScriptElementRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Element> scripts = XPath.selectNodes(page.getRoot(), "//script[not(@src)]");
        for (Element script : scripts) {
            errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: inline script element found: %s, containing: %s", Selector.from(script), StringUtils.abbreviate(script.getText(), 60))));
        }
    }
}

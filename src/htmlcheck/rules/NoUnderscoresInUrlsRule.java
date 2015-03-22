/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.LinkCheckRule;
import htmlcheck.Page;
import htmlcheck.Selector;
import org.jdom.Attribute;

import java.util.List;

public class NoUnderscoresInUrlsRule extends LinkCheckRule {

    public NoUnderscoresInUrlsRule(Page page) {
        super(page);
    }

    @Override
    public void addErrorsTo(List<HtmlCheckError> errors, List<Attribute> forTheseLinks) throws Exception {
        for (Attribute link : forTheseLinks) {
            if (link.getValue().contains("_")) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains link with underscore in the URL: %s", Selector.from(link.getParent()), link.getValue())));
            }
        }
    }
}

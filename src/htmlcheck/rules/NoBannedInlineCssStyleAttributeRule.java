/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;
import org.jdom.Attribute;
import org.jdom.xpath.XPath;

import java.util.Arrays;
import java.util.List;

public class NoBannedInlineCssStyleAttributeRule implements Rule {

    private final List<String> ALLOWED = Arrays.asList("display", "height", "width", "background-image", "visibility", "top", "left", "bottom", "right");

    private final Page page;

    public NoBannedInlineCssStyleAttributeRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Attribute> attrs = XPath.selectNodes(page.getRoot(), "//*/@style");

        for (Attribute attr : attrs) {
            String style = attr.getValue();
            for (String s : style.split("\\s*;\\s*")) {
                String i = s.split("\\s*:\\s*")[0];
                if (!"".equals(i) && !ALLOWED.contains(i)) {
                    errors.add(new HtmlCheckError(String.format("BANNED ATTRIBUTE: %s uses disallowed inline style: %s", Selector.from(attr.getParent()), i)));
                }
            }
        }
    }
}

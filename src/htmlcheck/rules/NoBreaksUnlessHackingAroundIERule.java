/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import htmlcheck.Selector;

public class NoBreaksUnlessHackingAroundIERule implements Rule {

    private final Page page;

    public NoBreaksUnlessHackingAroundIERule(Page page) {
        this.page = page;
    }
    
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> breaks = XPath.selectNodes(page.getRoot(), "//br[not(contains(@class, 'ieHack'))]");
        for (Element br : breaks) {
            errors.add(new HtmlCheckError(String.format("UNNECESSARY BREAK: %s only needs to be used if hacking around limitations in some versions of Internet Explorer. You can use the 'ieHack' class if that is the case", Selector.from(br))));
        }
    }

}

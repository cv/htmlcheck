/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.List;

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

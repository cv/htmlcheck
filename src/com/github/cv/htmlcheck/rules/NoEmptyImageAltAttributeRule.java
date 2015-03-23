/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import java.util.List;

public class NoEmptyImageAltAttributeRule implements Rule {

    private final Page page;

    public NoEmptyImageAltAttributeRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            List<Element> imgs = XPath.selectNodes(page.getRoot(), "//img[not(@alt) or @alt = '']");

            for (Element img : imgs) {
                errors.add(new HtmlCheckError(String.format("MISSING ALT: missing or empty alt attribute in %s: %s", Selector.from(img), img.getAttributeValue("src"))));
            }
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}

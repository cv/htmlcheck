/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.List;

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

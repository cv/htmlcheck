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

public class TitleLengthLimitRule implements Rule {

    private final int limit;
    private final Page page;

    public TitleLengthLimitRule(Page page, int length) {
        this.page = page;
        limit = length;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        Element title = (Element) XPath.selectSingleNode(page.getRoot(), "//title");
        if (title == null || title.getText() == null) {
            return;
        }

        int length = title.getText().length();
        if (length > limit) {
            errors.add(new HtmlCheckError(String.format("EXCESS TITLE: %s should have at most %d characters (but had %d)", Selector.from(title), limit, length)));
        }
    }
}

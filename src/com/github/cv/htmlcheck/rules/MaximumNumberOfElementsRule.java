/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.Rule;
import org.jdom.xpath.XPath;

import java.util.List;

public class MaximumNumberOfElementsRule implements Rule {

    private final int limit;
    private final Page page;

    public MaximumNumberOfElementsRule(Page page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        int count = XPath.selectNodes(page.getRoot(), "//*").size();

        if (count > limit) {
            errors.add(new HtmlCheckError(String.format("EXCESS ELEMENTS: document contains more elements (%d) than limit (%d)", count, limit)));
        }
    }

}

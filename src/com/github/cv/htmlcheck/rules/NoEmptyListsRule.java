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

public class NoEmptyListsRule implements Rule {

    private final Page page;

    public NoEmptyListsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> emptyLists = XPath.selectNodes(page.getRoot(), "//ul[count(child::*) = 0] | //ol[count(child::*) = 0]");
        for (Element emptyList : emptyLists) {
            errors.add(new HtmlCheckError(String.format("EMPTY LIST: %s cannot be empty", Selector.from(emptyList))));
        }
    }
}

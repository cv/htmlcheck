/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;

import java.util.List;

public class NoXmlDeclarationAtBeginningOfFileRule implements Rule {

    private final Page page;

    public NoXmlDeclarationAtBeginningOfFileRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        if (page.getSource().startsWith("<?xml")) {
            errors.add(new HtmlCheckError("XML PREROLL: page should not start with XML preroll, as it forces IE into standards mode"));
        }
    }
}

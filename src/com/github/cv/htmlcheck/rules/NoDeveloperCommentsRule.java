/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;

import java.util.List;

public class NoDeveloperCommentsRule implements Rule {

    private final Page page;

    public NoDeveloperCommentsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) {
        if (page.getSource().matches(".*?\\<!--[^\\[].*?")) {
            errors.add(new HtmlCheckError(String.format("DEVELOPER COMMENTS: HTML should not contain comments")));
        }
    }
}

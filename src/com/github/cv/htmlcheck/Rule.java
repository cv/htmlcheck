/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck;

import java.util.List;

public interface Rule {
    void addErrorsTo(List<HtmlCheckError> errors) throws Exception;
}

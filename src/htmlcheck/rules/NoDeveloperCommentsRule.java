/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;

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

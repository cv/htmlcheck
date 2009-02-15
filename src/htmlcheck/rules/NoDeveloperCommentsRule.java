package htmlcheck.rules;

import htmlcheck.*;

import java.util.List;

public class NoDeveloperCommentsRule implements Rule {

    private final Page page;

    public NoDeveloperCommentsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) {
        if (this.page.getSource().matches(".*?\\<!--[^\\[].*?")) {
            errors.add(new HtmlCheckError(String.format("DEVELOPER COMMENTS: HTML should not contain comments")));
        }
    }
}

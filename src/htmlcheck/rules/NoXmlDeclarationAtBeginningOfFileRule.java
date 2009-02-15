package htmlcheck.rules;

import htmlcheck.*;

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

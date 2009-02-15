package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.LinkCheckRule;
import htmlcheck.Page;
import htmlcheck.Selector;

import java.util.List;

import org.jdom.Attribute;

public class NoRelativeUrlsRule extends LinkCheckRule {

    public NoRelativeUrlsRule(Page page) {
        super(page);
    }

    @Override
    public void addErrorsTo(List<HtmlCheckError> errors, List<Attribute> forTheseLinks) throws Exception {
        for (Attribute attr : forTheseLinks) {
            if (!attr.getValue().startsWith("http")) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s links to '%s', which is not absolute", Selector.from(attr.getParent()), attr.getValue())));
            }
        }
    }

}

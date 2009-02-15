package htmlcheck.rules;

import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class HyphenLimitInURLSegmentRule implements Rule {

    private final Page page;
    private final int limit;

    public HyphenLimitInURLSegmentRule(Page page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Attribute> links = XPath.selectNodes(this.page.getRoot(), "//*/@src | //*/@href");

        for (Attribute link : links) {
            for (String segment : link.getValue().split("/")) {
                int count = 0;
                for (char c : segment.toCharArray()) {
                    if (c == '-') {
                        count++;
                    }
                }
                if (count > limit) {
                    errors.add(new HtmlCheckError(String.format("EXCESS HYPHENS: more than %d hyphens used in URL segment: %s", limit, link.getValue())));
                }
            }
        }
    }
}

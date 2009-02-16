/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public abstract class LinkCheckRule implements Rule {

    private final Page page;

    public LinkCheckRule(Page page) {
        this.page = page;
    }
    
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src | //*/@href[not(contains(@class, 'external')]");
        addErrorsTo(errors, links);
    }
    
    public abstract void addErrorsTo(List<HtmlCheckError> errors, List<Attribute> forTheseLinks) throws Exception;
}

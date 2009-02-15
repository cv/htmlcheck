/**
 * 
 */
package htmlcheck.rules;

import htmlcheck.*;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoExcessivelyNestedIdsRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoExcessivelyNestedIdsRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//body//*[@id]/*[@id]/*[@id]/*[@id]/*[@id]");

        for(Element element : elements) {
            errors.add(new HtmlCheckError(String.format("ID ABUSE: %s has four or more parents which already have id attributes", HtmlCheck.toSelector(element))));
        }
    }
}
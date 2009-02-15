/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class BodyIdAttributeRequiredRule implements Rule {

	private final HtmlCheck htmlCheck;

	public BodyIdAttributeRequiredRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		Element body = (Element) XPath.selectSingleNode(this.htmlCheck.page.getRoot(), "//body");
		if(body != null && body.getAttributeValue("id", "").equals("")) {
			errors.add(new HtmlCheckError(String.format("NO BODY ID: %s has no id attribute", HtmlCheck.toSelector(body))));
		}
	}
}
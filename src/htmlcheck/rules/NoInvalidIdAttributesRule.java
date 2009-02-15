package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoInvalidIdAttributesRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoInvalidIdAttributesRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> ids = XPath.selectNodes(this.htmlCheck.toLowerCase(this.htmlCheck.page), "//*/@id");
		for (Attribute id : ids) {
			String value = id.getValue();
			if(!value.matches("[a-z]{1}[a-zA-Z0-9]*")) {
				errors.add(new HtmlCheckError(String.format("INVALID ID: %s has an invalid id: %s", HtmlCheck.toSelector(id.getParent()), value)));
			}
		}
	}
}
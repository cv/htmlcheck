package htmlcheck.rules;


import htmlcheck.*;

import java.util.Arrays;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoInvalidAttributesInElementsRule implements Rule {

	private final Page page;

	public NoInvalidAttributesInElementsRule(Page page) {
		this.page = page;
	}

	private final List<String> INVALID = Arrays.asList("//div/@type", "//script/@style", "//script/@class", "//*/@align");
	
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		for (String invalid : INVALID) {
			@SuppressWarnings("unchecked")
			List<Attribute> invalidAttrs = XPath.selectNodes(this.page.getRoot(), invalid);
			for (Attribute attribute : invalidAttrs) {
				errors.add(new HtmlCheckError(String.format("BAD ATTRIBUTE: %s cannot have the '%s' attribute", HtmlCheck.toSelector(attribute.getParent()), attribute.getName())));
			}
		}
	}
}
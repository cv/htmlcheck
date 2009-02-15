package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoInvalidClassAttributesRule implements Rule {
	
	private final HtmlCheck htmlCheck;

	public NoInvalidClassAttributesRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> classes = XPath.selectNodes(this.htmlCheck.toLowerCase(this.htmlCheck.page), "//*/@class");
		for (Attribute clazzAttr : classes) {
			String value = clazzAttr.getValue();
			for (String clazz : value.split(" +")) {
				if(!"".equals(clazz) && !clazz.matches("[a-z]{1}[a-zA-Z0-9]*")) {
					errors.add(new HtmlCheckError(String.format("INVALID CLASS: %s has an invalid class: %s", HtmlCheck.toSelector(clazzAttr.getParent()), clazz)));
				}
			}
		}
	}
}
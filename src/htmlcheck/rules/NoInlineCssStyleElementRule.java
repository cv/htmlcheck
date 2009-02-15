package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoInlineCssStyleElementRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoInlineCssStyleElementRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Element> styles = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//style");
		for (Element style : styles) {
			errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: inline style element found: %s, containing: %s", HtmlCheck.toSelector(style), StringUtils.abbreviate(style.getText(), 60))));
		}
	}
}
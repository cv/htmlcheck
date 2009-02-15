package htmlcheck.rules;


import htmlcheck.*;

import java.util.Arrays;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoBannedInlineCssStyleAttributeRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoBannedInlineCssStyleAttributeRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	private final List<String> ALLOWED = Arrays.asList("display", "height", "width", "background-image", "visibility", "top", "left", "bottom", "right");

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> attrs = XPath.selectNodes(this.htmlCheck.toLowerCase(this.htmlCheck.page), "//*/@style");

		for (Attribute attr : attrs) {
			String style = attr.getValue();
			for (String s : style.split("\\s*;\\s*")) {
				String i = s.split("\\s*:\\s*")[0];
				if (!"".equals(i) && !ALLOWED.contains(i)) {
					errors.add(new HtmlCheckError(String.format("BANNED ATTRIBUTE: %s uses disallowed inline style: %s", HtmlCheck.toSelector(attr.getParent()), i)));
				}
			}
		}
	}
}
package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoUnderscoresInUrlsRule implements Rule {

	private final HtmlCheck htmlCheck;

	NoUnderscoresInUrlsRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> links = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//*/@src");
		links.addAll(XPath.selectNodes(this.htmlCheck.page.getRoot(), "//*/@href"));

		for (Attribute link : links) {
			if (link.getValue().contains("_")) {
				errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains link with underscore in the URL: %s", HtmlCheck.toSelector(link.getParent()), link.getValue())));
			}
		}
	}
}
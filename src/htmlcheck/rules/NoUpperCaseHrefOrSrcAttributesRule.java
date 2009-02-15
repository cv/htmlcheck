/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class NoUpperCaseHrefOrSrcAttributesRule implements Rule {
	/**
	 * 
	 */
	private final HtmlCheck htmlCheck;

	/**
	 * @param htmlCheck
	 */
	public NoUpperCaseHrefOrSrcAttributesRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> links = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//*/@src");
		links.addAll(XPath.selectNodes(this.htmlCheck.page.getRoot(), "//*[@href and not(contains(@class, 'external'))]/@href"));

		for (Attribute link : links) {
			String value = link.getValue().replaceAll("#.*$", "");
			if (!value.toLowerCase().equals(value)) {
				errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains upper case characters in href or src attribute: %s", HtmlCheck.toSelector(link.getParent()), value)));
			}
		}
	}
}
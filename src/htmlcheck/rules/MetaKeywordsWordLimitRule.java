/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.Arrays;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.xpath.XPath;

public class MetaKeywordsWordLimitRule implements Rule {

	/**
	 * 
	 */
	private final HtmlCheck htmlCheck;
	private final int limit;

	public MetaKeywordsWordLimitRule(HtmlCheck htmlCheck, int limit) {
		this.htmlCheck = htmlCheck;
		this.limit = limit;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Attribute> attributes = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//meta[@name='keywords']/@content");
					
		for (Attribute keywords : attributes) {
			String[] splitKeywords = keywords.getValue().split("\\W*,\\W*");
			if (splitKeywords.length > limit) {
				errors.add(new HtmlCheckError(String.format("EXCESS KEYWORDS: meta keywords is over the %d phrases limit: %s", limit, Arrays.asList(splitKeywords))));
			}
		}
	}
}
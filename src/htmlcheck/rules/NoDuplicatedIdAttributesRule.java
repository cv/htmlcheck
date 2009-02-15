/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.*;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoDuplicatedIdAttributesRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoDuplicatedIdAttributesRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	@SuppressWarnings("unchecked")
	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		List<Element> allElementsWithId = XPath.selectNodes(this.htmlCheck.toLowerCase(this.htmlCheck.page), "//*[@id]");
		Map<String, Element> map = new HashMap<String, Element>();
		
		for (Element current : allElementsWithId) {
			Element previous = map.get(current.getAttribute("id").getValue());
			if(previous == null) {
				map.put(current.getAttribute("id").getValue(), current);
			} else {
				errors.add(new HtmlCheckError(String.format("DUPLICATED ID: %s has the same id attribute as %s", HtmlCheck.toSelector(previous), HtmlCheck.toSelector(current))));
			}
		}
	}
}
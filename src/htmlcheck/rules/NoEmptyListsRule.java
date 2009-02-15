/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoEmptyListsRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoEmptyListsRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		@SuppressWarnings("unchecked")
		List<Element> emptyLists = XPath.selectNodes(this.htmlCheck.toLowerCase(this.htmlCheck.page), "//ul[count(child::*) = 0] | //ol[count(child::*) = 0]");
		for (Element emptyList : emptyLists) {
			errors.add(new HtmlCheckError(String.format("EMPTY LIST: %s cannot be empty", HtmlCheck.toSelector(emptyList))));
		}
	}
}
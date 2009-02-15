package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.xpath.XPath;

public class MaximumNumberOfElementsRule implements Rule {

	private final HtmlCheck htmlCheck;
	private final int limit;

	public MaximumNumberOfElementsRule(HtmlCheck htmlCheck, int limit) {
		this.htmlCheck = htmlCheck;
		this.limit = limit;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		int count = XPath.selectNodes(this.htmlCheck.page.getRoot(), "//*").size();

		if (count > limit) {
			errors.add(new HtmlCheckError(String.format("EXCESS ELEMENTS: document contains more elements (%d) than limit (%d)", count, limit)));
		}
	}

}
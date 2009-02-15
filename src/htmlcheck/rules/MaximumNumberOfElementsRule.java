package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.xpath.XPath;

public class MaximumNumberOfElementsRule implements Rule {

	private final Page page;
	private final int limit;

	public MaximumNumberOfElementsRule(Page page, int limit) {
		this.page = page;
		this.limit = limit;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		int count = XPath.selectNodes(this.page.getRoot(), "//*").size();

		if (count > limit) {
			errors.add(new HtmlCheckError(String.format("EXCESS ELEMENTS: document contains more elements (%d) than limit (%d)", count, limit)));
		}
	}

}
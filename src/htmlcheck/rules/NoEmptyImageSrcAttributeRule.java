package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class NoEmptyImageSrcAttributeRule implements Rule {

	private final Page page;

	public NoEmptyImageSrcAttributeRule(Page page) {
		this.page = page;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		@SuppressWarnings("unchecked")
		List<Element> imgs = XPath.selectNodes(this.page.getRoot(), "//img[not(@src) or @src = '']");

		for (Element img : imgs) {
			errors.add(new HtmlCheckError(String.format("MISSING SRC: missing or empty src attribute in %s", HtmlCheck.toSelector(img))));
		}
	}
}
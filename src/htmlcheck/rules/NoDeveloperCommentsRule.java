package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

public class NoDeveloperCommentsRule implements Rule {

	private final HtmlCheck htmlCheck;

	public NoDeveloperCommentsRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) {
		if (this.htmlCheck.page.getSource().matches(".*?\\<!--[^\\[].*?")) {
			errors.add(new HtmlCheckError(String.format("DEVELOPER COMMENTS: HTML should not contain comments")));
		}
	}
}
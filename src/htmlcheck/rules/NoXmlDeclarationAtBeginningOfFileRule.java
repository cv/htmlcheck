/**
 * 
 */
package htmlcheck.rules;


import htmlcheck.*;

import java.util.List;

public class NoXmlDeclarationAtBeginningOfFileRule implements Rule {

	/**
	 * 
	 */
	private final HtmlCheck htmlCheck;

	/**
	 * @param htmlCheck
	 */
	public NoXmlDeclarationAtBeginningOfFileRule(HtmlCheck htmlCheck) {
		this.htmlCheck = htmlCheck;
	}

	public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
		if(this.htmlCheck.page.getSource().startsWith("<?xml")) {
			errors.add(new HtmlCheckError("XML PREROLL: page should not start with XML preroll, as it forces IE into standards mode"));
		}
	}
}
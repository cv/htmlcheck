package htmlcheck;

import htmlcheck.rules.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

public class HtmlCheck {

    public static String toSelector(Element element) {
		StringBuilder selector = new StringBuilder();
		if (element.getParentElement() != null) {
			selector.append(toSelector(element.getParentElement())).append(" > ");
		}

		selector.append(element.getName());

		String id = element.getAttributeValue("id");
		if (id != null) {
			selector.append('#').append(id);
		}

		String[] classes = element.getAttributeValue("class", "").split(" +");
		Arrays.sort(classes);
		
		for (String clazz : classes) {
			if (!"".equals(clazz)) {
				selector.append(".").append(clazz);
			}
		}

		return selector.toString();
	}

	public Page page;

	public HtmlCheck(Page driver) {
		this.page = driver;
	}

	public List<HtmlCheckError> getVerificationErrors() {
		List<HtmlCheckError> errors = new ArrayList<HtmlCheckError>();

		try {
			new NoInvalidElementsInHeadRule(this.page).addErrorsTo(errors);
			new NoXmlDeclarationAtBeginningOfFileRule(this.page).addErrorsTo(errors);
			new NoBannedInlineCssStyleAttributeRule(this.page).addErrorsTo(errors);
			new NoInlineCssStyleElementRule(this.page).addErrorsTo(errors);
			new NoEmptyImageSrcAttributeRule(this.page).addErrorsTo(errors);
			new NoEmptyImageAltAttributeRule(this.page).addErrorsTo(errors);
			new NoDeveloperCommentsRule(this.page).addErrorsTo(errors);
			new NoInlineScriptElementRule(this.page).addErrorsTo(errors);
			new NoUpperCaseHrefOrSrcAttributesRule(this.page).addErrorsTo(errors);
			new MetaKeywordsWordLimitRule(this.page, 20).addErrorsTo(errors);
			new HyphenLimitInURLSegmentRule(this.page, 3).addErrorsTo(errors);
//			new NoUnderscoresInUrlsRule().addErrorsTo(errors);
			new HeaderWordLimitRule(this.page, "h1", 7).addErrorsTo(errors);
			new TitleLengthLimitRule(this.page, 66).addErrorsTo(errors);
			new MaximumNumberOfElementsRule(this.page, 1700).addErrorsTo(errors);
			new NoEventHandlerAttributesRule(this.page).addErrorsTo(errors);
			new NoDivsWithSingleBlockLevelChildRule(this.page).addErrorsTo(errors);
			new NoDuplicatedIdAttributesRule(this.page).addErrorsTo(errors);
			new NoInvalidIdAttributesRule(this.page).addErrorsTo(errors);
			new NoInvalidClassAttributesRule(this.page).addErrorsTo(errors);
			new BodyIdAttributeRequiredRule(this.page).addErrorsTo(errors);
			new NoEmptyListsRule(this.page).addErrorsTo(errors);
			new NoInvalidAttributesInElementsRule(this.page).addErrorsTo(errors);
			new W3CStandardsComplianceRule(this.page).addErrorsTo(errors);
			new NoExcessivelyNestedIdsRule(this.page).addErrorsTo(errors);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return errors;
	}

	public Document toLowerCase(Page browser) throws JDOMException, IOException {
		return new SAXBuilder().build(new StringReader(browser.getSource().toLowerCase()));
	}
}

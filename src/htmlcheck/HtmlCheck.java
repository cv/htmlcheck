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
			new NoInvalidElementsInHeadRule(this).addErrorsTo(errors);
			new NoXmlDeclarationAtBeginningOfFileRule(this).addErrorsTo(errors);
			new NoBannedInlineCssStyleAttributeRule(this).addErrorsTo(errors);
			new NoInlineCssStyleElementRule(this).addErrorsTo(errors);
			new NoEmptyImageSrcAttributeRule(this).addErrorsTo(errors);
			new NoEmptyImageAltAttributeRule(this).addErrorsTo(errors);
			new NoDeveloperCommentsRule(this).addErrorsTo(errors);
			new NoInlineScriptElementRule(this).addErrorsTo(errors);
			new NoUpperCaseHrefOrSrcAttributesRule(this).addErrorsTo(errors);
			new MetaKeywordsWordLimitRule(this, 20).addErrorsTo(errors);
			new HyphenLimitInURLSegmentRule(this, 3).addErrorsTo(errors);
//			new NoUnderscoresInUrlsRule().addErrorsTo(errors);
			new HeaderWordLimitRule(this, "h1", 7).addErrorsTo(errors);
			new TitleLengthLimitRule(this, 66).addErrorsTo(errors);
			new MaximumNumberOfElementsRule(this, 1700).addErrorsTo(errors);
			new NoEventHandlerAttributesRule(this).addErrorsTo(errors);
			new NoDivsWithSingleBlockLevelChildRule(this).addErrorsTo(errors);
			new NoDuplicatedIdAttributesRule(this).addErrorsTo(errors);
			new NoInvalidIdAttributesRule(this).addErrorsTo(errors);
			new NoInvalidClassAttributesRule(this).addErrorsTo(errors);
			new BodyIdAttributeRequiredRule(this).addErrorsTo(errors);
			new NoEmptyListsRule(this).addErrorsTo(errors);
			new NoInvalidAttributesInElementsRule(this).addErrorsTo(errors);
			new W3CStandardsComplianceRule(this).addErrorsTo(errors);
			new NoExcessivelyNestedIdsRule(this).addErrorsTo(errors);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return errors;
	}

	public Document toLowerCase(Page browser) throws JDOMException, IOException {
		return new SAXBuilder().build(new StringReader(browser.getSource().toLowerCase()));
	}
}

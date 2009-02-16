/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck;

import htmlcheck.rules.BodyIdAttributeRequiredRule;
import htmlcheck.rules.HeaderWordLimitRule;
import htmlcheck.rules.HyphenLimitInURLSegmentRule;
import htmlcheck.rules.MaximumNumberOfElementsRule;
import htmlcheck.rules.MetaKeywordsWordLimitRule;
import htmlcheck.rules.NoBannedInlineCssStyleAttributeRule;
import htmlcheck.rules.NoDeveloperCommentsRule;
import htmlcheck.rules.NoDivsWithSingleBlockLevelChildRule;
import htmlcheck.rules.NoDuplicatedIdAttributesRule;
import htmlcheck.rules.NoEmptyImageAltAttributeRule;
import htmlcheck.rules.NoEmptyImageSrcAttributeRule;
import htmlcheck.rules.NoEmptyListsRule;
import htmlcheck.rules.NoEventHandlerAttributesRule;
import htmlcheck.rules.NoExcessivelyNestedIdsRule;
import htmlcheck.rules.NoInlineCssStyleElementRule;
import htmlcheck.rules.NoInlineScriptElementRule;
import htmlcheck.rules.NoInvalidAttributesInElementsRule;
import htmlcheck.rules.NoInvalidClassAttributesRule;
import htmlcheck.rules.NoInvalidElementsInHeadRule;
import htmlcheck.rules.NoInvalidIdAttributesRule;
import htmlcheck.rules.NoRelativeUrlsRule;
import htmlcheck.rules.NoUnderscoresInUrlsRule;
import htmlcheck.rules.NoUpperCaseCharactersInUrlsRule;
import htmlcheck.rules.NoXmlDeclarationAtBeginningOfFileRule;
import htmlcheck.rules.TitleLengthLimitRule;
import htmlcheck.rules.W3CStandardsComplianceRule;

import java.util.ArrayList;
import java.util.List;

public class HtmlCheck {

    private final Page page;

    public HtmlCheck(String document) {
        page = new Page(document);
    }

    public List<HtmlCheckError> getVerificationErrors() {
        List<HtmlCheckError> errors = new ArrayList<HtmlCheckError>();

        try {
            new NoInvalidElementsInHeadRule(page).addErrorsTo(errors);
            new NoXmlDeclarationAtBeginningOfFileRule(page).addErrorsTo(errors);
            new NoBannedInlineCssStyleAttributeRule(page).addErrorsTo(errors);
            new NoInlineCssStyleElementRule(page).addErrorsTo(errors);
            new NoEmptyImageSrcAttributeRule(page).addErrorsTo(errors);
            new NoEmptyImageAltAttributeRule(page).addErrorsTo(errors);
            new NoDeveloperCommentsRule(page).addErrorsTo(errors);
            new NoInlineScriptElementRule(page).addErrorsTo(errors);
            new NoUpperCaseCharactersInUrlsRule(page).addErrorsTo(errors);
            new MetaKeywordsWordLimitRule(page, 20).addErrorsTo(errors);
            new HyphenLimitInURLSegmentRule(page, 3).addErrorsTo(errors);
            new NoUnderscoresInUrlsRule(page).addErrorsTo(errors);
            new NoRelativeUrlsRule(page).addErrorsTo(errors);
            new HeaderWordLimitRule(page, "h1", 7).addErrorsTo(errors);
            new TitleLengthLimitRule(page, 66).addErrorsTo(errors);
            new MaximumNumberOfElementsRule(page, 1700).addErrorsTo(errors);
            new NoEventHandlerAttributesRule(page).addErrorsTo(errors);
            new NoDivsWithSingleBlockLevelChildRule(page).addErrorsTo(errors);
            new NoDuplicatedIdAttributesRule(page).addErrorsTo(errors);
            new NoInvalidIdAttributesRule(page).addErrorsTo(errors);
            new NoInvalidClassAttributesRule(page).addErrorsTo(errors);
            new BodyIdAttributeRequiredRule(page).addErrorsTo(errors);
            new NoEmptyListsRule(page).addErrorsTo(errors);
            new NoInvalidAttributesInElementsRule(page).addErrorsTo(errors);
            new W3CStandardsComplianceRule(page).addErrorsTo(errors);
            new NoExcessivelyNestedIdsRule(page).addErrorsTo(errors);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return errors;
    }

}

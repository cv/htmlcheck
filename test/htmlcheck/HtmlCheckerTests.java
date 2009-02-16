/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class HtmlCheckerTests {

    private List<HtmlCheckError> errorsOn(String page) {
        HtmlCheck v = new HtmlCheck(page);
        List<HtmlCheckError> errors = v.getVerificationErrors();
        return errors;
    }

    private <E> Matcher<? super Collection<? extends E>> isEmpty() {
        return hasSize(0);
    }

    private StringBuilder pageWithXElements(int numberOfElements) {
        StringBuilder bigPage = new StringBuilder("<html><head><title></title></head><body id=\"foo\">");
        for (int i = 0; i < numberOfElements - 2; i++) {
            bigPage.append("<span>big</span>");
        }
        bigPage.append("</body></html>");
        return bigPage;
    }

    @Test
    public void shouldAllowAbsoluteUrlsInHrefAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://adestination/to/somewhere\"></a></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowAbsoluteUrlsInSrcAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script type=\"text/javscrtipt\" src=\"http://foo.com/bar.js\"></script></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowConditionalComments() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><!--[if lte IE 6]><link href=\"/ie-fixes.css\" rel=\"stylesheet\" /><![endif]--></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowInlineScriptsInsideConditionalComments() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><!--[if lte IE 6]><script>foo()</script><![endif]--></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowNonEmptyImageAltAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><img alt=\"foo\" src=\"http://bar.com/foo.jpg\"/></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowScriptElementsWithSrcAttribute() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script type=\"text/javascript\" src=\"http://foo.com/foo.js\"></script></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowUpperCaseCharactersInLinkAnchor() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://foo.com/foo.html#FOO\"></a></body></html>"), isEmpty());
    }

    @Test
    public void shouldAllowUpToAndIncluding1000Elements() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\">Hello</body></html>"), isEmpty());
        StringBuilder pageThatIsUnderTheElementLimit = pageWithXElements(1000);
        assertThat(errorsOn(pageThatIsUnderTheElementLimit.toString()), isEmpty());
    }

    @Test
    public void shouldLimitElementsUsedInsideStyleAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"display: none\">Hidden</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"width: 0\">Width</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"height: 0\">Height</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"background-image: none\">Background Image</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"visibility: none\">Visibility</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"top: 0\">Top</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"left: 0\">Left</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"bottom: 0\">Bottom</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div style=\"right: 0\">Right</div></body></html>"), isEmpty());

        assertThat(errorsOn("<html><body id=\"foo\"><div style=\"foo: bar\">FAIL</div></body></html>"), hasItem(new HtmlCheckError(
                "BANNED ATTRIBUTE: html > body#foo > div uses disallowed inline style: foo")));

        assertThat(errorsOn("<html><body id=\"foo\"><div id=\"bar\" style=\"foo: bar\">FAIL</div></body></html>"), hasItem(new HtmlCheckError(
                "BANNED ATTRIBUTE: html > body#foo > div#bar uses disallowed inline style: foo")));
    }

    @Test
    public void shouldLimitMetaKeywordsToTwentyWords() {
        assertThat(
                errorsOn("<html><head><title></title><meta name=\"keywords\" content=\"1, 2, 3, 4, 5, 6, 7, 8, 9, 10 10, 11, 12, 13, 14, 15, 16, 17, 18, 19\" /></head><body id=\"foo\"></body></html>"),
                isEmpty());
        assertThat(
                errorsOn("<html><head><title></title><meta name=\"keywords\" content=\"1, 2, 3, 4, 5, 6, 7, 8, 9, 10 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20\" /></head><body id=\"foo\"></body></html>"),
                isEmpty());
        assertThat(
                errorsOn("<html><head><title></title><meta name=\"keywords\" content=\"1, 2, 3, 4, 5, 6, 7, 8, 9, 10 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21\" /></head><body id=\"foo\"></body></html>"),
                hasItem(new HtmlCheckError("EXCESS KEYWORDS: meta keywords is over the 20 phrases limit: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21]")));
    }

    @Test
    public void shouldLimitNumberOfElements() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\">Hello</body></html>"), isEmpty());
        StringBuilder pageWithTooManyElements = pageWithXElements(1701);
        assertThat(errorsOn(pageWithTooManyElements.toString()), hasItem(new HtmlCheckError("EXCESS ELEMENTS: document contains more elements (1703) than limit (1700)")));
    }

    @Test
    public void shouldLimitNumberOfWordsInHeaderElement() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><h1>1 2 3 4 5 6</h1></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><h1>1 2 3 4 5 6 7</h1></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><h1>1 2 3 4 5 6 7 8</h1></body></html>"), hasItem(new HtmlCheckError(
                "HEADER WORD LIMIT: html > body#foo > h1 should have at most 7 words, but had 8 (1 2 3 4 5 6 7 8)")));
    }

    @Test
    public void shouldLimitPageTitleLength() {
        assertThat(errorsOn("<html><head><title>the quick furry brown fox jumps madly over the incredibly lazy dog</title></head><body id=\"foo\"></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title>the quick furry brown fox jumps madly over the incredibly lazy dogs</title></head><body></body></html>"), hasItem(new HtmlCheckError(
                "EXCESS TITLE: html > head > title should have at most 66 characters (but had 67)")));
    }

    @Test
    public void shouldNotAllowDeveloperComments() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><!-- this is not allowed --></body></html>"), hasItem(new HtmlCheckError(
                "DEVELOPER COMMENTS: HTML should not contain comments")));
    }

    @Test
    public void shouldNotAllowDuplicatedIdAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"good\">good</div></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad\"/><div id=\"bad\"></div></body></html>"), hasItem(new HtmlCheckError(
                "DUPLICATED ID: html > body#foo > div#bad has the same id attribute as html > body#foo > div#bad")));

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><p><div id=\"bad\"/></p><div id=\"bad\"></div></body></html>"), hasItem(new HtmlCheckError(
                "DUPLICATED ID: html > body#foo > div#bad has the same id attribute as html > body#foo > p > div#bad")));
    }

    @Test
    public void shouldNotAllowEmptyLists() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><ul class=\"empty\"></ul></body></html>"), hasItem(new HtmlCheckError(
                "EMPTY LIST: html > body#foo > ul.empty cannot be empty")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><ol class=\"empty\"></ol></body></html>"), hasItem(new HtmlCheckError(
                "EMPTY LIST: html > body#foo > ol.empty cannot be empty")));
    }

    @Test
    public void shouldNotAllowEventHandlingAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\">Hello</body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\" onload=\"foo()\">Hello</body></html>"), hasItem(new HtmlCheckError(
                "BANNED ATTRIBUTE: event handler attribute not allowed: onload in html > body#foo")));
    }

    @Test
    public void shouldNotAllowInlineScriptElements() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script>bad.script()</script></body></html>"), hasItem(new HtmlCheckError(
                "BANNED ELEMENT: inline script element found: html > body#foo > script, containing: bad.script()")));

        assertThat(
                errorsOn("<html><head><title></title></head><body id=\"foo\"><script>bad.script(whichIsReallyLongAndSpansMultipleLinesEtcEtcEtc(seriouslyThisIsALotOfCodeAndShouldNeverHaveBeenInlinedInTheFirstPlace(butWeWantANiceErrorMessageComingOutOfTheCheckerAnyway()))</script></body></html>"),
                hasItem(new HtmlCheckError("BANNED ELEMENT: inline script element found: html > body#foo > script, containing: bad.script(whichIsReallyLongAndSpansMultipleLinesEtcEtcEt...")));
    }

    @Test
    public void shouldNotAllowInlineStyleAttributes() {
        assertThat(errorsOn("<html><body id=\"foo\"><a style=\"color: black\">Bad inline style</a></body></html>"), hasItem(new HtmlCheckError(
                "BANNED ATTRIBUTE: html > body#foo > a uses disallowed inline style: color")));
    }

    @Test
    public void shouldNotAllowInlineStyleCSSElements() {
        assertThat(errorsOn("<html><body id=\"foo\"><style type=\"text/css\">Bad inline style</style></body></html>"), hasItem(new HtmlCheckError(
                "BANNED ELEMENT: inline style element found: html > body#foo > style, containing: Bad inline style")));
    }

    @Test
    public void shouldNotAllowInvalidAttributesInElements() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script class=\"foo\"></script></body></html>"), hasItem(new HtmlCheckError(
                "BAD ATTRIBUTE: html > body#foo > script.foo cannot have the 'class' attribute")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div type=\"text/javascript\"></div></body></html>"), hasItem(new HtmlCheckError(
                "BAD ATTRIBUTE: html > body#foo > div cannot have the 'type' attribute")));
    }

    @Test
    public void shouldNotAllowMoreThanThreeHyphensBetweenForwardSlashes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://none/to/somewhere\"></a></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://one/to-somewhere\"></a></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://three/to-some-whe-re\"></a></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://three-with-slash/to-some-whe-re/\"></a></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://four/to-so-me-whe-re/\"></a></body></html>"), hasItem(new HtmlCheckError(
                "EXCESS HYPHENS: more than 3 hyphens used in URL segment: http://four/to-so-me-whe-re/")));
    }

    @Test
    public void shouldNotAllowOnlyOneBlockLevelElementInsideDivs() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"good\">good</div></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"outer\"><div id=\"inner\">bad</div></div></body></html>"), hasItem(new HtmlCheckError(
                "UNNECESSARY DIV: html > body#foo > div#outer contains only one block level element: html > body#foo > div#outer > div#inner")));

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"outer\"><div class=\"inner\">bad</div></div></body></html>"), hasItem(new HtmlCheckError(
                "UNNECESSARY DIV: html > body#foo > div.outer contains only one block level element: html > body#foo > div.outer > div.inner")));
    }

    @Test
    public void shouldNotAllowRelativeUrlsInHrefAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"adestination/to/somewhere\"></a></body></html>"), hasItem(new HtmlCheckError(
                "INVALID URL: html > body#foo > a links to 'adestination/to/somewhere', which is not absolute")));
    }

    @Test
    public void shouldNotAllowRelativeUrlsInSrcAttributes() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script src=\"/bar.js\"></script></body></html>"), hasItem(new HtmlCheckError(
                "INVALID URL: html > body#foo > script links to '/bar.js', which is not absolute")));
    }

    @Test
    public void shouldNotAllowUnderscoresInUrls() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://foo.com/bar\"></a></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><a href=\"http://foo.com/bar_quux\"></a></body></html>"), hasItem(new HtmlCheckError(
                "INVALID URL: html > body#foo > a contains link with underscore in the URL: http://foo.com/bar_quux")));
    }

    @Test
    public void shouldNotAllowUpperCaseInHrefAttribute() {
        assertThat(errorsOn("<html><body id=\"foo\"><a href=\"http://foo.com/Foo.html\"></a></body></html>"), hasItem(new HtmlCheckError(
                "INVALID URL: html > body#foo > a contains upper case characters in href or src attribute: http://foo.com/Foo.html")));
    }

    @Test
    public void shouldNotAllowUpperCaseInSrcAttribute() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><script src=\"http://foo.com/Foo.js\"></script></body></html>"), hasItem(new HtmlCheckError(
                "INVALID URL: html > body#foo > script contains upper case characters in href or src attribute: http://foo.com/Foo.js")));
    }

    @Test
    public void shouldNotAllowXmlPreRolls() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"></body></html>"), isEmpty());
        assertThat(errorsOn("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title></title></head><body id=\"foo\"></body></html>"), hasItem(new HtmlCheckError(
                "XML PREROLL: page should not start with XML preroll, as it forces IE into standards mode")));
    }

    @Test
    public void shouldNotInterfereWithOtherMetaElements() {
        assertThat(errorsOn("<html><head><title></title><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /></head><body id=\"foo\"></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title><meta name=\"refresh\" content=\"0; 0; 0\" /></head><body id=\"foo\"></body></html>"), isEmpty());
    }

    @Test
    public void shouldRequireIdAttributeInBodyElements() throws Exception {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"good\">good</body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body>bad</body></html>"), hasItem(new HtmlCheckError("NO BODY ID: html > body has no id attribute")));
    }

    @Test
    public void shouldRequireImageAltAttribute() {
        assertThat(errorsOn("<html><body id=\"foo\"><img src=\"http://bar.com/foo.jpg\"/></body></html>"), hasItem(new HtmlCheckError(
                "MISSING ALT: missing or empty alt attribute in html > body#foo > img: http://bar.com/foo.jpg")));
    }

    @Test
    public void shouldRequireImageSrcAttribute() {
        assertThat(errorsOn("<html><body id=\"foo\"><img alt=\"the alt\"/></body></html>"), hasItem(new HtmlCheckError("MISSING SRC: missing or empty src attribute in html > body#foo > img")));
    }

    @Test
    public void shouldRequireNonEmptyImageAltAttributes() {
        assertThat(errorsOn("<html><body id=\"foo\"><img alt=\"\" src=\"http://foo.com/bar.jpg\"/></body></html>"), hasItem(new HtmlCheckError(
                "MISSING ALT: missing or empty alt attribute in html > body#foo > img: http://foo.com/bar.jpg")));
    }

    @Test
    public void shouldValidateFormatOfClassNames() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"\">good</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"good\">good</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"goodClass\">good</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"good9\">good</div></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"9bad\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID CLASS: html > body#foo > div.9bad has an invalid class: 9bad")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"bad-9\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID CLASS: html > body#foo > div.bad-9 has an invalid class: bad-9")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"bad_9\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID CLASS: html > body#foo > div.bad_9 has an invalid class: bad_9")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"bad_class\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID CLASS: html > body#foo > div.bad_class has an invalid class: bad_class")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div class=\"bad-class\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID CLASS: html > body#foo > div.bad-class has an invalid class: bad-class")));
    }

    @Test
    public void shouldValidateFormatOfIds() {
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"good\">good</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"goodId\">good</div></body></html>"), isEmpty());
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"good9\">good</div></body></html>"), isEmpty());

        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div# has an invalid id: ")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"9bad\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#9bad has an invalid id: 9bad")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad-9\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#bad-9 has an invalid id: bad-9")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad_9\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#bad_9 has an invalid id: bad_9")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad_id\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#bad_id has an invalid id: bad_id")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad-id\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#bad-id has an invalid id: bad-id")));
        assertThat(errorsOn("<html><head><title></title></head><body id=\"foo\"><div id=\"bad id\">bad</div></body></html>"), hasItem(new HtmlCheckError(
                "INVALID ID: html > body#foo > div#bad id has an invalid id: bad id")));
    }

}

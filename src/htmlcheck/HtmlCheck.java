package htmlcheck;

import htmlcheck.rules.NoExcessivelyNestedIdsRule;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.jdom.*;
import org.jdom.Attribute;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.w3c.tidy.*;
import org.w3c.tidy.TidyMessage.Level;

public class HtmlCheck {

    class BodyIdAttributeRequiredRule implements Rule {

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			Element body = (Element) XPath.selectSingleNode(page.getRoot(), "//body");
			if(body != null && body.getAttributeValue("id", "").equals("")) {
				errors.add(new HtmlCheckError(String.format("NO BODY ID: %s has no id attribute", toSelector(body))));
			}
		}
	}

	class HeaderWordLimitRule implements Rule {

		private final String header;
		private final int wordLimit;

		public HeaderWordLimitRule(String header, int wordLimit) {
			this.header = header;
			this.wordLimit = wordLimit;
		}

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> headers = XPath.selectNodes(page.getRoot(), "//" + header);

			for (Element header : headers) {
				int foundLength = header.getText().split("\\W+").length;
				if (foundLength > wordLimit) {
					errors.add(new HtmlCheckError(String.format("HEADER WORD LIMIT: %s should have at most %d words, but had %d (%s)", toSelector(header), wordLimit, foundLength, StringUtils.abbreviate(header.getText(), 60))));
				}
			}
		}
	}

	class HyphenLimitInURLSegmentRule implements Rule {

		private final int limit;

		public HyphenLimitInURLSegmentRule(int limit) {
			this.limit = limit;
		}

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src | //*/@href");

			for (Attribute link : links) {
				for (String segment : link.getValue().split("/")) {
					int count = 0;
					for (char c : segment.toCharArray()) {
						if (c == '-') {
							count++;
						}
					}
					if (count > limit) {
						errors.add(new HtmlCheckError(String.format("EXCESS HYPHENS: more than %d hyphens used in URL segment: %s", limit, link)));
					}
				}
			}
		}
	}

	class MaximumNumberOfElementsRule implements Rule {

		private final int limit;

		public MaximumNumberOfElementsRule(int limit) {
			this.limit = limit;
		}

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			int count = XPath.selectNodes(page.getRoot(), "//*").size();

			if (count > limit) {
				errors.add(new HtmlCheckError(String.format("EXCESS ELEMENTS: document contains more elements (%d) than limit (%d)", count, limit)));
			}
		}

	}

	class MetaKeywordsWordLimitRule implements Rule {

		private final int limit;

		public MetaKeywordsWordLimitRule(int limit) {
			this.limit = limit;
		}

		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> attributes = XPath.selectNodes(page.getRoot(), "//meta[@name='keywords']/@content");
						
			for (Attribute keywords : attributes) {
				String[] splitKeywords = keywords.getValue().split("\\W*,\\W*");
				if (splitKeywords.length > limit) {
					errors.add(new HtmlCheckError(String.format("EXCESS KEYWORDS: meta keywords is over the %d phrases limit: %s", limit, Arrays.asList(splitKeywords))));
				}
			}
		}
	}

	class NoBannedInlineCssStyleAttributeRule implements Rule {
		private final List<String> ALLOWED = Arrays.asList("display", "height", "width", "background-image", "visibility", "top", "left", "bottom", "right");

		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> attrs = XPath.selectNodes(toLowerCase(page), "//*/@style");

			for (Attribute attr : attrs) {
				String style = attr.getValue();
				for (String s : style.split("\\s*;\\s*")) {
					String i = s.split("\\s*:\\s*")[0];
					if (!"".equals(i) && !ALLOWED.contains(i)) {
						errors.add(new HtmlCheckError(String.format("BANNED ATTRIBUTE: %s uses disallowed inline style: %s", toSelector(attr.getParent()), i)));
					}
				}
			}
		}
	}

	class NoDeveloperCommentsRule implements Rule {
		public void addErrorsTo(List<HtmlCheckError> errors) {
			if (page.getSource().matches(".*?\\<!--[^\\[].*?")) {
				errors.add(new HtmlCheckError(String.format("DEVELOPER COMMENTS: HTML should not contain comments")));
			}
		}
	}

	class NoDivsWithSingleBlockLevelChildRule implements Rule {

		private final List<String> BLOCK_LEVEL_ELEMENTS = Arrays.asList("p", "h1", "h2", "h3", "h4", "h5", "h6", "ol", "ul", "pre", "address", "blockquote", "dl", "div", "fieldset" , "form", "hr", "noscript", "table");
	    	
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> divsWithSingleChild = XPath.selectNodes(toLowerCase(page), "//div[count(child::*) = 1]");
			for (Element divWithSingleChild : divsWithSingleChild) {
				Element child = (Element) divWithSingleChild.getChildren().get(0);
				if (BLOCK_LEVEL_ELEMENTS.contains(child.getName())) {
					errors.add(new HtmlCheckError(String.format("UNNECESSARY DIV: %s contains only one block level element: %s", toSelector(divWithSingleChild), toSelector(child))));
				}
			}
		}
	}

	class NoDuplicatedIdAttributesRule implements Rule {

		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Element> allElementsWithId = XPath.selectNodes(toLowerCase(page), "//*[@id]");
			Map<String, Element> map = new HashMap<String, Element>();
			
			for (Element current : allElementsWithId) {
				Element previous = map.get(current.getAttribute("id").getValue());
				if(previous == null) {
					map.put(current.getAttribute("id").getValue(), current);
				} else {
					errors.add(new HtmlCheckError(String.format("DUPLICATED ID: %s has the same id attribute as %s", toSelector(previous), toSelector(current))));
				}
			}
		}
	}

	class NoEmptyImageAltAttributeRule implements Rule {
		public void addErrorsTo(List<HtmlCheckError> errors) {
			try {
				@SuppressWarnings("unchecked")
				List<Element> imgs = XPath.selectNodes(page.getRoot(), "//img[not(@alt) or @alt = '']");

				for (Element img : imgs) {
					errors.add(new HtmlCheckError(String.format("MISSING ALT: missing or empty alt attribute in %s: %s", toSelector(img), img.getAttributeValue("src"))));
				}
			} catch (JDOMException e) {
				throw new RuntimeException(e);
			}
		}
	}

	class NoEmptyImageSrcAttributeRule implements Rule {
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> imgs = XPath.selectNodes(page.getRoot(), "//img[not(@src) or @src = '']");

			for (Element img : imgs) {
				errors.add(new HtmlCheckError(String.format("MISSING SRC: missing or empty src attribute in %s", toSelector(img))));
			}
		}
	}

	class NoEmptyListsRule implements Rule {

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> emptyLists = XPath.selectNodes(toLowerCase(page), "//ul[count(child::*) = 0] | //ol[count(child::*) = 0]");
			for (Element emptyList : emptyLists) {
				errors.add(new HtmlCheckError(String.format("EMPTY LIST: %s cannot be empty", toSelector(emptyList))));
			}
		}
	}

	class NoEventHandlerAttributesRule implements Rule {

		private final List<String> BANNED = Arrays.asList("onabort", "onblur", "onchange", "onclick", "ondblclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onload",
				"onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onresize", "onselect", "onsubmit", "onunload");

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> elements = XPath.selectNodes(toLowerCase(page), bannedAttributesAsXPath());

			for (Element element : elements) {
				errors.add(new HtmlCheckError(String.format("BANNED ATTRIBUTE: event handler attribute not allowed: %s in %s", bannedAttributeFor(element), toSelector(element))));
			}
		}

		private String bannedAttributeFor(Element element) {
			@SuppressWarnings("unchecked")
			List<Attribute> attributes = element.getAttributes();

			for (Attribute attribute : attributes) {
				if (BANNED.contains(attribute.getName()))
					return attribute.getName();
			}
			return "unknown";
		}

		private String bannedAttributesAsXPath() {
			StringBuilder xpathBuilder = new StringBuilder("//*[");
			for (int i = 0; i < BANNED.size(); i++) {
				xpathBuilder.append("@").append(BANNED.get(i));
				if (i < BANNED.size() - 1) {
					xpathBuilder.append(" or ");
				} else {
					xpathBuilder.append("]");
				}
			}

			return xpathBuilder.toString();
		}
	}

	class NoInlineCssStyleElementRule implements Rule {
		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Element> styles = XPath.selectNodes(page.getRoot(), "//style");
			for (Element style : styles) {
				errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: inline style element found: %s, containing: %s", toSelector(style), StringUtils.abbreviate(style.getText(), 60))));
			}
		}
	}

	class NoInlineScriptElementRule implements Rule {
		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Element> scripts = XPath.selectNodes(page.getRoot(), "//script[not(@src)]");
			for (Element script : scripts) {
				errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: inline script element found: %s, containing: %s", toSelector(script), StringUtils.abbreviate(script.getText(), 60))));
			}
		}
	}

	class NoInvalidAttributesInElementsRule implements Rule {

		private final List<String> INVALID = Arrays.asList("//div/@type", "//script/@style", "//script/@class", "//*/@align");
		
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			for (String invalid : INVALID) {
				@SuppressWarnings("unchecked")
				List<Attribute> invalidAttrs = XPath.selectNodes(toLowerCase(page), invalid);
				for (Attribute attribute : invalidAttrs) {
					errors.add(new HtmlCheckError(String.format("BAD ATTRIBUTE: %s cannot have the '%s' attribute", toSelector(attribute.getParent()), attribute.getName())));
				}
			}
		}
	}

	class NoInvalidClassAttributesRule implements Rule {
		
		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> classes = XPath.selectNodes(toLowerCase(page), "//*/@class");
			for (Attribute clazzAttr : classes) {
				String value = clazzAttr.getValue();
				for (String clazz : value.split(" +")) {
					if(!"".equals(clazz) && !clazz.matches("[a-z]{1}[a-zA-Z0-9]*")) {
						errors.add(new HtmlCheckError(String.format("INVALID CLASS: %s has an invalid class: %s", toSelector(clazzAttr.getParent()), clazz)));
					}
				}
			}
		}
	}

	class NoInvalidElementsInHeadRule implements Rule {

		private final List<String> allowed = Arrays.asList("meta", "title", "script", "link", "style");
		
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			@SuppressWarnings("unchecked")
			List<Element> elements = XPath.selectNodes(toLowerCase(page), "/html/head/*");

			for (Element element : elements) {
				if(!allowed.contains(element.getName())) {
					errors.add(new HtmlCheckError(String.format("BANNED ELEMENT: %s is not allowed inside html > head", toSelector(element))));
				}
			}
		}
	}

	class NoInvalidIdAttributesRule implements Rule {

		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> ids = XPath.selectNodes(toLowerCase(page), "//*/@id");
			for (Attribute id : ids) {
				String value = id.getValue();
				if(!value.matches("[a-z]{1}[a-zA-Z0-9]*")) {
					errors.add(new HtmlCheckError(String.format("INVALID ID: %s has an invalid id: %s", toSelector(id.getParent()), value)));
				}
			}
		}
	}

	class NoUnderscoresInUrlsRule implements Rule {
		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src");
			links.addAll(XPath.selectNodes(page.getRoot(), "//*/@href"));

			for (Attribute link : links) {
				if (link.getValue().contains("_")) {
					errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains link with underscore in the URL: %s", toSelector(link.getParent()), link.getValue())));
				}
			}
		}
	}

	class NoUpperCaseHrefOrSrcAttributesRule implements Rule {
		@SuppressWarnings("unchecked")
		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			List<Attribute> links = XPath.selectNodes(page.getRoot(), "//*/@src");
			links.addAll(XPath.selectNodes(page.getRoot(), "//*[@href and not(contains(@class, 'external'))]/@href"));

			for (Attribute link : links) {
				String value = link.getValue().replaceAll("#.*$", "");
				if (!value.toLowerCase().equals(value)) {
					errors.add(new HtmlCheckError(String.format("INVALID URL: %s contains upper case characters in href or src attribute: %s", toSelector(link.getParent()), value)));
				}
			}
		}
	}

	class NoXmlDeclarationAtBeginningOfFileRule implements Rule {

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			if(page.getSource().startsWith("<?xml")) {
				errors.add(new HtmlCheckError("XML PREROLL: page should not start with XML preroll, as it forces IE into standards mode"));
			}
		}
	}

	public interface Rule {
		void addErrorsTo(List<HtmlCheckError> errors) throws Exception;
	}

	class TitleLengthLimitRule implements Rule {

		private final int limit;

		public TitleLengthLimitRule(int length) {
			this.limit = length;
		}

		public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
			Element title = (Element) XPath.selectSingleNode(page.getRoot(), "//title");
			if (title == null || title.getText() == null) {
				return;
			}

			int length = title.getText().length();
			if (length > limit) {
				errors.add(new HtmlCheckError(String.format("EXCESS TITLE: %s should have at most %d characters (but had %d)", toSelector(title), limit, length)));
			}
		}
	}

	class W3CStandardsComplianceRule implements Rule {

		public void addErrorsTo(final List<HtmlCheckError> errors) throws Exception {
			Tidy tidy = new Tidy();
			tidy.setErrout(new PrintWriter(new StringWriter()));
			tidy.setDocType("\"-//W3C//DTD XHTML 1.0 Strict//EN\"");
			tidy.setXHTML(true);
			
			tidy.setMessageListener(new TidyMessageListener() {
			
				private List<Integer> ignored = Arrays.asList(
						44, // doc type declaration not present
						49, // <script> lacks type attribute
						23, // empty spans
						66  // element id already defined (we have a specific check for that)
				);

				public void messageReceived(TidyMessage message) {
					if(shouldReport(message)) {
						errors.add(new HtmlCheckError(String.format("TIDY: %s (code %d, line %d, column %d)", message.getMessage(), message.getErrorCode(), message.getLine(), message.getColumn())));
					}
				}

				private boolean shouldReport(TidyMessage message) {
					return !ignored .contains(message.getErrorCode()) && (message.getLevel() == Level.ERROR || message.getLevel() == Level.WARNING);
				}
			});
			tidy.parse(new StringReader(page.getSource()), new StringWriter());
		}
	}

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
			new NoInvalidElementsInHeadRule().addErrorsTo(errors);
			new NoXmlDeclarationAtBeginningOfFileRule().addErrorsTo(errors);
			new NoBannedInlineCssStyleAttributeRule().addErrorsTo(errors);
			new NoInlineCssStyleElementRule().addErrorsTo(errors);
			new NoEmptyImageSrcAttributeRule().addErrorsTo(errors);
			new NoEmptyImageAltAttributeRule().addErrorsTo(errors);
			new NoDeveloperCommentsRule().addErrorsTo(errors);
			new NoInlineScriptElementRule().addErrorsTo(errors);
			new NoUpperCaseHrefOrSrcAttributesRule().addErrorsTo(errors);
			new MetaKeywordsWordLimitRule(20).addErrorsTo(errors);
			new HyphenLimitInURLSegmentRule(3).addErrorsTo(errors);
//			new NoUnderscoresInUrlsRule().addErrorsTo(errors);
			new HeaderWordLimitRule("h1", 7).addErrorsTo(errors);
			new TitleLengthLimitRule(66).addErrorsTo(errors);
			new MaximumNumberOfElementsRule(1700).addErrorsTo(errors);
			new NoEventHandlerAttributesRule().addErrorsTo(errors);
			new NoDivsWithSingleBlockLevelChildRule().addErrorsTo(errors);
			new NoDuplicatedIdAttributesRule().addErrorsTo(errors);
			new NoInvalidIdAttributesRule().addErrorsTo(errors);
			new NoInvalidClassAttributesRule().addErrorsTo(errors);
			new BodyIdAttributeRequiredRule().addErrorsTo(errors);
			new NoEmptyListsRule().addErrorsTo(errors);
			new NoInvalidAttributesInElementsRule().addErrorsTo(errors);
			new W3CStandardsComplianceRule().addErrorsTo(errors);
			new NoExcessivelyNestedIdsRule(this).addErrorsTo(errors);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return errors;
	}

	private Document toLowerCase(Page browser) throws JDOMException, IOException {
		return new SAXBuilder().build(new StringReader(browser.getSource().toLowerCase()));
	}
}

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.Arrays;
import java.util.List;

public class NoEventHandlerAttributesRule implements Rule {

    private final List<String> BANNED = Arrays.asList("onabort", "onblur", "onchange", "onclick", "ondblclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onload", "onmousedown",
            "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onresize", "onselect", "onsubmit", "onunload");

    private final Page page;

    public NoEventHandlerAttributesRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(page.getRoot(), bannedAttributesAsXPath());

        for (Element element : elements) {
            errors.add(new HtmlCheckError(String.format("BANNED ATTRIBUTE: event handler attribute not allowed: %s in %s", bannedAttributeFor(element), Selector.from(element))));
        }
    }

    private String bannedAttributeFor(Element element) {
        @SuppressWarnings("unchecked")
        List<Attribute> attributes = element.getAttributes();

        for (Attribute attribute : attributes) {
            if (BANNED.contains(attribute.getName())) {
                return attribute.getName();
            }
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

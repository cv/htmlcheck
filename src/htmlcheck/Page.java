/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck;

import java.io.StringReader;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Page {

    private final String page;

    public Page(String page) {
        this.page = page;
    }

    public Element getRoot() throws Exception {
        return new SAXBuilder().build(new StringReader(page)).getRootElement();
    }

    public String getSource() {
        return page;
    }

}

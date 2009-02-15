package htmlcheck;

import java.io.StringReader;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Page {

	private String page;
	
	public Page(String page) {
		this.page = page;
	}

	public String getSource() {
		return page;
	}

	public Element getRoot() throws Exception {
		return new SAXBuilder().build(new StringReader(page)).getRootElement();
	}

}

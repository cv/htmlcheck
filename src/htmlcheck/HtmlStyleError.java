package htmlcheck;

import org.jdom.Element;


public class HtmlStyleError extends AssertionError {

	private static final long serialVersionUID = 1L;
	private Element element;

	public HtmlStyleError(String message) {
		super(message);
	}

	public HtmlStyleError(String message, Element element) {
		super(message);
		this.element = element;
	}

	public Element getElement() {
		return element;
	}
	
	@Override
	public String toString() {
		return "\n" + getMessage();
	}
	
	@Override
	public boolean equals(Object that) {
		return this.toString().equals(that.toString());
	}
	
}

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package htmlcheck.rules;

import htmlcheck.HtmlCheckError;
import htmlcheck.Page;
import htmlcheck.Rule;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessage.Level;
import org.w3c.tidy.TidyMessageListener;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class W3CStandardsComplianceRule implements Rule {

    private final Page page;

    public W3CStandardsComplianceRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(final List<HtmlCheckError> errors) throws Exception {
        Tidy tidy = new Tidy();
        tidy.setErrout(new PrintWriter(new StringWriter()));
        tidy.setDocType("\"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        tidy.setXHTML(true);

        tidy.setMessageListener(new TidyMessageListener() {

            private final List<Integer> ignored = Arrays.asList(44, // doc type
                    // declaration not
                    // present
                    49, // <script> lacks type attribute
                    23, // empty spans
                    66 // element id already defined (we have a specific check
                    // for that)
            );

            public void messageReceived(TidyMessage message) {
                if (shouldReport(message)) {
                    errors.add(new HtmlCheckError(String.format("TIDY: %s (code %d, line %d, column %d)", message.getMessage(), message.getErrorCode(), message.getLine(), message.getColumn())));
                }
            }

            private boolean shouldReport(TidyMessage message) {
                return !ignored.contains(message.getErrorCode()) && (message.getLevel() == Level.ERROR || message.getLevel() == Level.WARNING);
            }
        });
        tidy.parse(new StringReader(page.getSource()), new StringWriter());
    }
}

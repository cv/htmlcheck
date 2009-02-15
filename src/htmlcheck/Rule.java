package htmlcheck;

import java.util.List;

public interface Rule {
    void addErrorsTo(List<HtmlCheckError> errors) throws Exception;
}

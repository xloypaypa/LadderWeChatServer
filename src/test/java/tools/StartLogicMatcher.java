package tools;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import spring.logic.StartLogic;

public class StartLogicMatcher implements Matcher<StartLogic> {

    private String message;

    public StartLogicMatcher(String message) {
        this.message = message;
    }

    @Override
    public boolean matches(Object item) {
        return message.equals(((StartLogic) item).getReplyFromServer());
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        if (item.getClass().equals(StartLogic.class)) {
            mismatchDescription.appendText("should be: " + message + "; but: " + ((StartLogic) item).getReplyFromServer());
        } else {
            mismatchDescription.appendText("should be: " + StartLogic.class.getName() + "; but : " + item.getClass().getName());
        }
    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }

    @Override
    public void describeTo(Description description) {

    }
}

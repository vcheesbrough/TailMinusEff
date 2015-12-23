package tailminuseff.fx;

import java.util.regex.Pattern;

class UserSearchCommand {

    private final Pattern pattern;

    public UserSearchCommand(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "UserSearchCommand{" + "pattern=" + pattern + '}';
    }

}

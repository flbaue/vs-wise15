package haw.vs.superteam.playerservice.model;

/**
 * Created by florian on 14.12.15.
 */
public class Command {
    private String command;
    private String content;

    public Command(String command, String content) {
        this.command = command;
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public String getContent() {
        return content;
    }
}

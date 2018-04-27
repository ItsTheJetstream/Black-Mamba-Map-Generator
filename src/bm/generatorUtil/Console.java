package bm.generatorUtil;

import javafx.scene.control.TextArea;

public class Console {

    private TextArea console;

    public Console(TextArea console) {
        this.console = console;
        this.console.setEditable(false);
    }

    public void clearConsole() {
        console.clear();
    }

    public void write(String text) {
        console.appendText(text + "\n");
    }

    public void write(String[] lines) {
        for (String s : lines) {
            write(s);
        }
    }

    public void putCommand(String command) {
        String[] lines = {"[HELP] This is feature currently W.I.P.", "[HELP] No commands implemented yet"};
        switch (command) {
            case "help":
                write(lines);
                break;
            default:
                write(lines);
                break;
        }
    }
}

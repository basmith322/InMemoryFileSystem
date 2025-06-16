package filesystem.entities;

import filesystem.api.FileSystemEntity;

public class TextFile extends FileSystemEntity {
    private String content;

    public TextFile(String name) {
        super(name, "TextFile");
        this.content = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
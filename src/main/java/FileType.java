public class FileType {
    private String name;
    private String content;

    public FileType() { }

    public FileType(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "file{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}

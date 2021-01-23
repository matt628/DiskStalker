package pl.edu.agh.diskstalker.database.model;

public enum Type {

    JPG("Image"),
    PNG("Image"),
    PDF("File"),
    TXT("File"),
    MP3("Music"),
    MP4("Video"),
    EXE("Application"),
    ZIP("Archive"),
    FOLDER("Folder"),
    OTHER("Other"),
    ;

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public static Type getType(String type) {
        return switch (type) {
            case ".jpg" -> Type.JPG;
            case ".png" -> Type.PNG;
            case ".pdf" -> Type.PDF;
            case ".txt" -> Type.TXT;
            case ".mp3" -> Type.MP3;
            case ".mp4" -> Type.MP4;
            case ".exe" -> Type.EXE;
            case ".zip" -> Type.ZIP;
            case "folder" -> Type.FOLDER;
            default -> Type.OTHER;
        };
    }

    public String getDescription() {
        return description;
    }
}

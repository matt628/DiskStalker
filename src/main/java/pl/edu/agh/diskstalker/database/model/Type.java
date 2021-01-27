package pl.edu.agh.diskstalker.database.model;

public enum Type {

    JPG("Image"),
    JPEG("JPEG graphic"),
    PNG("Portable Network Graphics"),
    GIF("Graphics Interchange Format"),
    PDF("Acrobat -Portable document format"),
    TXT("ASCII text"),
    CVS("Comma separated, variable length file"),
    MP3("Music"),
    MP4("Video"),
    EXE("PC Application"),
    ZIP("Archive"),
    WAV("Windows sound"),
    TAR("Compressed archive"),
    HTML("Web page source text"),
    JAVA("Java files"),
    CLASS("Java files"),
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
            case ".jpeg" -> Type.JPEG;
            case ".png" -> Type.PNG;
            case ".gif" -> Type.GIF;
            case ".pdf" -> Type.PDF;
            case ".txt" -> Type.TXT;
            case ".cvs" -> Type.CVS;
            case ".mp3" -> Type.MP3;
            case ".mp4" -> Type.MP4;
            case ".exe" -> Type.EXE;
            case ".zip" -> Type.ZIP;
            case ".wav" -> Type.WAV;
            case ".tar" -> Type.TAR;
            case ".html" -> Type.HTML;
            case ".java" -> Type.JAVA;
            case ".class" -> Type.CLASS;
            case "folder" -> Type.FOLDER;
            default -> Type.OTHER;
        };
    }

    public String getDescription() {
        return description;
    }
}

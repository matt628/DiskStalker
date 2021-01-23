package pl.edu.agh.diskstalker.database.model;

public class Statistic {

    private final String extension;
    private final String description;
    private final long bytes;
    private final double percentage;

    public Statistic(String extension, String description, long bytes, double percentage) {
        this.extension = extension;
        this.description = description;
        this.bytes = bytes;
        this.percentage = percentage;
    }

    public String getExtension() {
        return extension;
    }

    public String getDescription() {
        return description;
    }

    public long getBytes() {
        return bytes;
    }

    public double getPercentage() {
        return percentage;
    }
}

package pl.edu.agh.diskstalker.database.model;

import java.util.Objects;

public class Type {

    private final String extension;
    private final String description;
    private final double bytes;
    private final double percentage;

    public Type(String extension, String description, double bytes, double percentage) {
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

    public double getBytes() {
        return bytes;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type type = (Type) o;
        return Double.compare(type.bytes, bytes) == 0 &&
                Double.compare(type.percentage, percentage) == 0 &&
                extension.equals(type.extension) &&
                description.equals(type.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extension, description, bytes, percentage);
    }
}

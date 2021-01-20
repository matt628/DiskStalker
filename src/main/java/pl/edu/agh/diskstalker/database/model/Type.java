package pl.edu.agh.diskstalker.database.model;

import java.util.Objects;

public class Type {

    private final int id;

    private final String extension;

    private final String description;

    private long bytes;

    private double percentage;

    public Type(int id, String extension, String description) {
        this.id = id;
        this.extension = extension;
        this.description = description;
    }

    public int getId() {
        return id;
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

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type type = (Type) o;
        return id == type.id &&
                extension.equals(type.extension) &&
                Objects.equals(description, type.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, extension, description);
    }
}
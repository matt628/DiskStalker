package pl.edu.agh.diskstalker.database.model;

import java.text.DecimalFormat;

import static pl.edu.agh.diskstalker.presenter.Converter.bytesToString;

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

    public long getSize() {
        return bytes;
    }

    public String getPercentage() {
        return percentage + "%";
    }
}

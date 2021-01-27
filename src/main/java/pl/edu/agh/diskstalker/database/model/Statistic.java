package pl.edu.agh.diskstalker.database.model;

import java.text.DecimalFormat;

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

    public String getSize() {
        double result = (double) bytes;
        if(bytes/(1024*1024*1024)  > 1){
            result /= (1024*1024*1024);
            return new DecimalFormat("##.##").format(result) + "GB";
        }else if(bytes/(1024*1024) > 1){
            result /= (1024*1024);
            return new DecimalFormat("##.##").format(result) + "MB";
        }else if(bytes/1024 > 1){
            result /= 1024;
            return new DecimalFormat("##.##").format(result) + "KB";
        }
        return bytes + "B";
    }

    public String getPercentage() {
        return percentage + "%";
    }
}

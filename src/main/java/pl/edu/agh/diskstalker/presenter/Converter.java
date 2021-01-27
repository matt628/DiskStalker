package pl.edu.agh.diskstalker.presenter;

import java.text.DecimalFormat;

public class Converter {
    public static String bytesToString(long bytes){
        double result = (double) bytes;
        if(bytes/(1024*1024*1024)  > 1){
            result /= (1024*1024*1024);
            return  new DecimalFormat("##.##").format(result) + "GB";
        }else if(bytes/(1024*1024) > 1){
            result /= (1024*1024);
            return  new DecimalFormat("##.##").format(result) + "MB";
        }else if(bytes/1024 > 1){
            result /= 1024;
            return  new DecimalFormat("##.##").format(result) + "KB";
        }
        return  bytes + "B";
    }

    public static long stringSizeToLong(String size, String unit) {
        size = size.replaceAll(",", ".");
        double maxSize = Double.parseDouble(size);
        maxSize = switch (unit) {
            case "GB" -> maxSize * 1073741824;
            case "MB" -> maxSize * 1048576;
            case "kB" -> maxSize * 1024;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
        return (long) maxSize;
    }
}

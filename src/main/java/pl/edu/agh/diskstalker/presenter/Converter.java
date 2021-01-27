package pl.edu.agh.diskstalker.presenter;

import java.text.DecimalFormat;

public class Converter {
    public static String bytesToString(long bytes){
        double result = (double) bytes;
        if(bytes/(1024*1024*1024)  > 1){
            result /= (1024*1024*1024);
            return " " + new DecimalFormat("##.##").format(result) + "GB";
        }else if(bytes/(1024*1024) > 1){
            result /= (1024*1024);
            return " " + new DecimalFormat("##.##").format(result) + "MB";
        }else if(bytes/1024 > 1){
            result /= 1024;
            return " " + new DecimalFormat("##.##").format(result) + "KB";
        }
        return " " + bytes + "B";
    }
}

package pl.edu.agh.diskstalker.controller;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class PopUpNotification {

    public void displayTray(String caption, String text) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(caption, text, MessageType.INFO);
    }
}

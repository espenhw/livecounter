package org.grumblesmurf.livecounter;

import com.melloware.jintellitype.JIntellitype;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class LiveCounter {
    static Preferences prefs = Preferences.userNodeForPackage(LiveCounter.class);

    public static void main(String[] args) throws Exception {
        JIntellitype.getInstance();

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Main frame = new Main(getPrefixText(), getFont(), getColor(), getHotkey(), getHotkeyGlobal());
        frame.setVisible(true);
    }

    private static boolean getHotkeyGlobal() {
        return prefs.getBoolean("HOTKEY_GLOBAL", false);
    }

    private static String getPrefixText() {
        return prefs.get("PREFIX", "Events:");
    }

    private static Font getFont() {
        String name = prefs.get("FONT_FAMILY", "Sans Serif");
        int style = prefs.getInt("FONT_STYLE", Font.PLAIN);
        int size = prefs.getInt("FONT_SIZE", 24);

        return new Font(name, style, size);
    }

    private static Color getColor() {
        try {
            return Color.decode(prefs.get("COLOR", "#ffffff"));
        } catch (NumberFormatException e) {
            return Color.WHITE;
        }
    }

    private static KeyStroke getHotkey() {
        return KeyStroke.getKeyStroke(prefs.get("HOTKEY", "released SPACE"));
    }

    public static void setSettings(String prefixText, Font font, Color color, KeyStroke hotkey, boolean global) {
        prefs.put("PREFIX", prefixText);

        prefs.put("FONT_FAMILY", font.getFamily());
        prefs.putInt("FONT_STYLE", font.getStyle());
        prefs.putInt("FONT_SIZE", font.getSize());

        prefs.put("COLOR", "#"+Integer.toHexString(color.getRGB() & 0xffffff));

        prefs.put("HOTKEY", hotkey.toString());
        prefs.putBoolean("HOTKEY_GLOBAL", global);
    }
}

package org.grumblesmurf.livecounter;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class Main extends JFrame implements HotkeyListener {
    private JPanel contentPane;
    private int value = 0;
    JLabel counter;
    JLabel prefix;

    KeyStroke hotkey;
    boolean global;
    Font font;
    Color color;

    private Set<JComponent> textElements = new HashSet<JComponent>();

    public Main(String prefixText, Font font, Color color, KeyStroke hotkey, boolean global) {
        super("LiveCounter");

        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        MouseAdapter showSettings = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Settings dialog = new Settings(Main.this);
                    dialog.setModal(true);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            }
        };
        contentPane.addMouseListener(showSettings);
        prefix.addMouseListener(showSettings);
        counter.addMouseListener(showSettings);

        textElements.add(prefix);
        textElements.add(counter);

        counter.setText(String.valueOf(value));

        setSettings(prefixText, font, color, hotkey, global);
    }

    void setHotkey(KeyStroke hotkey, boolean global) {
        contentPane.unregisterKeyboardAction(this.hotkey);
        JIntellitype.getInstance().unregisterHotKey(1);
        JIntellitype.getInstance().removeHotKeyListener(this);
        this.hotkey = hotkey;
        this.global = global;
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increment();
            }
        }, hotkey, JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (global) {
            JIntellitype.getInstance().registerSwingHotKey(1, hotkey.getModifiers(), hotkey.getKeyCode());
            JIntellitype.getInstance().addHotKeyListener(this);
        }
    }

    private void increment() {
        counter.setText(String.valueOf(++value));
        pack();
    }

    void setSettings(String prefixText, Font font, Color color, KeyStroke hotkey, boolean global) {
        this.color = color;
        this.font = font;

        prefix.setText(prefixText);
        for (JComponent c : textElements) {
            c.setFont(font);
            c.setForeground(color);
        }
        setHotkey(hotkey, global);
        LiveCounter.setSettings(prefixText, font, color, hotkey, global);
        pack();
    }

    @Override
    public void onHotKey(int identifier) {
        increment();
    }
}

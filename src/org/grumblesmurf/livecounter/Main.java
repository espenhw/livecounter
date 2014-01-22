package org.grumblesmurf.livecounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class Main extends JFrame {
    private JPanel contentPane;
    private int value = 0;
    JLabel counter;
    JLabel prefix;

    KeyStroke hotkey;
    Font font;
    Color color;

    private Set<JComponent> textElements = new HashSet<JComponent>();

    public Main(String prefixText, Font font, Color color, KeyStroke hotkey) {
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

        setSettings(prefixText, font, color, hotkey);
    }

    void setHotkey(KeyStroke hotkey) {
        contentPane.unregisterKeyboardAction(this.hotkey);
        this.hotkey = hotkey;
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter.setText(String.valueOf(++value));
                Main.this.pack();
            }
        }, hotkey, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    void setSettings(String prefixText, Font font, Color color, KeyStroke hotkey) {
        this.color = color;
        this.font = font;

        prefix.setText(prefixText);
        for (JComponent c : textElements) {
            c.setFont(font);
            c.setForeground(color);
        }
        setHotkey(hotkey);
        LiveCounter.setSettings(prefixText, font, color, hotkey);
        pack();
    }
}

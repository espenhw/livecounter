package org.grumblesmurf.livecounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings extends JDialog {
    private final Main caller;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField prefix;
    private JButton fontButton;
    private JTextField incrementKey;
    private JTextField color;
    private JCheckBox globalHotkey;
    private FontChooser fontChooser;

    private KeyStroke hotkey;
    private Color chosenColor;

    public Settings(final Main caller) {
        this.caller = caller;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        prefix.setText(caller.prefix.getText());

        chosenColor = caller.prefix.getForeground();
        color.setBackground(chosenColor);

        fontChooser = new FontChooser(caller.prefix.getFont());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        fontButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontChooser.setModal(true);
                fontChooser.pack();
                fontChooser.setVisible(true);
            }
        });

        hotkey = caller.hotkey;
        globalHotkey.setSelected(caller.global);
        incrementKey.setText(saneString(caller.hotkey));
        incrementKey.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    incrementKey.setText("-press-");
                    incrementKey.setEnabled(true);
                    incrementKey.requestFocusInWindow();
                    incrementKey.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            incrementKey.setEnabled(false);
                            hotkey = KeyStroke.getKeyStrokeForEvent(e);
                            incrementKey.setText(saneString(hotkey));
                            pack();
                            incrementKey.removeKeyListener(this);
                        }
                    });
                }
            }
        });
        color.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color c = JColorChooser.showDialog(Settings.this, "Choose a color", chosenColor);
                    if (c != null) {
                        chosenColor = c;
                        color.setBackground(c);
                    }
                }
            }
        });
    }

    private String saneString(KeyStroke hotkey) {
        return hotkey.toString().replace("released ", "");
    }

    private void onOK() {
        caller.setSettings(prefix.getText(), fontChooser.getSelectedFont(), chosenColor, hotkey, globalHotkey.isSelected());
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}

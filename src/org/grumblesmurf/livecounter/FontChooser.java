package org.grumblesmurf.livecounter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class FontChooser extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField sample;
    private JComboBox<String> fontName;
    private JComboBox<FontStyle> fontStyle;
    private JSpinner fontSize;

    private void createUIComponents() {
        fontName = new JComboBox<String>(new DefaultComboBoxModel<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        fontName.setRenderer(new FontNameRenderer());
        fontStyle = new JComboBox<FontStyle>(new DefaultComboBoxModel<FontStyle>(FontStyle.values()));
        fontStyle.setRenderer(new FontStyleRenderer());
    }

    private static class FontNameRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Font f = component.getFont();
            component.setFont(new Font(value.toString(), f.getStyle(), f.getSize()));
            return component;
        }
    }

    enum FontStyle {
        Plain(Font.PLAIN), Bold(Font.BOLD), Italic(Font.ITALIC), BoldItalic(Font.BOLD | Font.ITALIC);

        final int magic;

        FontStyle(int magic) {
            this.magic = magic;
        }


        public static FontStyle fromMagic(int magic) {
            for (FontStyle fs : values()) {
                if (fs.magic == magic) return fs;
            }
            throw new IllegalArgumentException("Unknown magic " + magic);
        }
    }

    private static class FontStyleRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Font f = component.getFont();
            component.setFont(new Font(f.getFamily(), ((FontStyle)value).magic, f.getSize()));
            return component;
        }
    }

    public FontChooser(Font start) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        fontName.setSelectedItem(start.getFamily());
        fontStyle.setSelectedItem(FontStyle.fromMagic(start.getStyle()));
        fontSize.setValue(start.getSize());

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
        Changed changed = new Changed();
        fontName.addActionListener(changed);
        fontStyle.addActionListener(changed);
        fontSize.addChangeListener(changed);
    }

    private class Changed implements ActionListener, ChangeListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateSample();
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            updateSample();
        }
    }

    private void updateSample() {
        sample.setFont(getSelectedFont());
        pack();
    }

    Font getSelectedFont() {
        return new Font(selection(fontName), selection(fontStyle).magic, (Integer)fontSize.getValue());
    }

    private <E> E selection(JComboBox<E> comboBox) {
        return comboBox.getItemAt(comboBox.getSelectedIndex());
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        FontChooser dialog = new FontChooser(new JTextField().getFont());
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
    }
}

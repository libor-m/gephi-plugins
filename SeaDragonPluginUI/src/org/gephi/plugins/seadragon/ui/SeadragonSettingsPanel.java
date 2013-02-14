/*
 * Gephi Seadragon Plugin
 *
 * Copyright 2010-2011 Gephi
 * Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 * Website : http://www.gephi.org
 * Licensed under Apache 2 License (http://www.apache.org/licenses/LICENSE-2.0)
 */
package org.gephi.plugins.seadragon.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import org.gephi.lib.validation.Multiple4NumberValidator;
import org.gephi.lib.validation.PositiveNumberValidator;
import org.gephi.plugins.seadragon.SeadragonExporter;
import org.netbeans.validation.api.builtin.Validators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

/**
 *
 * @author Mathieu Bastian
 */
public class SeadragonSettingsPanel extends javax.swing.JPanel {

    final String LAST_PATH = "SeadragonExporterUI_Last_Path";
    private File path;
    private SeadragonExporter exporter;

    public SeadragonSettingsPanel() {
        initComponents();

        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(pathTextField.getText());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(WindowManager.getDefault().getMainWindow());
                if (result == JFileChooser.APPROVE_OPTION) {
                    path = fileChooser.getSelectedFile();
                    pathTextField.setText(path.getAbsolutePath());
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelDirectory = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        header = new org.jdesktop.swingx.JXHeader();
        labelTileSize = new javax.swing.JLabel();
        tileSizeTextField = new javax.swing.JTextField();
        labelPx3 = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        labelWidth = new javax.swing.JLabel();
        widthTextField = new javax.swing.JTextField();
        heightTextField = new javax.swing.JTextField();
        labelPx = new javax.swing.JLabel();
        labelPx2 = new javax.swing.JLabel();
        labelMargins = new javax.swing.JLabel();
        marginTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        useWalkerCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        viewWidthTextField = new javax.swing.JTextField();
        viewHeightTextField = new javax.swing.JTextField();
        labelPx1 = new javax.swing.JLabel();
        labelPx4 = new javax.swing.JLabel();

        labelDirectory.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelDirectory.text")); // NOI18N

        browseButton.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.browseButton.text")); // NOI18N

        header.setDescription(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.header.description")); // NOI18N
        header.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/plugins/seadragon/ui/resources/logo.png"))); // NOI18N
        header.setTitle(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.header.title")); // NOI18N

        labelTileSize.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelTileSize.text")); // NOI18N

        tileSizeTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.tile size.text")); // NOI18N
        tileSizeTextField.setName("tile size"); // NOI18N

        labelPx3.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelPx3.text")); // NOI18N

        pathTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.path.text")); // NOI18N
        pathTextField.setName("path"); // NOI18N

        labelWidth.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelWidth.text")); // NOI18N

        widthTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.width.text")); // NOI18N
        widthTextField.setName("width"); // NOI18N

        heightTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.height.text")); // NOI18N
        heightTextField.setName("height"); // NOI18N

        labelPx.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelPx.text")); // NOI18N

        labelPx2.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelPx2.text")); // NOI18N

        labelMargins.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelMargins.text")); // NOI18N

        marginTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.margin.text")); // NOI18N
        marginTextField.setName("margin"); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.jLabel1.text")); // NOI18N

        useWalkerCheckBox.setSelected(true);
        useWalkerCheckBox.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.useWalkerCheckBox.text")); // NOI18N
        useWalkerCheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.useWalkerCheckBox.toolTipText")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.jLabel2.text")); // NOI18N

        viewWidthTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.viewWidthTextField.text")); // NOI18N
        viewWidthTextField.setName("width"); // NOI18N

        viewHeightTextField.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.viewHeightTextField.text")); // NOI18N
        viewHeightTextField.setName("height"); // NOI18N

        labelPx1.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelPx1.text")); // NOI18N

        labelPx4.setText(org.openide.util.NbBundle.getMessage(SeadragonSettingsPanel.class, "SeadragonSettingsPanel.labelPx4.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelDirectory)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelWidth)
                                    .addComponent(jLabel2))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(viewWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelPx1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(viewHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelPx4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelMargins)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(83, 83, 83)
                                                .addComponent(jLabel1))
                                            .addComponent(marginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelPx)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelPx2)
                                        .addGap(18, 18, 18)
                                        .addComponent(useWalkerCheckBox)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(labelTileSize)
                                .addGap(18, 18, 18)
                                .addComponent(tileSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(labelPx3))
                            .addComponent(pathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelDirectory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelWidth)
                    .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPx)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPx2)
                    .addComponent(useWalkerCheckBox))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMargins)
                    .addComponent(marginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(labelTileSize)
                    .addComponent(tileSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPx3))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelPx1)
                        .addComponent(viewHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelPx4))))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setup(SeadragonExporter exporter) {
        this.exporter = exporter;
        tileSizeTextField.setText(String.valueOf(exporter.getTileSize()));
        path = new File(NbPreferences.forModule(SeadragonExporterUI.class).get(LAST_PATH, System.getProperty("user.home")));
        pathTextField.setText(path.getAbsolutePath());

        //PDF
        widthTextField.setText(String.valueOf((int) exporter.getWidth()));
        heightTextField.setText(String.valueOf((int) exporter.getHeight()));
        marginTextField.setText(String.valueOf((int) exporter.getMargin()));
        viewHeightTextField.setText(String.valueOf(exporter.getViewHeight()));
        viewWidthTextField.setText(String.valueOf(exporter.getViewWidth()));
    }

    public void unsetup(boolean update) {
        if (update) {
            try {
                path = new File(pathTextField.getText());
            } catch (Exception e) {
            }
            NbPreferences.forModule(SeadragonExporterUI.class).put(LAST_PATH, path.getAbsolutePath());
            exporter.setPath(path);
            exporter.setWidth(Integer.parseInt(widthTextField.getText()));
            exporter.setHeight(Integer.parseInt(heightTextField.getText()));
            exporter.setTileSize(Integer.parseInt(tileSizeTextField.getText()));
            exporter.setMargin(Integer.parseInt(marginTextField.getText()));
            exporter.setWalker(useWalkerCheckBox.isSelected());
            exporter.setView(Integer.parseInt(viewHeightTextField.getText()), Integer.parseInt(viewWidthTextField.getText()));
        }
    }

    public static ValidationPanel createValidationPanel(SeadragonSettingsPanel innerPanel) {
        ValidationPanel validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(innerPanel);

        ValidationGroup group = validationPanel.getValidationGroup();
        group.add(innerPanel.tileSizeTextField, Validators.REQUIRE_NON_EMPTY_STRING,
                new Multiple4NumberValidator());

        group.add(innerPanel.widthTextField, Validators.REQUIRE_NON_EMPTY_STRING,
                new PositiveNumberValidator());
        group.add(innerPanel.heightTextField, Validators.REQUIRE_NON_EMPTY_STRING,
                new PositiveNumberValidator());

        // group.add(innerPanel.pathTextField, Validators.FILE_MUST_BE_DIRECTORY);

        //Margins
        group.add(innerPanel.marginTextField, Validators.REQUIRE_NON_EMPTY_STRING,
                Validators.REQUIRE_VALID_NUMBER);

        return validationPanel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private org.jdesktop.swingx.JXHeader header;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel labelDirectory;
    private javax.swing.JLabel labelMargins;
    private javax.swing.JLabel labelPx;
    private javax.swing.JLabel labelPx1;
    private javax.swing.JLabel labelPx2;
    private javax.swing.JLabel labelPx3;
    private javax.swing.JLabel labelPx4;
    private javax.swing.JLabel labelTileSize;
    private javax.swing.JLabel labelWidth;
    private javax.swing.JTextField marginTextField;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JTextField tileSizeTextField;
    private javax.swing.JCheckBox useWalkerCheckBox;
    private javax.swing.JTextField viewHeightTextField;
    private javax.swing.JTextField viewWidthTextField;
    private javax.swing.JTextField widthTextField;
    // End of variables declaration//GEN-END:variables
}

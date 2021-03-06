package no.uib.storbioinfogui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import no.uib.storbioinfogui.data.Project;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import no.uib.storbioinfogui.util.AlignedListCellRenderer;

/**
 * A dialog for creating a new Project.
 *
 * @author Harald Barsnes
 */
public class NewProjectDialog extends javax.swing.JDialog {

    /**
     * The StoreBioinfoGUI parent frame.
     */
    private StoreBioinfoGUI storeBioinfoGUI;

    /**
     * Creates a new NewProjectDialog.
     *
     * @param storeBioinfoGUI
     * @param modal
     */
    public NewProjectDialog(StoreBioinfoGUI storeBioinfoGUI, boolean modal) {
        super(storeBioinfoGUI, modal);
        initComponents();

        quotaIdComboBox.setRenderer(new AlignedListCellRenderer(SwingConstants.CENTER));
        quotaIdComboBox.setModel(new DefaultComboBoxModel(storeBioinfoGUI.getQuotaNames().toArray()));
        quotaIdComboBox.setSelectedIndex(0);

        this.storeBioinfoGUI = storeBioinfoGUI;

        setLocationRelativeTo(storeBioinfoGUI);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectDetailsPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        summaryLabel = new javax.swing.JLabel();
        summaryJTextField = new javax.swing.JTextField();
        descriptionJScrollPane = new javax.swing.JScrollPane();
        descriptionJTextArea = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        cancelJButton = new javax.swing.JButton();
        okJButton = new javax.swing.JButton();
        mandatoryLabel = new javax.swing.JLabel();
        quotaSelectionPanel = new javax.swing.JPanel();
        quotaLabel = new javax.swing.JLabel();
        quotaIdComboBox = new javax.swing.JComboBox();
        dataOwnerPanel = new javax.swing.JPanel();
        contactLabel = new javax.swing.JLabel();
        contactJTextField = new javax.swing.JTextField();
        eMailJTextField = new javax.swing.JTextField();
        phoneJTextField = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        eMailLabel = new javax.swing.JLabel();
        storeBioinfoOwnerLabel = new javax.swing.JLabel();
        storeBioinfoOwnerTextField = new javax.swing.JTextField();
        invitesPanel = new javax.swing.JPanel();
        mailInvitesLabel = new javax.swing.JLabel();
        userInvitesLabel = new javax.swing.JLabel();
        mailInvitesJScrollPane = new javax.swing.JScrollPane();
        mailInvitesJTextArea = new javax.swing.JTextArea();
        userInvitesJScrollPane = new javax.swing.JScrollPane();
        userInvitesJTextArea = new javax.swing.JTextArea();
        userInviteToDataOwnerCheckBox = new javax.swing.JCheckBox();
        mailInviteToDataOwnerCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Project");
        setResizable(false);

        projectDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Details"));

        nameLabel.setText("Name*");

        nameJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nameJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        nameJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameJTextFieldKeyReleased(evt);
            }
        });

        summaryLabel.setText("Summary*");

        summaryJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        summaryJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        summaryJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                summaryJTextFieldKeyReleased(evt);
            }
        });

        descriptionJTextArea.setColumns(5);
        descriptionJTextArea.setLineWrap(true);
        descriptionJTextArea.setRows(3);
        descriptionJTextArea.setWrapStyleWord(true);
        descriptionJTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                descriptionJTextAreaKeyReleased(evt);
            }
        });
        descriptionJScrollPane.setViewportView(descriptionJTextArea);

        descriptionLabel.setText("Description*");

        javax.swing.GroupLayout projectDetailsPanelLayout = new javax.swing.GroupLayout(projectDetailsPanel);
        projectDetailsPanel.setLayout(projectDetailsPanelLayout);
        projectDetailsPanelLayout.setHorizontalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(summaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameJTextField)
                    .addComponent(summaryJTextField)
                    .addComponent(descriptionJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        projectDetailsPanelLayout.setVerticalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(summaryLabel)
                    .addComponent(summaryJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelJButton.setText("Cancel");
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        okJButton.setText("OK");
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });
        okJButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                okJButtonKeyReleased(evt);
            }
        });

        mandatoryLabel.setFont(mandatoryLabel.getFont().deriveFont((mandatoryLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        mandatoryLabel.setText("* Mandatory fields.");

        quotaSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Quota Selection"));

        quotaLabel.setText("Quota*");

        quotaIdComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quotaIdComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout quotaSelectionPanelLayout = new javax.swing.GroupLayout(quotaSelectionPanel);
        quotaSelectionPanel.setLayout(quotaSelectionPanelLayout);
        quotaSelectionPanelLayout.setHorizontalGroup(
            quotaSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quotaSelectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quotaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quotaIdComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        quotaSelectionPanelLayout.setVerticalGroup(
            quotaSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quotaSelectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(quotaSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quotaLabel)
                    .addComponent(quotaIdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        dataOwnerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Owner"));

        contactLabel.setText("Contact*");

        contactJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        contactJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        contactJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                contactJTextFieldKeyReleased(evt);
            }
        });

        eMailJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        eMailJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        eMailJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                eMailJTextFieldKeyReleased(evt);
            }
        });

        phoneJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        phoneJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        phoneJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                phoneJTextFieldKeyReleased(evt);
            }
        });

        phoneLabel.setText("Phone*");

        eMailLabel.setText("E-mail*");

        storeBioinfoOwnerLabel.setText("StoreBioinfo User");

        storeBioinfoOwnerTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                storeBioinfoOwnerTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout dataOwnerPanelLayout = new javax.swing.GroupLayout(dataOwnerPanel);
        dataOwnerPanel.setLayout(dataOwnerPanelLayout);
        dataOwnerPanelLayout.setHorizontalGroup(
            dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataOwnerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contactLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataOwnerPanelLayout.createSequentialGroup()
                        .addComponent(contactJTextField)
                        .addGap(18, 18, 18)
                        .addComponent(storeBioinfoOwnerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(storeBioinfoOwnerTextField))
                    .addComponent(eMailJTextField)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        dataOwnerPanelLayout.setVerticalGroup(
            dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataOwnerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactLabel)
                    .addComponent(storeBioinfoOwnerLabel)
                    .addComponent(storeBioinfoOwnerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactJTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eMailLabel)
                    .addComponent(eMailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invitesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Invites"));

        mailInvitesLabel.setText("Mail Invites");

        userInvitesLabel.setText("User Invites");

        mailInvitesJTextArea.setColumns(5);
        mailInvitesJTextArea.setLineWrap(true);
        mailInvitesJTextArea.setRows(2);
        mailInvitesJTextArea.setWrapStyleWord(true);
        mailInvitesJTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mailInvitesJTextAreaKeyReleased(evt);
            }
        });
        mailInvitesJScrollPane.setViewportView(mailInvitesJTextArea);

        userInvitesJTextArea.setColumns(5);
        userInvitesJTextArea.setLineWrap(true);
        userInvitesJTextArea.setRows(2);
        userInvitesJTextArea.setWrapStyleWord(true);
        userInvitesJTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                userInvitesJTextAreaKeyReleased(evt);
            }
        });
        userInvitesJScrollPane.setViewportView(userInvitesJTextArea);

        userInviteToDataOwnerCheckBox.setText("Invite data owner");
        userInviteToDataOwnerCheckBox.setEnabled(false);
        userInviteToDataOwnerCheckBox.setIconTextGap(15);

        mailInviteToDataOwnerCheckBox.setText("Invite data owner");
        mailInviteToDataOwnerCheckBox.setEnabled(false);
        mailInviteToDataOwnerCheckBox.setIconTextGap(15);

        javax.swing.GroupLayout invitesPanelLayout = new javax.swing.GroupLayout(invitesPanel);
        invitesPanel.setLayout(invitesPanelLayout);
        invitesPanelLayout.setHorizontalGroup(
            invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, invitesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(invitesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userInviteToDataOwnerCheckBox)
                            .addComponent(mailInviteToDataOwnerCheckBox)))
                    .addGroup(invitesPanelLayout.createSequentialGroup()
                        .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userInvitesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mailInvitesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mailInvitesJScrollPane)
                            .addComponent(userInvitesJScrollPane))))
                .addContainerGap())
        );

        invitesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {mailInviteToDataOwnerCheckBox, userInviteToDataOwnerCheckBox});

        invitesPanelLayout.setVerticalGroup(
            invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invitesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(userInvitesLabel)
                    .addComponent(userInvitesJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userInviteToDataOwnerCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(invitesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(mailInvitesLabel)
                    .addComponent(mailInvitesJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mailInviteToDataOwnerCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataOwnerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quotaSelectionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(projectDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(mandatoryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 440, Short.MAX_VALUE)
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton))
                    .addComponent(invitesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quotaSelectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(dataOwnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invitesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton)
                    .addComponent(mandatoryLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog.
     *
     * @param evt
     */
    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Creates the new project and then closes the dialog.
     *
     * @param evt
     */
    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed

        if (nameJTextField.getText().lastIndexOf(" ") != -1) {
            JOptionPane.showMessageDialog(this, "Project name cannot contain spaces.", "Project Error", JOptionPane.INFORMATION_MESSAGE);
        } else {

            // validate the input
            if (!validateInput()) {
                JOptionPane.showMessageDialog(this, "All fields have to be filled in.", "Project Error", JOptionPane.INFORMATION_MESSAGE);
            } else {

                // check if project already exists
                if (storeBioinfoGUI.projectExists(nameJTextField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Project named \'" + nameJTextField.getText().trim() + "\' already exists!", "Project Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    this.setVisible(false);

                    // convert the invites to the correct format
                    ArrayList<String> mailInvites = new ArrayList<String>();
                    if (mailInvitesJTextArea.getText().length() > 0) {
                        String values[] = mailInvitesJTextArea.getText().split(","); // @TODO: add more error checking here?
                        mailInvites.addAll(Arrays.asList(values));
                    }
                    ArrayList<String> userInvites = new ArrayList<String>();
                    if (userInvitesJTextArea.getText().length() > 0) {
                        String values[] = userInvitesJTextArea.getText().split(","); // @TODO: add more error checking here?
                        userInvites.addAll(Arrays.asList(values));
                    }

                    Project project = new Project(storeBioinfoGUI.getQuotaId(quotaIdComboBox.getSelectedItem().toString()), nameJTextField.getText().trim(), contactJTextField.getText().trim(),
                            storeBioinfoOwnerTextField.getText().trim(), eMailJTextField.getText().trim(), phoneJTextField.getText().trim(), summaryJTextField.getText().trim(),
                            descriptionJTextArea.getText().trim(), userInvites, mailInvites, userInviteToDataOwnerCheckBox.isSelected() && userInviteToDataOwnerCheckBox.isEnabled(),
                            mailInviteToDataOwnerCheckBox.isSelected() && mailInviteToDataOwnerCheckBox.isEnabled(), "new"); // @TODO: "new" should not be hardcoded here if the editing of projects is supported!!
                    boolean created = storeBioinfoGUI.addProject(project);
                    this.dispose();
                }
            }
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    /**
     * Creates the new project and then closes the dialog.
     *
     * @param evt
     */
    private void okJButtonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okJButtonKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            okJButtonActionPerformed(null);
        }
    }//GEN-LAST:event_okJButtonKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void nameJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_nameJTextFieldKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void contactJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_contactJTextFieldKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void eMailJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eMailJTextFieldKeyReleased
        validateInput();
        mailInviteToDataOwnerCheckBox.setEnabled(eMailJTextField.getText().length() > 0);
    }//GEN-LAST:event_eMailJTextFieldKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void phoneJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_phoneJTextFieldKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void summaryJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_summaryJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_summaryJTextFieldKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void descriptionJTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionJTextAreaKeyReleased
        validateInput();
    }//GEN-LAST:event_descriptionJTextAreaKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void mailInvitesJTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mailInvitesJTextAreaKeyReleased
        validateInput();
    }//GEN-LAST:event_mailInvitesJTextAreaKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void userInvitesJTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userInvitesJTextAreaKeyReleased
        validateInput();
    }//GEN-LAST:event_userInvitesJTextAreaKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void quotaIdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quotaIdComboBoxActionPerformed
        validateInput();
    }//GEN-LAST:event_quotaIdComboBoxActionPerformed

    /**
     * Enable/disable the mail invite owner check box.
     *
     * @param evt
     */
    private void storeBioinfoOwnerTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_storeBioinfoOwnerTextFieldKeyReleased
        userInviteToDataOwnerCheckBox.setEnabled(storeBioinfoOwnerTextField.getText().length() > 0);
    }//GEN-LAST:event_storeBioinfoOwnerTextFieldKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JTextField contactJTextField;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JPanel dataOwnerPanel;
    private javax.swing.JScrollPane descriptionJScrollPane;
    private javax.swing.JTextArea descriptionJTextArea;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField eMailJTextField;
    private javax.swing.JLabel eMailLabel;
    private javax.swing.JPanel invitesPanel;
    private javax.swing.JCheckBox mailInviteToDataOwnerCheckBox;
    private javax.swing.JScrollPane mailInvitesJScrollPane;
    private javax.swing.JTextArea mailInvitesJTextArea;
    private javax.swing.JLabel mailInvitesLabel;
    private javax.swing.JLabel mandatoryLabel;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton okJButton;
    private javax.swing.JTextField phoneJTextField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JPanel projectDetailsPanel;
    private javax.swing.JComboBox quotaIdComboBox;
    private javax.swing.JLabel quotaLabel;
    private javax.swing.JPanel quotaSelectionPanel;
    private javax.swing.JLabel storeBioinfoOwnerLabel;
    private javax.swing.JTextField storeBioinfoOwnerTextField;
    private javax.swing.JTextField summaryJTextField;
    private javax.swing.JLabel summaryLabel;
    private javax.swing.JCheckBox userInviteToDataOwnerCheckBox;
    private javax.swing.JScrollPane userInvitesJScrollPane;
    private javax.swing.JTextArea userInvitesJTextArea;
    private javax.swing.JLabel userInvitesLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Enable/disable the OK button.
     *
     * @return true of validated
     */
    private boolean validateInput() {

        if (quotaIdComboBox.getSelectedItem() != null
                && quotaIdComboBox.getSelectedItem().toString().length() > 0
                && nameJTextField.getText().length() > 0
                && contactJTextField.getText().length() > 0
                && eMailJTextField.getText().length() > 0
                && phoneJTextField.getText().length() > 0
                && summaryJTextField.getText().length() > 0
                && descriptionJTextArea.getText().length() > 0) {

            // check the invites
            // @TODO: how to check the invite format?

            okJButton.setEnabled(true);
            return true;
        } else {
            okJButton.setEnabled(false);
            return false;
        }
    }
}

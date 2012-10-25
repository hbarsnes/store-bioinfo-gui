package no.uib.storbioinfogui;

import java.awt.Color;
import no.uib.jsparklines.renderers.*;
import no.uib.storbioinfogui.data.Project;
import no.uib.storbioinfogui.data.Dataset;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import no.uib.storbioinfogui.data.DataFolder;
import org.apache.commons.io.FileUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.uib.storbioinfogui.util.XMLUtility;
import org.w3c.dom.Element;

/**
 * The main GUI class. Also contains most of the methods.
 *
 * @author Harald Barsnes
 */
public class StoreBioinfoGUI extends javax.swing.JFrame implements ClipboardOwner {

    /**
     * The local mapped folder.
     */
    private File mappedFolder;
    /**
     * The local StorBioinfo folder containing the meta data.
     */
    private File storeBioinfoFolder;
    /**
     * The list of all projets.
     */
    private HashMap<String, File> allFiles;
    /**
     * The available quota names.
     */
    private ArrayList<String> quotaNames = new ArrayList<String>();
    /**
     * A map of the quota name to quota ids.
     */
    private HashMap<String, String> quotaNameToIdMap = new HashMap<String, String>();
    /**
     * A map of the quota name to quota id.
     */
    private HashMap<String, String> quotaIdToNameMap = new HashMap<String, String>();
    /**
     * The last selected folder.
     */
    private String lastSelectedFolder = "user.home";
    /**
     * The color used for the complete project status..
     */
    private Color sparklineColor = new Color(110, 196, 97);

    /**
     * Creates a new StoreBioinfoGUI frame.
     */
    public StoreBioinfoGUI() {
        initComponents();

        // set the title of the frame and add the icon
        setTitle("StoreBioinfoGUI " + getVersion());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/StoreBioinfo.png")));

        // the index column
        projectsTable.getColumn(" ").setMaxWidth(50);
        projectsTable.getColumn(" ").setMinWidth(50);
        datasetsTable.getColumn(" ").setMaxWidth(50);
        datasetsTable.getColumn(" ").setMinWidth(50);

        // the status column
        projectsTable.getColumn("  ").setMaxWidth(30);
        projectsTable.getColumn("  ").setMinWidth(30);
        datasetsTable.getColumn("  ").setMaxWidth(30);
        datasetsTable.getColumn("  ").setMinWidth(30);

        projectsTable.getTableHeader().setReorderingAllowed(false);
        datasetsTable.getTableHeader().setReorderingAllowed(false);

        projectsTable.setAutoCreateRowSorter(true);
        datasetsTable.setAutoCreateRowSorter(true);

        // make sure that the scroll panes are see-through
        localProjectsScrollPane.getViewport().setOpaque(false);
        dataSetsJScrollPane.getViewport().setOpaque(false);

        File jarFile = new File(getJarFilePath());
        File localPathFile = new File(jarFile, "config/local_path");
        File quotaIdsFile = new File(jarFile, "config/quota_ids");

        setTableProperties();

        try {
            FileReader r = new FileReader(localPathFile);
            BufferedReader br = new BufferedReader(r);

            String localFolderPath = br.readLine();
            mappedFolder = new File(localFolderPath);

            br.close();
            r.close();

            if (!mappedFolder.exists()) {
                JOptionPane.showMessageDialog(null, "Local folder \'" + localFolderPath + "\' not found! Closing program.", "Folder Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            storeBioinfoFolder = new File(mappedFolder, "StoreBioinfo");

            if (!new File(mappedFolder, "StoreBioinfo").exists()) {

                // create the StoreBioinfo meta data folder
                boolean success = storeBioinfoFolder.mkdir();

                if (!success) {
                    JOptionPane.showMessageDialog(null, "Failed to create StoreBioinfo folder \'" + new File(mappedFolder, "StoreBioinfo").getPath() + "\'! Closing program.",
                            "Folder Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }

            // set the last selected folder to the mapped drive
            lastSelectedFolder = localFolderPath;

            // get the quota ids
            r = new FileReader(quotaIdsFile);
            br = new BufferedReader(r);

            String line = br.readLine();

            while (line != null) {
                String values[] = line.split("\\t");
                quotaNames.add(values[0]);
                quotaNameToIdMap.put(values[0], values[1]);
                quotaIdToNameMap.put(values[1], values[0]);
                line = br.readLine();
            }

            br.close();
            r.close();

            updateProjectsList(null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up the table properties.
     */
    private void setTableProperties() {

        // set up the project status color map
        HashMap<Integer, Color> statusColorMap = new HashMap<Integer, Color>();
        statusColorMap.put(0, Color.YELLOW); // new
        statusColorMap.put(1, Color.CYAN); // in progress
        statusColorMap.put(2, sparklineColor); // complete
        statusColorMap.put(3, Color.RED); // failed
        statusColorMap.put(4, Color.BLUE); // archived
        statusColorMap.put(5, Color.ORANGE); // partial
        statusColorMap.put(-1, Color.LIGHT_GRAY); // unknown status

        // set up the project status tooltip map
        HashMap<Integer, String> statusTooltipMap = new HashMap<Integer, String>();
        statusTooltipMap.put(0, "New");
        statusTooltipMap.put(1, "In Progress");
        statusTooltipMap.put(2, "Complete");
        statusTooltipMap.put(3, "Failed");
        statusTooltipMap.put(4, "Archived");
        statusTooltipMap.put(5, "Partial");
        statusTooltipMap.put(-1, "Unknown");

        projectsTable.getColumn("  ").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(Color.YELLOW, statusColorMap, statusTooltipMap));
        datasetsTable.getColumn("  ").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(Color.YELLOW, statusColorMap, statusTooltipMap));
    }

    /**
     * Update the list of current projects.
     *
     * @param projectName the selected project name
     */
    private void updateProjectsList(String projectName) {

        File[] allLocalProjects = storeBioinfoFolder.listFiles();

        allFiles = new HashMap<String, File>();
        ArrayList<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < allLocalProjects.length; i++) {
            if (allLocalProjects[i].isDirectory()) {
                allFiles.put(allLocalProjects[i].getName(), allLocalProjects[i]);
                fileNames.add(allLocalProjects[i].getName());
            }
        }

        Collections.sort(fileNames);

        DefaultTableModel dm = (DefaultTableModel) projectsTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        datasetDescriptionTextArea.setText("");
        datasetsJLabel.setText("Datasets");

        int selectedRow = 0;

        for (int i = 0; i < fileNames.size(); i++) {

            // get the meta data
            Project tempProject = readProjectMetaData(fileNames.get(i));
            int numberOfDatasets = getNumberOfDatasets(tempProject);

            ((DefaultTableModel) projectsTable.getModel()).addRow(new Object[]{
                        projectsTable.getRowCount() + 1,
                        quotaIdToNameMap.get(tempProject.getQuotaId()),
                        fileNames.get(i),
                        tempProject.getContact(),
                        tempProject.getEMail(),
                        tempProject.getPhone(),
                        tempProject.getSummary(),
                        numberOfDatasets,
                        convertStatusToInteger(tempProject.getProjectStatus())
                    });

            if (projectName != null && projectName.equalsIgnoreCase(fileNames.get(i))) {
                selectedRow = projectsTable.getRowCount() - 1;
            }
        }

        removeProjectButton.setEnabled(projectsTable.getRowCount() > 0);

        // select the given project
        if (projectsTable.getRowCount() > 0) {
            projectsTable.setRowSelectionInterval(selectedRow, selectedRow);
            projectsTableMouseReleased(null);
            addDatasetJButton.setEnabled(true);
        }

        ((TitledBorder) localProjectsPanel.getBorder()).setTitle("Local Projects (" + projectsTable.getRowCount() + ")");
        localProjectsPanel.revalidate();
        localProjectsPanel.repaint();
    }

    /**
     * Returns the project status as an integer.
     *
     * @param status the current project status as text
     * @return the project status as an integer
     */
    private Integer convertStatusToInteger(String status) {

        if (status.equalsIgnoreCase("new")) {
            return 0;
        } else if (status.equalsIgnoreCase("in progress")) {
            return 1;
        } else if (status.equalsIgnoreCase("complete")) {
            return 2;
        } else if (status.equalsIgnoreCase("failed")) {
            return 3;
        } else if (status.equalsIgnoreCase("archived")) {
            return 4;
        } else if (status.equalsIgnoreCase("partial")) {
            return 5;
        } else {
            return -1; // unknown status
        }
    }

    /**
     * Returns the path to the jar file.
     *
     * @return the path to the jar file
     */
    public String getJarFilePath() {
        String path = this.getClass().getResource("StoreBioinfoGUI.class").getPath();
        if (path.lastIndexOf("/store-bioinfo-gui-") != -1) {
            path = path.substring(5, path.lastIndexOf("/store-bioinfo-gui-"));
            path = path.replace("%20", " ");
            path = path.replace("%5b", "[");
            path = path.replace("%5d", "]");

            if (System.getProperty("os.name").lastIndexOf("Windows") != -1) {
                path = path.replace("/", "\\");
            }
        } else {
            path = ".";
        }
        //path= "D:\\temp\\storebioinfo-gui-0.2.2"; //for debugging in netbeans
        return path;


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        localProjectsPanel = new javax.swing.JPanel();
        localProjectsScrollPane = new javax.swing.JScrollPane();
        projectsTable = new javax.swing.JTable();
        addProjectJButton = new javax.swing.JButton();
        removeProjectButton = new javax.swing.JButton();
        projectDetailsPanel = new javax.swing.JPanel();
        projectDescriptionScrollPane = new javax.swing.JScrollPane();
        projectDescriptionTextArea = new javax.swing.JTextArea();
        summaryLabel = new javax.swing.JLabel();
        projectSummaryJTextField = new javax.swing.JTextField();
        projectDescriptionLabel = new javax.swing.JLabel();
        foldersLabel = new javax.swing.JLabel();
        datatypesJComboBox = new javax.swing.JComboBox();
        copyJButton = new javax.swing.JButton();
        dataSetsJScrollPane = new javax.swing.JScrollPane();
        datasetsTable = new javax.swing.JTable();
        datasetDescriptionLabel = new javax.swing.JLabel();
        datasetDescriptionJScrollPane = new javax.swing.JScrollPane();
        datasetDescriptionTextArea = new javax.swing.JTextArea();
        datasetsJLabel = new javax.swing.JLabel();
        addDatasetJButton = new javax.swing.JButton();
        openJButton = new javax.swing.JButton();
        editDatasetJButton = new javax.swing.JButton();
        removeDatasetJButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StoreBioinfo");
        setBackground(new java.awt.Color(255, 255, 255));

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        localProjectsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Local Projects"));
        localProjectsPanel.setOpaque(false);

        projectsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Quota", "Name", "Contact", "E-mail", "Phone", "Summary", "Datasets", "  "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        projectsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                projectsTableMouseReleased(evt);
            }
        });
        projectsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                projectsTableKeyReleased(evt);
            }
        });
        localProjectsScrollPane.setViewportView(projectsTable);

        addProjectJButton.setText("Add");
        addProjectJButton.setToolTipText("Add a new project");
        addProjectJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProjectJButtonActionPerformed(evt);
            }
        });

        removeProjectButton.setText("Remove");
        removeProjectButton.setToolTipText("Remove the selected project");
        removeProjectButton.setEnabled(false);
        removeProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout localProjectsPanelLayout = new javax.swing.GroupLayout(localProjectsPanel);
        localProjectsPanel.setLayout(localProjectsPanelLayout);
        localProjectsPanelLayout.setHorizontalGroup(
            localProjectsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(localProjectsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(localProjectsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(localProjectsScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, localProjectsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addProjectJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeProjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        localProjectsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addProjectJButton, removeProjectButton});

        localProjectsPanelLayout.setVerticalGroup(
            localProjectsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(localProjectsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(localProjectsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(localProjectsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addProjectJButton)
                    .addComponent(removeProjectButton))
                .addContainerGap())
        );

        projectDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Details"));
        projectDetailsPanel.setOpaque(false);

        projectDescriptionTextArea.setColumns(20);
        projectDescriptionTextArea.setEditable(false);
        projectDescriptionTextArea.setLineWrap(true);
        projectDescriptionTextArea.setRows(2);
        projectDescriptionTextArea.setWrapStyleWord(true);
        projectDescriptionScrollPane.setViewportView(projectDescriptionTextArea);

        summaryLabel.setText("Summary:");

        projectSummaryJTextField.setEditable(false);
        projectSummaryJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        projectSummaryJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));

        projectDescriptionLabel.setText("Project Description");

        foldersLabel.setText("Folders:");

        datatypesJComboBox.setEnabled(false);

        copyJButton.setText("Copy");
        copyJButton.setToolTipText("Copy the folder path to the clipboard");
        copyJButton.setEnabled(false);
        copyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyJButtonActionPerformed(evt);
            }
        });

        datasetsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Name", "Description", "  "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datasetsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        datasetsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datasetsTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                datasetsTableMouseReleased(evt);
            }
        });
        datasetsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datasetsTableKeyReleased(evt);
            }
        });
        dataSetsJScrollPane.setViewportView(datasetsTable);

        datasetDescriptionLabel.setText("Dataset Description");

        datasetDescriptionTextArea.setColumns(20);
        datasetDescriptionTextArea.setEditable(false);
        datasetDescriptionTextArea.setLineWrap(true);
        datasetDescriptionTextArea.setRows(2);
        datasetDescriptionTextArea.setWrapStyleWord(true);
        datasetDescriptionJScrollPane.setViewportView(datasetDescriptionTextArea);

        datasetsJLabel.setText("Datasets");

        addDatasetJButton.setText("Add");
        addDatasetJButton.setToolTipText("Add a new dataset to the selected project");
        addDatasetJButton.setEnabled(false);
        addDatasetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatasetJButtonActionPerformed(evt);
            }
        });

        openJButton.setText("Open");
        openJButton.setToolTipText("Open the folder in a file browser");
        openJButton.setEnabled(false);
        openJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openJButtonActionPerformed(evt);
            }
        });

        editDatasetJButton.setText("Edit");
        editDatasetJButton.setToolTipText("Edit the selected dataset");
        editDatasetJButton.setEnabled(false);
        editDatasetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDatasetJButtonActionPerformed(evt);
            }
        });

        removeDatasetJButton.setText("Remove");
        removeDatasetJButton.setToolTipText("Remove the selected dataset");
        removeDatasetJButton.setEnabled(false);
        removeDatasetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDatasetJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout projectDetailsPanelLayout = new javax.swing.GroupLayout(projectDetailsPanel);
        projectDetailsPanel.setLayout(projectDetailsPanelLayout);
        projectDetailsPanelLayout.setHorizontalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                        .addComponent(summaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(projectSummaryJTextField))
                    .addComponent(dataSetsJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
                    .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                        .addComponent(foldersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datatypesJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copyJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, projectDetailsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addDatasetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editDatasetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeDatasetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(projectDescriptionScrollPane)
                    .addComponent(datasetDescriptionJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                        .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(projectDescriptionLabel)
                            .addComponent(datasetsJLabel)
                            .addComponent(datasetDescriptionLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        projectDetailsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {copyJButton, openJButton});

        projectDetailsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addDatasetJButton, editDatasetJButton, removeDatasetJButton});

        projectDetailsPanelLayout.setVerticalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(summaryLabel)
                    .addComponent(projectSummaryJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(projectDescriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectDescriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(datasetsJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataSetsJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDatasetJButton)
                    .addComponent(editDatasetJButton)
                    .addComponent(removeDatasetJButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datasetDescriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datasetDescriptionJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datatypesJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copyJButton)
                    .addComponent(foldersLabel)
                    .addComponent(openJButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(localProjectsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(projectDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(localProjectsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        exitMenuItem.setMnemonic('X');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setMnemonic('H');
        helpMenuItem.setText("Help Content");
        helpMenuItem.setToolTipText("C");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Copies the current folder path to the clipboard.
     *
     * @param evt
     */
    private void copyJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyJButtonActionPerformed

        DataFolder selectedFolder = (DataFolder) datatypesJComboBox.getSelectedItem();
        String tempPath = new File(mappedFolder, selectedFolder.getRelativeFolderPath()).getAbsolutePath();

        StringSelection stringSelection = new StringSelection(tempPath);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);

        JOptionPane.showMessageDialog(this, "Path copied to clipboard.", "Copied to Clipboard", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_copyJButtonActionPerformed

    /**
     * Opens the current folder in a file browser.
     *
     * @param evt
     */
    private void openJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openJButtonActionPerformed
        DataFolder selectedFolder = (DataFolder) datatypesJComboBox.getSelectedItem();
        String tempPath = new File(mappedFolder, selectedFolder.getRelativeFolderPath()).getAbsolutePath();

        try {
            Runtime.getRuntime().exec("explorer " + tempPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_openJButtonActionPerformed

    /**
     * Add a new dataset.
     *
     * @param evt
     */
    private void addDatasetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatasetJButtonActionPerformed

        int selectedRow = projectsTable.getSelectedRow();

        if (selectedRow != -1) {
            String selectedProjectName = (String) projectsTable.getValueAt(selectedRow, projectsTable.getColumn("Name").getModelIndex());
            new NewDatasetDialog(this, selectedProjectName, true);
        }
    }//GEN-LAST:event_addDatasetJButtonActionPerformed

    /**
     * Add a new project.
     *
     * @param evt
     */
    private void addProjectJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProjectJButtonActionPerformed
        new NewProjectDialog(this, true);
    }//GEN-LAST:event_addProjectJButtonActionPerformed

    /**
     * Updates the dataset display.
     *
     * @param evt
     */
    private void projectsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectsTableMouseReleased

        int row = projectsTable.getSelectedRow();

        if (row != -1) {
            Project tempProject = readProjectMetaData((String) projectsTable.getValueAt(row, projectsTable.getColumn("Name").getModelIndex()));
            projectSummaryJTextField.setText(tempProject.getSummary());
            projectDescriptionTextArea.setText(tempProject.getDescription());
            projectDescriptionTextArea.setCaretPosition(0);

            ((TitledBorder) projectDetailsPanel.getBorder()).setTitle("Project Details - " + tempProject.getName());
            projectDetailsPanel.revalidate();
            projectDetailsPanel.repaint();

            ArrayList<Dataset> datasets = getDatasets(tempProject);
            updateDatasetTable(datasets, tempProject.getName());
        }

        removeProjectButton.setEnabled(row != -1);
    }//GEN-LAST:event_projectsTableMouseReleased

    /**
     * Closes the tool.
     *
     * @param evt
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * @see #projectsTableMouseReleased(java.awt.event.MouseEvent)
     */
    private void projectsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectsTableKeyReleased
        projectsTableMouseReleased(null);
    }//GEN-LAST:event_projectsTableKeyReleased

    /**
     * Updates the information displayed about the selected dataset.
     *
     * @param evt
     */
    private void datasetsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datasetsTableMouseReleased

        int row = datasetsTable.getSelectedRow();

        if (row != -1) {

            String projectName = (String) projectsTable.getValueAt(projectsTable.getSelectedRow(), projectsTable.getColumn("Name").getModelIndex());
            String datasetName = (String) datasetsTable.getValueAt(row, datasetsTable.getColumn("Name").getModelIndex());

            String datasetDescription = readDatasetDescription(projectName, datasetName);
            datasetDescriptionTextArea.setText(datasetDescription);
            datasetDescriptionTextArea.setCaretPosition(0);

            Vector<DataFolder> folders = new Vector<DataFolder>();

            ArrayList<DataFolder> dataFolders = getDatasetDataFolders(projectName, datasetName);

            // iterate the data folders for the dataset
            for (int j = 0; j < dataFolders.size(); j++) {
                DataFolder dataFolder = dataFolders.get(j);
                folders.add(dataFolder);
            }

            datatypesJComboBox.setModel(new DefaultComboBoxModel(folders));
            datatypesJComboBox.setEnabled(true);
            openJButton.setEnabled(true);
            copyJButton.setEnabled(true);
        } else {
            datatypesJComboBox.setModel(new DefaultComboBoxModel());
            datatypesJComboBox.setEnabled(false);
            openJButton.setEnabled(false);
            copyJButton.setEnabled(false);
        }

        editDatasetJButton.setEnabled(row != -1);
        removeDatasetJButton.setEnabled(row != -1);
    }//GEN-LAST:event_datasetsTableMouseReleased

    /**
     * @see #datasetsTableMouseReleased(java.awt.event.MouseEvent)
     */
    private void datasetsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_datasetsTableKeyReleased
        datasetsTableMouseReleased(null);
    }//GEN-LAST:event_datasetsTableKeyReleased

    /**
     * Opens the about page.
     *
     * @param evt
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        new HelpDialog(this, getClass().getResource("/helpFiles/AboutStoreBioinfoGUI.html"));
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * Opens the help dialog.
     *
     * @param evt
     */
    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        new HelpDialog(this, getClass().getResource("/helpFiles/StoreBioinfoHelp.html"));
    }//GEN-LAST:event_helpMenuItemActionPerformed

    /**
     * Remove the selected project.
     *
     * @param evt
     */
    private void removeProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProjectButtonActionPerformed

        int selection = JOptionPane.showConfirmDialog(this, "This will remove the project and all related datasets."
                + "\nNote that this will not delete data already moved to StoreBioinfo."
                + "\n\nContinue?",
                "Remove Project?", JOptionPane.YES_NO_CANCEL_OPTION);

        if (selection == JOptionPane.YES_OPTION) {

            int selectedRow = projectsTable.getSelectedRow();
            String selectedProjectName = (String) projectsTable.getValueAt(selectedRow, projectsTable.getColumn("Name").getModelIndex());

            File projectFolder = new File(storeBioinfoFolder, selectedProjectName);

            try {
                FileUtils.deleteDirectory(projectFolder);
                projectSummaryJTextField.setText("");
                projectDescriptionTextArea.setText("");
                ((TitledBorder) projectDetailsPanel.getBorder()).setTitle("Project Details");

                // reset dataset table
                while (datasetsTable.getRowCount() > 0) {
                    ((DefaultTableModel) datasetsTable.getModel()).removeRow(0);
                }

                removeDatasetJButton.setEnabled(datasetsTable.getRowCount() > 0);

                updateProjectsList(null);
                JOptionPane.showMessageDialog(this, "Project successfully removed.", "Project Removed.", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to remove project. Try removing the proejct folder manually.", "Project Removal Failed.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_removeProjectButtonActionPerformed

    /**
     * Remove the selected dataset.
     *
     * @param evt
     */
    private void removeDatasetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDatasetJButtonActionPerformed
        int selection = JOptionPane.showConfirmDialog(this, "This will remove the selected dataset."
                + "\nNote that this will not delete data already moved to StoreBioinfo."
                + "\n\nContinue?",
                "Remove Dataset?", JOptionPane.YES_NO_CANCEL_OPTION);

        if (selection == JOptionPane.YES_OPTION) {

            int selectedProjectsRow = projectsTable.getSelectedRow();
            String selectedProjectName = (String) projectsTable.getValueAt(selectedProjectsRow, projectsTable.getColumn("Name").getModelIndex());

            int selectedDatasetRow = datasetsTable.getSelectedRow();
            String selectedDatasetName = (String) datasetsTable.getValueAt(selectedDatasetRow, datasetsTable.getColumn("Name").getModelIndex());

            File projectFolder = new File(storeBioinfoFolder, selectedProjectName);
            File datasetFolder = new File(projectFolder, selectedDatasetName);

            try {
                FileUtils.deleteDirectory(datasetFolder);
                updateProjectsList(selectedProjectName);
                JOptionPane.showMessageDialog(this, "Dataset successfully removed.", "Dataset Removed.", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to remove dataset. Try removing the dataset folder manually.", "Dataset Removal Failed.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_removeDatasetJButtonActionPerformed

    /**
     * Edit the selected dataset.
     *
     * @param evt
     */
    private void editDatasetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDatasetJButtonActionPerformed

        int selectedProjectsRow = projectsTable.getSelectedRow();
        String selectedProjectName = (String) projectsTable.getValueAt(selectedProjectsRow, projectsTable.getColumn("Name").getModelIndex());

        int selectedDatasetRow = datasetsTable.getSelectedRow();
        String selectedDatasetName = (String) datasetsTable.getValueAt(selectedDatasetRow, datasetsTable.getColumn("Name").getModelIndex());

        Dataset tempDataset = new Dataset(selectedDatasetName,
                readDatasetDescription(selectedProjectName, selectedDatasetName),
                selectedProjectName,
                getDatasetDataFolders(selectedProjectName, selectedDatasetName),
                getDatasetStatus(selectedProjectName, selectedDatasetName));

        new NewDatasetDialog(this, tempDataset, true);
    }//GEN-LAST:event_editDatasetJButtonActionPerformed

    /**
     * Double clicking in the dataset dialog opens the edit dataset dialog.
     *
     * @param evt
     */
    private void datasetsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datasetsTableMouseClicked
        if (datasetsTable.getSelectedRow() != -1 && evt.getButton() == 1 && evt.getClickCount() == 2) {
            editDatasetJButtonActionPerformed(null);
        }
    }//GEN-LAST:event_datasetsTableMouseClicked

    /**
     * Updates the dataset table.
     *
     * @param datasets
     * @param projectName
     */
    public void updateDatasetTable(ArrayList<Dataset> datasets, String projectName) {

        ArrayList<String> datasetTypes = getDatasetTypes();

        // reset the table model
        datasetsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    " ", "Name", "Description"
                }) {

            public Class getColumnClass(int columnIndex) {

                switch (columnIndex) {
                    case 1:
                        return java.lang.String.class;
                    case 2:
                        return java.lang.String.class;
                    default:
                        return java.lang.Integer.class;
                }
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        ((DefaultTableModel) datasetsTable.getModel()).fireTableDataChanged();

        // add the dataset type columns
        for (int i = 0; i < datasetTypes.size(); i++) {
            ((DefaultTableModel) datasetsTable.getModel()).addColumn(datasetTypes.get(i));
        }

        // add the status column
        ((DefaultTableModel) datasetsTable.getModel()).addColumn("  ");

        // set the column widths for the dataset type columns
        for (int i = 0; i < datasetTypes.size(); i++) {
            datasetsTable.getColumn(datasetTypes.get(i)).setMinWidth(80);
            datasetsTable.getColumn(datasetTypes.get(i)).setMaxWidth(80);
        }

        datasetsTable.getColumn(" ").setMaxWidth(50);
        datasetsTable.getColumn(" ").setMinWidth(50);
        datasetsTable.getColumn("  ").setMaxWidth(30);
        datasetsTable.getColumn("  ").setMinWidth(30);

        datasetDescriptionTextArea.setText("");
        datasetsJLabel.setText("Datasets");

        Collections.sort(datasets);

        // add the datasets
        for (int i = 0; i < datasets.size(); i++) {
            ((DefaultTableModel) datasetsTable.getModel()).addRow(new Object[]{
                        (i + 1),
                        datasets.get(i).getName(),
                        datasets.get(i).getDescription()
                    });
        }


        // iterate the datasets
        for (int i = 0; i < datasetsTable.getRowCount(); i++) {

            ArrayList<DataFolder> dataFolders = getDatasetDataFolders(projectName, datasets.get(i).getName());
            HashMap<String, Integer> dataFileCounter = new HashMap<String, Integer>();

            // iterate the data folders for the dataset
            for (int j = 0; j < dataFolders.size(); j++) {

                DataFolder dataFolder = dataFolders.get(j);

                File tempDataFolder = new File(mappedFolder, dataFolder.getRelativeFolderPath());

                // get the number of files in the given data folder
                if (dataFileCounter.containsKey(dataFolder.getDataType())) {
                    Integer count = dataFileCounter.get(dataFolder.getDataType());
                    count += tempDataFolder.listFiles().length;
                    dataFileCounter.put(dataFolder.getDataType(), ++count);
                } else {
                    dataFileCounter.put(dataFolder.getDataType(), tempDataFolder.listFiles().length);
                }
            }

            // add the number of files of each dataset type
            for (int j = 0; j < getDatasetTypes().size(); j++) {
                String dataType = getDatasetTypes().get(j);

                Integer counter = dataFileCounter.get(dataType);

                if (dataFileCounter.get(dataType) == null) {
                    counter = 0;
                }

                datasetsTable.setValueAt(counter, i, datasetsTable.getColumn(dataType).getModelIndex());
            }
        }

        // add the status values
        for (int i = 0; i < datasets.size(); i++) {
            datasetsTable.setValueAt(convertStatusToInteger(datasets.get(i).getDatasetStatus()), i, datasetsTable.getColumn("  ").getModelIndex());
        }

        setTableProperties();

        datatypesJComboBox.setModel(new DefaultComboBoxModel());
        datatypesJComboBox.setEnabled(false);
        openJButton.setEnabled(false);
        copyJButton.setEnabled(false);

        removeDatasetJButton.setEnabled(datasetsTable.getRowCount() > 0);

        if (datasetsTable.getRowCount() > 0) {
            datasetsJLabel.setText("Datasets (" + datasetsTable.getRowCount() + ")");
            datasetsTable.setRowSelectionInterval(0, 0);
            datasetsTableMouseReleased(null);
        }
    }

    /**
     * The main method used to start the tool.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StoreBioinfoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreBioinfoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreBioinfoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreBioinfoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                StoreBioinfoGUI storeBioinfoGUI = new StoreBioinfoGUI();
                storeBioinfoGUI.setLocationRelativeTo(null);
                storeBioinfoGUI.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addDatasetJButton;
    private javax.swing.JButton addProjectJButton;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton copyJButton;
    private javax.swing.JScrollPane dataSetsJScrollPane;
    private javax.swing.JScrollPane datasetDescriptionJScrollPane;
    private javax.swing.JLabel datasetDescriptionLabel;
    private javax.swing.JTextArea datasetDescriptionTextArea;
    private javax.swing.JLabel datasetsJLabel;
    private javax.swing.JTable datasetsTable;
    private javax.swing.JComboBox datatypesJComboBox;
    private javax.swing.JButton editDatasetJButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel foldersLabel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JPanel localProjectsPanel;
    private javax.swing.JScrollPane localProjectsScrollPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton openJButton;
    private javax.swing.JLabel projectDescriptionLabel;
    private javax.swing.JScrollPane projectDescriptionScrollPane;
    private javax.swing.JTextArea projectDescriptionTextArea;
    private javax.swing.JPanel projectDetailsPanel;
    private javax.swing.JTextField projectSummaryJTextField;
    private javax.swing.JTable projectsTable;
    private javax.swing.JButton removeDatasetJButton;
    private javax.swing.JButton removeProjectButton;
    private javax.swing.JLabel summaryLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns true if the project exists.
     *
     * @param projectName the project name
     * @return true if the project exists
     */
    public boolean projectExists(String projectName) {
        return new File(storeBioinfoFolder, projectName).exists();
    }

    /**
     * Returns true if the dataset already exists.
     *
     * @param projectName the project name
     * @param datasetName the dataset name
     * @return true if the dataset already exists
     */
    public boolean datasetExists(String projectName, String datasetName) {
        File projectFolder = new File(storeBioinfoFolder, projectName);
        File datasetFolder = new File(projectFolder, datasetName);
        return datasetFolder.exists();
    }

    /**
     * Add a new project.
     *
     * @param project
     * @return true if the project was created without errors
     */
    public boolean addProject(Project project) {

        try {
            if (!new File(storeBioinfoFolder, project.getName()).exists()) {

                // create the project
                boolean created = new File(storeBioinfoFolder, project.getName()).mkdir();

                if (created) {

                    // create the metaDataXML file
                    boolean metaDataAdded = createProjectMetaData(project, false);

                    if (metaDataAdded) {
                        // project created
                        updateProjectsList(project.getName());

                        JOptionPane.showMessageDialog(this, "Project \'" + project.getName() + "\' created and selected.", "Project Created", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Project meta data could not be created!", "Project Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Project could not be created!", "Project Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Project already exists!", "Project Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Create the project meta data.
     *
     * @param project
     * @param overwrite if true the old project will be overwritten
     * @return the project meta data
     */
    private boolean createProjectMetaData(Project project, boolean overwrite) {

        if (new File(storeBioinfoFolder, project.getName()).exists() || overwrite) {

            File projectFolder = new File(storeBioinfoFolder, project.getName());

            try {
                File metaFile = new File(projectFolder, "meta.xml");

                boolean created = true;

                if (!overwrite) {
                    created = new File(projectFolder, "meta.xml").createNewFile();
                }

                if (created) {

                    FileWriter f = new FileWriter(metaFile);
                    BufferedWriter bw = new BufferedWriter(f);

                    String meta = "<project status=\"" + project.getProjectStatus() + "\">" + "\n"
                            + "\t<quota_id>" + project.getQuotaId() + "</quota_id>" + "\n"
                            + "\t<description>" + project.getDescription() + "</description>" + "\n"
                            + "\t<summary>" + project.getSummary() + "</summary>" + "\n"
                            + "\t<pi_name>" + project.getContact() + "</pi_name>" + "\n"
                            + "\t<pi_user_name>" + project.getOwnerUserName() + "</pi_user_name>" + "\n"
                            + "\t<pi_email>" + project.getEMail() + "</pi_email>" + "\n"
                            + "\t<pi_telephone>" + project.getPhone() + "</pi_telephone>" + "\n";

                    if (project.getMailInvites().size() > 0 || project.isSendMailInviteToDataOwner()) {
                        meta += "\t<mail_invites>" + "\n";
                        for (String mailInvite : project.getMailInvites()) {
                            meta += "\t\t<mail_invite role=\"user\">" + mailInvite + "</mail_invite>" + "\n";
                        }

                        if (project.isSendMailInviteToDataOwner()) {
                            meta += "\t\t<mail_invite role=\"pi\">" + project.getEMail() + "</mail_invite>" + "\n";
                        }

                        meta += "\t</mail_invites>" + "\n";
                    }



                    if (project.getUserInvites().size() > 0 || project.isSendUserInviteToDataOwner()) {
                        meta += "\t<user_invites>" + "\n";
                        for (String userInvite : project.getUserInvites()) {
                            meta += "\t\t<user_invite role=\"user\">" + userInvite + "</user_invite>" + "\n";
                        }

                        if (project.isSendUserInviteToDataOwner()) {
                            meta += "\t\t<user_invite role=\"pi\">" + project.getOwnerUserName() + "</user_invite>" + "\n";
                        }

                        meta += "\t</user_invites>" + "\n";
                    }

                    meta += "</project>";

                    bw.write(meta);
                    bw.close();
                    f.close();

                    return true;
                } else {
                    // @TODO: show error message
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * Get all datasets for the given project.
     *
     * @param project
     * @return all datasets for the given project
     */
    private ArrayList<Dataset> getDatasets(Project project) {

        ArrayList<Dataset> datasets = new ArrayList<Dataset>();

        File projectFolder = new File(storeBioinfoFolder, project.getName());

        if (!projectFolder.exists()) {
            JOptionPane.showMessageDialog(this, "Project \'" + project.getName() + "\' not found!", "Project Error", JOptionPane.ERROR_MESSAGE);
            return datasets;
        }

        File[] datasetFolders = projectFolder.listFiles();

        for (int i = 0; i < datasetFolders.length; i++) {
            if (datasetFolders[i].isDirectory()) {
                datasets.add(new Dataset(datasetFolders[i].getName(),
                        readDatasetDescription(project.getName(), datasetFolders[i].getName()),
                        project.getName(),
                        getDatasetDataFolders(project.getName(), datasetFolders[i].getName()),
                        getDatasetStatus(project.getName(), datasetFolders[i].getName())));
            }
        }

        return datasets;
    }

    /**
     * Get the dataset description.
     *
     * @param projectName
     * @param datasetName
     * @return the dataset description
     */
    private String readDatasetDescription(String projectName, String datasetName) {

        String description;

        File projectFolder = new File(storeBioinfoFolder, projectName);
        File datasetFolder = new File(projectFolder, datasetName);
        File metaFile = new File(datasetFolder, "meta.xml");

        // @TODO: replace by propper xml parsing!!

        try {
            FileReader r = new FileReader(metaFile);
            BufferedReader br = new BufferedReader(r);

            br.readLine(); // skip the dataset line
            description = br.readLine();
            description = description.substring(description.indexOf("<description>") + "<description>".length(),
                    description.indexOf("</description>"));

            br.close();
            r.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return description;
    }

    /**
     * Get the dataset data folders.
     *
     * @param projectName
     * @param datasetName
     * @return the dataset description
     */
    private ArrayList<DataFolder> getDatasetDataFolders(String projectName, String datasetName) {

        ArrayList<DataFolder> dataFolders = new ArrayList<DataFolder>();

        File projectFolder = new File(storeBioinfoFolder, projectName);
        File datasetFolder = new File(projectFolder, datasetName);
        File metaFile = new File(datasetFolder, "meta.xml");

        try {
            Document doc = XMLUtility.parseXml(metaFile.getPath());
            //get the root
            NodeList nodes = doc.getElementsByTagName("datafolder");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) node;
                    String path = XMLUtility.getTagValue("path", elm);
                    String type = XMLUtility.getTagValue("type", elm);
                    dataFolders.add(new DataFolder(path, type));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dataFolders;
    }

    /**
     * Read the project meta data.
     *
     * @param projectName
     * @return the project with the meta data included
     */
    private Project readProjectMetaData(String projectName) {

        if (new File(storeBioinfoFolder, projectName).exists()) {

            try {

                File projectFolder = new File(storeBioinfoFolder, projectName);
                File metaFile = new File(projectFolder, "meta.xml");

                // @TODO: replace by propper xml parsing!!

                FileReader r = new FileReader(metaFile);
                BufferedReader br = new BufferedReader(r);

                String project = br.readLine();
                String quotaId = br.readLine();
                String description = br.readLine();
                String summary = br.readLine();
                String pi_name = br.readLine();
                String pi_user_name = br.readLine();
                String pi_email = br.readLine();
                String pi_telephone = br.readLine();

                // get the mail and user invites
                ArrayList<String> mailInvites = new ArrayList<String>();
                ArrayList<String> userInvites = new ArrayList<String>();
                boolean maillInviteOwner = false;
                boolean userInviteOwner = false;

                String temp = br.readLine();

                if (temp != null && temp.contains("mail_invites")) {
                    String mailInvite = br.readLine();
                    while (!mailInvite.contains("mail_invites")) {
                        String role = mailInvite.substring(mailInvite.indexOf("=\"") + 2, mailInvite.indexOf("\">"));
                        String tempMailInvite = mailInvite.substring(mailInvite.indexOf("\">") + 2, mailInvite.indexOf("</mail_invite>"));

                        if (!role.equalsIgnoreCase("pi")) {
                            mailInvites.add(tempMailInvite);
                        } else {
                            maillInviteOwner = true;
                        }

                        mailInvite = br.readLine();
                    }
                }

                temp = br.readLine();

                if (temp != null && temp.contains("user_invites")) {
                    String userInvite = br.readLine();
                    while (!userInvite.contains("user_invites")) {
                        String role = userInvite.substring(userInvite.indexOf("=\"") + 2, userInvite.indexOf("\">"));
                        String tempUserInvite = userInvite.substring(userInvite.indexOf("\">") + 2, userInvite.indexOf("</user_invite>"));

                        if (!role.equalsIgnoreCase("pi")) {
                            userInvites.add(tempUserInvite);
                        } else {
                            userInviteOwner = true;
                        }

                        userInvite = br.readLine();
                    }
                }

                // special case for pi_user_name as this can appear as '<pi_user_name/>'
                if (pi_user_name.indexOf("</pi_user_name>") != -1) {
                    pi_user_name = pi_user_name.substring(pi_user_name.indexOf("<pi_user_name>") + "<pi_user_name>".length(),
                            pi_user_name.indexOf("</pi_user_name>"));
                } else {
                    pi_user_name = "";
                }

                Project newProject = new Project(
                        quotaId.substring(quotaId.indexOf("<quota_id>") + "<quota_id>".length(),
                        quotaId.indexOf("</quota_id>")),
                        projectName,
                        pi_name.substring(pi_name.indexOf("<pi_name>") + "<pi_name>".length(),
                        pi_name.indexOf("</pi_name>")),
                        pi_user_name,
                        pi_email.substring(pi_email.indexOf("<pi_email>") + "<pi_email>".length(),
                        pi_email.indexOf("</pi_email>")),
                        pi_telephone.substring(pi_telephone.indexOf("<pi_telephone>") + "<pi_telephone>".length(),
                        pi_telephone.indexOf("</pi_telephone>")),
                        summary.substring(summary.indexOf("<summary>") + "<summary>".length(),
                        summary.indexOf("</summary>")),
                        description.substring(description.indexOf("<description>") + "<description>".length(),
                        description.indexOf("</description>")),
                        mailInvites, userInvites, maillInviteOwner, userInviteOwner,
                        project.substring(project.indexOf("=") + 2,
                        project.indexOf("\">")));

                br.close();
                r.close();

                return newProject;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    /**
     * Get the dataset status.
     *
     * @param projectName
     * @param datasetName
     * @return the dataset status
     */
    private String getDatasetStatus(String projectName, String datasetName) {

        String status;

        File projectFolder = new File(storeBioinfoFolder, projectName);
        File datasetFolder = new File(projectFolder, datasetName);
        File metaFile = new File(datasetFolder, "meta.xml");

        // @TODO: replace by propper xml parsing!!

        try {
            FileReader r = new FileReader(metaFile);
            BufferedReader br = new BufferedReader(r);
            status = br.readLine();
            status = status.substring(status.indexOf("=") + 2, status.indexOf("\">"));
            br.close();
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return status;
    }

    /**
     * Get the number of datasets.
     *
     * @param project
     * @return the number of datasets
     */
    public int getNumberOfDatasets(Project project) {

        int numberOfDatasets = 0;

        File projectFolder = new File(storeBioinfoFolder, project.getName());

        if (!projectFolder.exists()) {
            JOptionPane.showMessageDialog(this, "Project \'" + project.getName() + "\' not found!", "Project Error", JOptionPane.ERROR_MESSAGE);
            return numberOfDatasets;
        }

        File[] datasetFolders = projectFolder.listFiles();
        int datatypesCounter = 0;

        for (int i = 0; i < datasetFolders.length; i++) {
            if (datasetFolders[i].isDirectory()) {
                datatypesCounter++;
            }
        }

        return datatypesCounter;
    }

    /**
     * Get the list of supported dataset types.
     *
     * @return the list of supported dataset types
     */
    public ArrayList<String> getDatasetTypes() {

        ArrayList<String> datasetTypes = new ArrayList<String>();

        File jarFile = new File(getJarFilePath());
        File datasetTypesFile = new File(jarFile, "config/dataset_types");

        if (!datasetTypesFile.exists()) {
            JOptionPane.showMessageDialog(this, "Dataset types file not found! Closing program.", "Folder Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        try {

            FileReader r = new FileReader(datasetTypesFile);
            BufferedReader br = new BufferedReader(r);

            String line = br.readLine();

            while (line != null) {
                datasetTypes.add(line);
                line = br.readLine();
            }

            br.close();
            r.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datasetTypes;
    }

    /**
     * Add a dataset.
     *
     * @param projectName
     * @param dataset
     */
    public void addDataset(String projectName, Dataset dataset) {

        File projectFolder = new File(storeBioinfoFolder, projectName);
        File datasetFolder = new File(projectFolder, dataset.getName());

        if (!datasetFolder.exists()) {
            boolean created = datasetFolder.mkdir();

            if (!created) {
                JOptionPane.showMessageDialog(this, "Dataset \'" + dataset.getName() + "\' folder could not be created!", "Dataset Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            File metaFile = new File(datasetFolder, "meta.xml");

            boolean created = true;
            boolean alreadyExisted = true;

            // create the meta file
            if (!metaFile.exists()) {
                created = new File(datasetFolder, "meta.xml").createNewFile();
                alreadyExisted = false;
            }

            if (created) {

                FileWriter f = new FileWriter(metaFile);
                BufferedWriter bw = new BufferedWriter(f);

                String meta = "<dataset status=\"" + dataset.getDatasetStatus() + "\">\n";
                meta += "\t<description>" + dataset.getDescription() + "</description>" + "\n";

                for (int i = 0; i < dataset.getDataFolders().size(); i++) {
                    meta += "\t<datafolder status=\"new\">\n";
                    meta += "\t\t<path>" + dataset.getDataFolders().get(i).getRelativeFolderPath() + "</path>\n";
                    meta += "\t\t<type>" + dataset.getDataFolders().get(i).getDataType() + "</type>\n";
                    meta += "\t</datafolder>\n";
                }

                meta += "</dataset>";

                bw.write(meta);
                bw.close();
                f.close();
            } else {
                JOptionPane.showMessageDialog(this, "Dataset \'" + dataset.getName() + "\' failed to create meta.xml!", "Dataset Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // update the status of the parent project
            Project tempProject = readProjectMetaData(projectName);
            tempProject.setProjectStatus("partial");
            createProjectMetaData(tempProject, true);

            updateProjectsList(projectName);
            projectsTableMouseReleased(null);

            if (alreadyExisted) {
                JOptionPane.showMessageDialog(this, "Dataset \'" + dataset.getName() + "\' updated.", "Dataset Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dataset \'" + dataset.getName() + "\' created.", "Dataset Created", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // do nothing
    }

    /**
     * Retrieves the version number set in the pom file.
     *
     * @return the version number of StoreBioinfoGUI
     */
    private String getVersion() {

        java.util.Properties p = new java.util.Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("storebioinfogui.properties");
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p.getProperty("storebioinfogui.version");
    }

    /**
     * Returns the last selected folder.
     *
     * @return the lastSelectedFolder
     */
    public String getLastSelectedFolder() {
        return lastSelectedFolder;
    }

    /**
     * Set the last selected folder.
     *
     * @param lastSelectedFolder the lastSelectedFolder to set
     */
    public void setLastSelectedFolder(String lastSelectedFolder) {
        this.lastSelectedFolder = lastSelectedFolder;
    }

    /**
     * Returns the folder containing the StoreBioinfo meta data.
     *
     * @return the folder containing the StoreBioinfo meta data
     */
    public File getStoreBioinfoFolder() {
        return storeBioinfoFolder;
    }

    /**
     * Set the folder containing the StoreBioinfo meta data.
     *
     * @param storeBioinfoFolder the folder to set
     */
    public void setStoreBioinfoFolder(File storeBioinfoFolder) {
        this.storeBioinfoFolder = storeBioinfoFolder;
    }

    /**
     * Returns the mapped folder.
     *
     * @return the mappedFolder
     */
    public File getMappedFolder() {
        return mappedFolder;
    }

    /**
     * Set the mapped folder.
     *
     * @param mappedFolder the mappedFolder to set
     */
    public void setMappedFolder(File mappedFolder) {
        this.mappedFolder = mappedFolder;
    }

    /**
     * Returns the quota names.
     *
     * @return the quotaNames
     */
    public ArrayList<String> getQuotaNames() {
        return quotaNames;
    }

    /**
     * Returns the quota id for a given quota name.
     *
     * @param quotaName the quota to get the id for
     * @return the quotaNameToIdsMap
     */
    public String getQuotaId(String quotaName) {
        return quotaNameToIdMap.get(quotaName);
    }
}

package no.uib.storbioinfogui.data;

import java.util.ArrayList;

/**
 * Represents a StoreBioinfo dataset.
 *
 * @author Harald Barsnes
 */
public class Dataset implements Comparable<Object> {

    /**
     * The dataset name.
     */
    private String name;
    /**
     * The project name.
     */
    private String projectName;
    /**
     * The dataset description.
     */
    private String description;
    /**
     * The set of data folders.
     */
    private ArrayList<DataFolder> dataFolders;

    /**
     * Create a new dataset.
     *
     * @param name the dataset name
     * @param description the dataset description
     * @param projectName the project the dataset belongs to
     * @param dataFolders the set of data folders 
     */
    public Dataset(String name, String description, String projectName, ArrayList<DataFolder> dataFolders) {
        this.name = name;
        this.projectName = projectName;
        this.description = description;
        this.dataFolders = dataFolders;
    }

    /**
     * Returns the dataset name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the dataset name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the project name.
     * 
     * @return the projecName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the project name.
     * 
     * @param projectName the name to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Returns the dataset description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the dataset description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Dataset) {
            return name.compareTo(((Dataset) o).getName());
        } else {
            return 1;
        }
    }

    /**
     * Return the set of data folders.
     * 
     * @return the dataFolders
     */
    public ArrayList<DataFolder> getDataFolders() {
        return dataFolders;
    }

    /**
     * Set the set of data folders.
     * 
     * @param dataFolders the dataFolders to set
     */
    public void setDataFolders(ArrayList<DataFolder> dataFolders) {
        this.dataFolders = dataFolders;
    }
}

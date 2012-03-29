package no.uib.storbioinfogui.data;

/**
 * This class represents a data folder, i.e., a folder the user links to that
 * contains the data to be backed up.
 *
 * @author Harald Barsnes
 */
public class DataFolder {

    /**
     * The path to the data folder.
     */
    private String folderPath;
    /**
     * The data type.
     */
    private String dataType;

    /**
     * Create a new DataFolder object.
     *
     * @param folderPath
     * @param dataType
     */
    public DataFolder(String folderPath, String dataType) {
        this.folderPath = folderPath;
        this.dataType = dataType;
    }

    /**
     * Returns the path to the data folder.
     *
     * @return the folderPath
     */
    public String getFolderPath() {
        return folderPath;
    }

    /**
     * Set the path to the data folder.
     *
     * @param folderPath the folderPath to set
     */
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    /**
     * Returns the data type.
     *
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the data type.
     *
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public String toString() {
        return "[" + dataType + "]    " + folderPath;
    }
}

package no.uib.storbioinfogui.data;

/**
 * This class represents a data folder, i.e., a folder the user links to that
 * contains the data to be backed up.
 *
 * @author Harald Barsnes
 */
public class DataFolder {

    /**
     * The relative path to the data folder. Note: Relative to the mapped folder.
     */
    private String relativeFolderPath;
    /**
     * The data type.
     */
    private String dataType;

    /**
     * Create a new DataFolder object.
     *
     * @param relativeFolderPath
     * @param dataType
     */
    public DataFolder(String relativeFolderPath, String dataType) {
        this.relativeFolderPath = relativeFolderPath;
        this.dataType = dataType;
    }

    /**
     * Returns the relative path to the data folder.
     *
     * @return the relativeFolderPath
     */
    public String getRelativeFolderPath() {
        return relativeFolderPath;
    }

    /**
     * Set the relative path to the data folder.
     *
     * @param relativeFolderPath the relativeFolderPath to set
     */
    public void setFolderPath(String relativeFolderPath) {
        this.relativeFolderPath = relativeFolderPath;
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
        return "[" + dataType + "]    " + relativeFolderPath;
    }
}

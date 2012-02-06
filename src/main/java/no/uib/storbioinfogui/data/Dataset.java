package no.uib.storbioinfogui.data;

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
     * The dataset description.
     */
    private String description;

    /**
     * Create a new dataset.
     *
     * @param name the dataset name
     * @param description the dataset description
     */
    public Dataset(String name, String description) {
        this.name = name;
        this.description = description;
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
}

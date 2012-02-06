package no.uib.storbioinfogui.data;

/**
 * Represents a StorBioinfo project.
 *
 * @author Harald Barsnes
 */
public class Project {

    /**
     * The project name.
     */
    private String name;
    /**
     * The project contact.
     */
    private String contact;
    /**
     * The contact e-mail.
     */
    private String eMail;
    /**
     * The contact phone number.
     */
    private String phone;
    /**
     * The project summary.
     */
    private String summary;
    /**
     * The project description.
     */
    private String description;

    /**
     * Create a new Project.
     *
     * @param name project name
     * @param contact project contact
     * @param eMail contact e-mail address
     * @param phone contact phone number
     * @param summary project summary
     * @param description project description
     */
    public Project(String name, String contact, String eMail, String phone, String summary, String description) {
        this.name = name;
        this.contact = contact;
        this.eMail = eMail;
        this.phone = phone;
        this.summary = summary;
        this.description = description;
    }

    /**
     * Returns the project name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the project name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the contact name.
     * 
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Set the contact name.
     * 
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Retutns the contact's e-mail.
     * 
     * @return the eMail
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * Sets the contact's e-mail address.
     * 
     * @param eMail the eMail to set
     */
    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * Returns the contact's phone number.
     * 
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the contact's phone number.
     * 
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the project summary.
     * 
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the project summary.
     * 
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Returns the projects description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the project description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

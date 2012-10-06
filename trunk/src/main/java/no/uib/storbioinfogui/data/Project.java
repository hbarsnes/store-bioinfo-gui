package no.uib.storbioinfogui.data;

import java.util.ArrayList;

/**
 * Represents a StorBioinfo project.
 *
 * @author Harald Barsnes
 */
public class Project {

    /**
     * The ID of the quota to be used.
     */
    private String quotaId;
    /**
     * The project name.
     */
    private String name;
    /**
     * The project contact.
     */
    private String contact;
    /**
     * The StoreBioinfo user name of the data owner. Can be null.
     */
    private String ownerUserName;
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
     * The mail invites.
     */
    private ArrayList<String> mailInvites;
    /**
     * The user invites.
     */
    private ArrayList<String> userInvites;
    /**
     * If true, a mail invite will be sent to the data owner.
     */
    private boolean sendMailInviteToDataOwner;
    /**
     * If true, a user invite will be sent to the data owner.
     */
    private boolean sendUserInviteToDataOwner;

    /**
     * Create a new Project.
     *
     * @param quotaId quota id
     * @param name project name
     * @param contact project contact
     * @param ownerUserName the StoreBioinfor user name for the owner
     * @param eMail contact e-mail address
     * @param phone contact phone number
     * @param summary project summary
     * @param description project description
     * @param mailInvites the mail invites
     * @param userInvites the user invites
     * @param sendMailInviteToDataOwner if true, a mail invite will be sent to the data owner
     * @param sendUserInviteToDataOwner if true, a user invite will be sent to the data owner
     */
    public Project(String quotaId, String name, String contact, String ownerUserName, String eMail, String phone, String summary, 
            String description, ArrayList<String> mailInvites, ArrayList<String> userInvites, 
            boolean sendMailInviteToDataOwner, boolean sendUserInviteToDataOwner) {
        this.quotaId = quotaId;
        this.name = name;
        this.contact = contact;
        this.ownerUserName = ownerUserName;
        this.eMail = eMail;
        this.phone = phone;
        this.summary = summary;
        this.description = description;
        this.mailInvites = mailInvites;
        this.userInvites = userInvites;
        this.sendMailInviteToDataOwner = sendMailInviteToDataOwner;
        this.sendUserInviteToDataOwner = sendUserInviteToDataOwner;
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

    /**
     * Returns the mail invites.
     * 
     * @return the mailInvites
     */
    public ArrayList<String> getMailInvites() {
        return mailInvites;
    }

    /**
     * Sets the mail invites.
     * 
     * @param mailInvites the mailInvites to set
     */
    public void setMailInvites(ArrayList<String> mailInvites) {
        this.mailInvites = mailInvites;
    }

    /**
     * Returns the user invites.
     * 
     * @return the userInvites
     */
    public ArrayList<String> getUserInvites() {
        return userInvites;
    }

    /**
     * Sets the user invites.
     * 
     * @param userInvites the userInvites to set
     */
    public void setUserInvites(ArrayList<String> userInvites) {
        this.userInvites = userInvites;
    }

    /**
     * Returns the quota ID.
     * 
     * @return the quotaId
     */
    public String getQuotaId() {
        return quotaId;
    }

    /**
     * Sets the quota ID.
     * 
     * @param quotaId the quotaId to set
     */
    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId;
    }

    /**
     * Returns true if an mail invite should be sent to the data owner.
     * 
     * @return the sendMailInviteToDataOwner
     */
    public boolean isSendMailInviteToDataOwner() {
        return sendMailInviteToDataOwner;
    }

    /**
     * Set if an mail invite should be sent to the data owner. 
     * 
     * @param sendMailInviteToDataOwner the sendMailInviteToDataOwner to set
     */
    public void setSendMailInviteToDataOwner(boolean sendMailInviteToDataOwner) {
        this.sendMailInviteToDataOwner = sendMailInviteToDataOwner;
    }

    /**
     * Returns true if a user invite should be sent to the data owner.
     * 
     * @return the sendUserInviteToDataOwner
     */
    public boolean isSendUserInviteToDataOwner() {
        return sendUserInviteToDataOwner;
    }

    /**
     * Set if a user invite should be sent to the data owner.
     * 
     * @param sendUserInviteToDataOwner the sendUserInviteToDataOwner to set
     */
    public void setSendUserInviteToDataOwner(boolean sendUserInviteToDataOwner) {
        this.sendUserInviteToDataOwner = sendUserInviteToDataOwner;
    }

    /**
     * Returns the StoreBioinfo user name of the data owner.
     * 
     * @return the ownerUserName
     */
    public String getOwnerUserName() {
        return ownerUserName;
    }

    /**
     * Sets the StoreBioinfo user name of the data owner.
     * 
     * @param ownerUserName the ownerUserName to set
     */
    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }
}

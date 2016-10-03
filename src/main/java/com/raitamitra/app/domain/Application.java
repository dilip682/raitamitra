package com.raitamitra.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.raitamitra.app.domain.enumeration.FarmerType;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "app_date", nullable = false)
    private ZonedDateTime appDate;

    @NotNull
    @Size(min = 1)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 1)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Lob
    @Column(name = "applicant_photo")
    private byte[] applicantPhoto;

    @Column(name = "applicant_photo_content_type")
    private String applicantPhotoContentType;

    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 8)
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "father_or_husband_name")
    private String fatherOrHusbandName;

    @NotNull
    @Column(name = "servey_number", nullable = false)
    private String serveyNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "farmer_type", nullable = false)
    private FarmerType farmerType;

    @NotNull
    @Column(name = "crop", nullable = false)
    private String crop;

    @NotNull
    @Column(name = "source_of_water", nullable = false)
    private String sourceOfWater;

    @NotNull
    @Column(name = "benefit_needed", nullable = false)
    private String benefitNeeded;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "docs_verified")
    private Boolean docsVerified;

    @Column(name = "attachments")
    private String attachments;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @NotNull
    private District district;

    @ManyToOne
    @NotNull
    private Taluka taluka;

    @ManyToOne
    @NotNull
    private Hobli hobli;

    @ManyToOne
    @NotNull
    private Village village;

    @ManyToOne
    @NotNull
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAppDate() {
        return appDate;
    }

    public Application appDate(ZonedDateTime appDate) {
        this.appDate = appDate;
        return this;
    }

    public void setAppDate(ZonedDateTime appDate) {
        this.appDate = appDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public Application firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Application lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getApplicantPhoto() {
        return applicantPhoto;
    }

    public Application applicantPhoto(byte[] applicantPhoto) {
        this.applicantPhoto = applicantPhoto;
        return this;
    }

    public void setApplicantPhoto(byte[] applicantPhoto) {
        this.applicantPhoto = applicantPhoto;
    }

    public String getApplicantPhotoContentType() {
        return applicantPhotoContentType;
    }

    public Application applicantPhotoContentType(String applicantPhotoContentType) {
        this.applicantPhotoContentType = applicantPhotoContentType;
        return this;
    }

    public void setApplicantPhotoContentType(String applicantPhotoContentType) {
        this.applicantPhotoContentType = applicantPhotoContentType;
    }

    public String getEmail() {
        return email;
    }

    public Application email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Application phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFatherOrHusbandName() {
        return fatherOrHusbandName;
    }

    public Application fatherOrHusbandName(String fatherOrHusbandName) {
        this.fatherOrHusbandName = fatherOrHusbandName;
        return this;
    }

    public void setFatherOrHusbandName(String fatherOrHusbandName) {
        this.fatherOrHusbandName = fatherOrHusbandName;
    }

    public String getServeyNumber() {
        return serveyNumber;
    }

    public Application serveyNumber(String serveyNumber) {
        this.serveyNumber = serveyNumber;
        return this;
    }

    public void setServeyNumber(String serveyNumber) {
        this.serveyNumber = serveyNumber;
    }

    public FarmerType getFarmerType() {
        return farmerType;
    }

    public Application farmerType(FarmerType farmerType) {
        this.farmerType = farmerType;
        return this;
    }

    public void setFarmerType(FarmerType farmerType) {
        this.farmerType = farmerType;
    }

    public String getCrop() {
        return crop;
    }

    public Application crop(String crop) {
        this.crop = crop;
        return this;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getSourceOfWater() {
        return sourceOfWater;
    }

    public Application sourceOfWater(String sourceOfWater) {
        this.sourceOfWater = sourceOfWater;
        return this;
    }

    public void setSourceOfWater(String sourceOfWater) {
        this.sourceOfWater = sourceOfWater;
    }

    public String getBenefitNeeded() {
        return benefitNeeded;
    }

    public Application benefitNeeded(String benefitNeeded) {
        this.benefitNeeded = benefitNeeded;
        return this;
    }

    public void setBenefitNeeded(String benefitNeeded) {
        this.benefitNeeded = benefitNeeded;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Application companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public Application accountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public Application bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Boolean isDocsVerified() {
        return docsVerified;
    }

    public Application docsVerified(Boolean docsVerified) {
        this.docsVerified = docsVerified;
        return this;
    }

    public void setDocsVerified(Boolean docsVerified) {
        this.docsVerified = docsVerified;
    }

    public String getAttachments() {
        return attachments;
    }

    public Application attachments(String attachments) {
        this.attachments = attachments;
        return this;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getNotes() {
        return notes;
    }

    public Application notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public District getDistrict() {
        return district;
    }

    public Application district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Taluka getTaluka() {
        return taluka;
    }

    public Application taluka(Taluka taluka) {
        this.taluka = taluka;
        return this;
    }

    public void setTaluka(Taluka taluka) {
        this.taluka = taluka;
    }

    public Hobli getHobli() {
        return hobli;
    }

    public Application hobli(Hobli hobli) {
        this.hobli = hobli;
        return this;
    }

    public void setHobli(Hobli hobli) {
        this.hobli = hobli;
    }

    public Village getVillage() {
        return village;
    }

    public Application village(Village village) {
        this.village = village;
        return this;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Status getStatus() {
        return status;
    }

    public Application status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if(application.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, application.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", appDate='" + appDate + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", applicantPhoto='" + applicantPhoto + "'" +
            ", applicantPhotoContentType='" + applicantPhotoContentType + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", fatherOrHusbandName='" + fatherOrHusbandName + "'" +
            ", serveyNumber='" + serveyNumber + "'" +
            ", farmerType='" + farmerType + "'" +
            ", crop='" + crop + "'" +
            ", sourceOfWater='" + sourceOfWater + "'" +
            ", benefitNeeded='" + benefitNeeded + "'" +
            ", companyName='" + companyName + "'" +
            ", accountNo='" + accountNo + "'" +
            ", bankName='" + bankName + "'" +
            ", docsVerified='" + docsVerified + "'" +
            ", attachments='" + attachments + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}

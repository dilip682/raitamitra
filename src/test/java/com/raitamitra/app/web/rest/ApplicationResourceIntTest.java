package com.raitamitra.app.web.rest;

import com.raitamitra.app.RaitamitraApp;

import com.raitamitra.app.domain.Application;
import com.raitamitra.app.domain.District;
import com.raitamitra.app.domain.Taluka;
import com.raitamitra.app.domain.Hobli;
import com.raitamitra.app.domain.Village;
import com.raitamitra.app.domain.Status;
import com.raitamitra.app.repository.ApplicationRepository;
import com.raitamitra.app.repository.search.ApplicationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.raitamitra.app.domain.enumeration.FarmerType;
/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaitamitraApp.class)
public class ApplicationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_APP_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_APP_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_APP_DATE_STR = dateTimeFormatter.format(DEFAULT_APP_DATE);
    private static final String DEFAULT_FIRST_NAME = "A";
    private static final String UPDATED_FIRST_NAME = "B";
    private static final String DEFAULT_LAST_NAME = "A";
    private static final String UPDATED_LAST_NAME = "B";

    private static final byte[] DEFAULT_APPLICANT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_APPLICANT_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_APPLICANT_PHOTO_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBB";
    private static final String DEFAULT_FATHER_OR_HUSBAND_NAME = "AAAAA";
    private static final String UPDATED_FATHER_OR_HUSBAND_NAME = "BBBBB";
    private static final String DEFAULT_SERVEY_NUMBER = "AAAAA";
    private static final String UPDATED_SERVEY_NUMBER = "BBBBB";

    private static final FarmerType DEFAULT_FARMER_TYPE = FarmerType.Small;
    private static final FarmerType UPDATED_FARMER_TYPE = FarmerType.Medium;
    private static final String DEFAULT_CROP = "AAAAA";
    private static final String UPDATED_CROP = "BBBBB";
    private static final String DEFAULT_SOURCE_OF_WATER = "AAAAA";
    private static final String UPDATED_SOURCE_OF_WATER = "BBBBB";
    private static final String DEFAULT_BENEFIT_NEEDED = "AAAAA";
    private static final String UPDATED_BENEFIT_NEEDED = "BBBBB";
    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";
    private static final String DEFAULT_ACCOUNT_NO = "AAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBB";
    private static final String DEFAULT_BANK_NAME = "AAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBB";

    private static final Boolean DEFAULT_DOCS_VERIFIED = false;
    private static final Boolean UPDATED_DOCS_VERIFIED = true;
    private static final String DEFAULT_ATTACHMENTS = "AAAAA";
    private static final String UPDATED_ATTACHMENTS = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationSearchRepository applicationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource();
        ReflectionTestUtils.setField(applicationResource, "applicationSearchRepository", applicationSearchRepository);
        ReflectionTestUtils.setField(applicationResource, "applicationRepository", applicationRepository);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
                .appDate(DEFAULT_APP_DATE)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .applicantPhoto(DEFAULT_APPLICANT_PHOTO)
                .applicantPhotoContentType(DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .fatherOrHusbandName(DEFAULT_FATHER_OR_HUSBAND_NAME)
                .serveyNumber(DEFAULT_SERVEY_NUMBER)
                .farmerType(DEFAULT_FARMER_TYPE)
                .crop(DEFAULT_CROP)
                .sourceOfWater(DEFAULT_SOURCE_OF_WATER)
                .benefitNeeded(DEFAULT_BENEFIT_NEEDED)
                .companyName(DEFAULT_COMPANY_NAME)
                .accountNo(DEFAULT_ACCOUNT_NO)
                .bankName(DEFAULT_BANK_NAME)
                .docsVerified(DEFAULT_DOCS_VERIFIED)
                .attachments(DEFAULT_ATTACHMENTS)
                .notes(DEFAULT_NOTES);
        // Add required entity
        District district = DistrictResourceIntTest.createEntity(em);
        em.persist(district);
        em.flush();
        application.setDistrict(district);
        // Add required entity
        Taluka taluka = TalukaResourceIntTest.createEntity(em);
        em.persist(taluka);
        em.flush();
        application.setTaluka(taluka);
        // Add required entity
        Hobli hobli = HobliResourceIntTest.createEntity(em);
        em.persist(hobli);
        em.flush();
        application.setHobli(hobli);
        // Add required entity
        Village village = VillageResourceIntTest.createEntity(em);
        em.persist(village);
        em.flush();
        application.setVillage(village);
        // Add required entity
        Status status = StatusResourceIntTest.createEntity(em);
        em.persist(status);
        em.flush();
        application.setStatus(status);
        return application;
    }

    @Before
    public void initTest() {
        applicationSearchRepository.deleteAll();
        application = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getAppDate()).isEqualTo(DEFAULT_APP_DATE);
        assertThat(testApplication.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplication.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplication.getApplicantPhoto()).isEqualTo(DEFAULT_APPLICANT_PHOTO);
        assertThat(testApplication.getApplicantPhotoContentType()).isEqualTo(DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE);
        assertThat(testApplication.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApplication.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApplication.getFatherOrHusbandName()).isEqualTo(DEFAULT_FATHER_OR_HUSBAND_NAME);
        assertThat(testApplication.getServeyNumber()).isEqualTo(DEFAULT_SERVEY_NUMBER);
        assertThat(testApplication.getFarmerType()).isEqualTo(DEFAULT_FARMER_TYPE);
        assertThat(testApplication.getCrop()).isEqualTo(DEFAULT_CROP);
        assertThat(testApplication.getSourceOfWater()).isEqualTo(DEFAULT_SOURCE_OF_WATER);
        assertThat(testApplication.getBenefitNeeded()).isEqualTo(DEFAULT_BENEFIT_NEEDED);
        assertThat(testApplication.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testApplication.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testApplication.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testApplication.isDocsVerified()).isEqualTo(DEFAULT_DOCS_VERIFIED);
        assertThat(testApplication.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testApplication.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the Application in ElasticSearch
        Application applicationEs = applicationSearchRepository.findOne(testApplication.getId());
        assertThat(applicationEs).isEqualToComparingFieldByField(testApplication);
    }

    @Test
    @Transactional
    public void checkAppDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setAppDate(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setFirstName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setLastName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setPhone(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServeyNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setServeyNumber(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFarmerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setFarmerType(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCropIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setCrop(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceOfWaterIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setSourceOfWater(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBenefitNeededIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setBenefitNeeded(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applications
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
                .andExpect(jsonPath("$.[*].appDate").value(hasItem(DEFAULT_APP_DATE_STR)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].applicantPhotoContentType").value(hasItem(DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].applicantPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICANT_PHOTO))))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].fatherOrHusbandName").value(hasItem(DEFAULT_FATHER_OR_HUSBAND_NAME.toString())))
                .andExpect(jsonPath("$.[*].serveyNumber").value(hasItem(DEFAULT_SERVEY_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].farmerType").value(hasItem(DEFAULT_FARMER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].crop").value(hasItem(DEFAULT_CROP.toString())))
                .andExpect(jsonPath("$.[*].sourceOfWater").value(hasItem(DEFAULT_SOURCE_OF_WATER.toString())))
                .andExpect(jsonPath("$.[*].benefitNeeded").value(hasItem(DEFAULT_BENEFIT_NEEDED.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO.toString())))
                .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
                .andExpect(jsonPath("$.[*].docsVerified").value(hasItem(DEFAULT_DOCS_VERIFIED.booleanValue())))
                .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.appDate").value(DEFAULT_APP_DATE_STR))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.applicantPhotoContentType").value(DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.applicantPhoto").value(Base64Utils.encodeToString(DEFAULT_APPLICANT_PHOTO)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.fatherOrHusbandName").value(DEFAULT_FATHER_OR_HUSBAND_NAME.toString()))
            .andExpect(jsonPath("$.serveyNumber").value(DEFAULT_SERVEY_NUMBER.toString()))
            .andExpect(jsonPath("$.farmerType").value(DEFAULT_FARMER_TYPE.toString()))
            .andExpect(jsonPath("$.crop").value(DEFAULT_CROP.toString()))
            .andExpect(jsonPath("$.sourceOfWater").value(DEFAULT_SOURCE_OF_WATER.toString()))
            .andExpect(jsonPath("$.benefitNeeded").value(DEFAULT_BENEFIT_NEEDED.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()))
            .andExpect(jsonPath("$.docsVerified").value(DEFAULT_DOCS_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findOne(application.getId());
        updatedApplication
                .appDate(UPDATED_APP_DATE)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .applicantPhoto(UPDATED_APPLICANT_PHOTO)
                .applicantPhotoContentType(UPDATED_APPLICANT_PHOTO_CONTENT_TYPE)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE)
                .fatherOrHusbandName(UPDATED_FATHER_OR_HUSBAND_NAME)
                .serveyNumber(UPDATED_SERVEY_NUMBER)
                .farmerType(UPDATED_FARMER_TYPE)
                .crop(UPDATED_CROP)
                .sourceOfWater(UPDATED_SOURCE_OF_WATER)
                .benefitNeeded(UPDATED_BENEFIT_NEEDED)
                .companyName(UPDATED_COMPANY_NAME)
                .accountNo(UPDATED_ACCOUNT_NO)
                .bankName(UPDATED_BANK_NAME)
                .docsVerified(UPDATED_DOCS_VERIFIED)
                .attachments(UPDATED_ATTACHMENTS)
                .notes(UPDATED_NOTES);

        restApplicationMockMvc.perform(put("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
                .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getAppDate()).isEqualTo(UPDATED_APP_DATE);
        assertThat(testApplication.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplication.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplication.getApplicantPhoto()).isEqualTo(UPDATED_APPLICANT_PHOTO);
        assertThat(testApplication.getApplicantPhotoContentType()).isEqualTo(UPDATED_APPLICANT_PHOTO_CONTENT_TYPE);
        assertThat(testApplication.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplication.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplication.getFatherOrHusbandName()).isEqualTo(UPDATED_FATHER_OR_HUSBAND_NAME);
        assertThat(testApplication.getServeyNumber()).isEqualTo(UPDATED_SERVEY_NUMBER);
        assertThat(testApplication.getFarmerType()).isEqualTo(UPDATED_FARMER_TYPE);
        assertThat(testApplication.getCrop()).isEqualTo(UPDATED_CROP);
        assertThat(testApplication.getSourceOfWater()).isEqualTo(UPDATED_SOURCE_OF_WATER);
        assertThat(testApplication.getBenefitNeeded()).isEqualTo(UPDATED_BENEFIT_NEEDED);
        assertThat(testApplication.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testApplication.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testApplication.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testApplication.isDocsVerified()).isEqualTo(UPDATED_DOCS_VERIFIED);
        assertThat(testApplication.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testApplication.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the Application in ElasticSearch
        Application applicationEs = applicationSearchRepository.findOne(testApplication.getId());
        assertThat(applicationEs).isEqualToComparingFieldByField(testApplication);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean applicationExistsInEs = applicationSearchRepository.exists(application.getId());
        assertThat(applicationExistsInEs).isFalse();

        // Validate the database is empty
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);

        // Search the application
        restApplicationMockMvc.perform(get("/api/_search/applications?query=id:" + application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].appDate").value(hasItem(DEFAULT_APP_DATE_STR)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].applicantPhotoContentType").value(hasItem(DEFAULT_APPLICANT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].applicantPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICANT_PHOTO))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].fatherOrHusbandName").value(hasItem(DEFAULT_FATHER_OR_HUSBAND_NAME.toString())))
            .andExpect(jsonPath("$.[*].serveyNumber").value(hasItem(DEFAULT_SERVEY_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].farmerType").value(hasItem(DEFAULT_FARMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].crop").value(hasItem(DEFAULT_CROP.toString())))
            .andExpect(jsonPath("$.[*].sourceOfWater").value(hasItem(DEFAULT_SOURCE_OF_WATER.toString())))
            .andExpect(jsonPath("$.[*].benefitNeeded").value(hasItem(DEFAULT_BENEFIT_NEEDED.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].docsVerified").value(hasItem(DEFAULT_DOCS_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }
}

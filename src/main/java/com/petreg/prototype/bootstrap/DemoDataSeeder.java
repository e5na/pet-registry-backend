package com.petreg.prototype.bootstrap;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.petreg.prototype.exception.ResourceNotFoundException;
import com.petreg.prototype.model.Breed;
import com.petreg.prototype.model.Microchip;
import com.petreg.prototype.model.OwnerProfile;
import com.petreg.prototype.model.Pet;
import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.Species;
import com.petreg.prototype.model.User;
import com.petreg.prototype.model.VetProfile;
import com.petreg.prototype.model.type.PetStatus;
import com.petreg.prototype.model.type.RoleEnum;
import com.petreg.prototype.repository.BreedRepository;
import com.petreg.prototype.repository.MicrochipRepository;
import com.petreg.prototype.repository.PetRepository;
import com.petreg.prototype.repository.RoleRepository;
import com.petreg.prototype.repository.SpeciesRepository;
import com.petreg.prototype.repository.UserRepository;

import net.datafaker.Faker;
import net.datafaker.providers.base.Text;

@Component
@ConditionalOnBooleanProperty(name = "prototype.demo.seed")
public class DemoDataSeeder {

    private static final int NUM_VETS_TO_GENERATE = 5;
    private static final int NUM_OWNERS_TO_GENERATE = 25;
    private static final int MAX_PETS_PER_OWNER = 3;
    private static final String DEFAULT_PASSWORD = "password";

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final Faker faker = new Faker(Locale.of("et", "EE"));
    private final Random random = new Random();

    private final BreedRepository breedRepository;
    private final MicrochipRepository microchipRepository;
    private final PetRepository petRepository;
    private final RoleRepository roleRepository;
    private final RoleSeeder roleSeeder;
    private final SpeciesRepository speciesRepository;
    private final UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(
            BreedRepository breedRepository,
            MicrochipRepository microchipRepository,
            PetRepository petRepository,
            RoleRepository roleRepository,
            RoleSeeder roleSeeder,
            SpeciesRepository speciesRepository,
            UserRepository userRepository,
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder) {
        this.breedRepository = breedRepository;
        this.microchipRepository = microchipRepository;
        this.petRepository = petRepository;
        this.roleRepository = roleRepository;
        this.roleSeeder = roleSeeder;
        this.speciesRepository = speciesRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void createDemoData() {
        log.info("Bootstrapping test data...");

        // Always clear everything before starting
        clearDatabase();

        // We need to re-create roles, since the db was wiped
        roleSeeder.createRoles();

        // Ensure Species and some Breeds exist
        Species dog = createSpecies("Koer");
        Species cat = createSpecies("Kass");
        Species ferret = createSpecies("Tuhkur");

        // Dogs
        final String[] dogBreedNames = {
            "Kuldne retriiver",
            "Lambarodi retriiver",
            "Saksa lambakoer",
            "Kääbusspits",
            "Berni Alpi karjakoer",
            "Papillon",
            "Prantsuse buldog",
            "Austraalia lambakoer"
        };

        for (String dogBreedName : dogBreedNames) {
            createBreed(dogBreedName, dog);
        }

        // Cats
        final String[] catBreedNames = {
            "Briti lühikarvaline",
            "Norra metskass",
            "Šoti lontkõrvaline",
            "Vene sinine",
            "Pärsia pikakarvaline",
            "Siiami",
            "Egiptuse mau"
        };

        for (String catBreedName : catBreedNames) {
            createBreed(catBreedName, cat);
        }

        // Other
        createBreed("Valgetuhkur", ferret);

        Role ownerRole = roleRepository.findByName(RoleEnum.OWNER)
                .orElseThrow(() -> new ResourceNotFoundException("OWNER role not found"));

        List<Breed> allBreeds = breedRepository.findAll();

        for (int i = 0; i < NUM_OWNERS_TO_GENERATE; i++) {
            User user = createFakeUser(ownerRole);

            // 1..n pets per owner
            int petCount = random.nextInt(MAX_PETS_PER_OWNER) + 1;
            for (int j = 0; j < petCount; j++) {
                createFakePet(user, allBreeds);
            }
        }

        Role vetRole = roleRepository.findByName(RoleEnum.VET)
                .orElseThrow(() -> new ResourceNotFoundException("VET role not found"));

        for (int i = 0; i < NUM_VETS_TO_GENERATE; i++) {
            User user = createFakeUser(vetRole);
        }

        // Log one OWNER for testing purposes
        userRepository.findByRoles_Name(RoleEnum.OWNER)
                .stream()
                .findFirst()
                .ifPresent(u -> log.info("DEMO: Log in as an OWNER with the credentials '{}:{}'",
                        u.getPersonalCode(),
                        DEFAULT_PASSWORD));

        // Log one VET for testing purposes
        userRepository.findByRoles_Name(RoleEnum.VET)
                .stream()
                .findFirst()
                .ifPresent(u -> log.info("DEMO: Log in as a VET with the credentials '{}:{}'",
                        u.getPersonalCode(),
                        DEFAULT_PASSWORD));

        log.info("Test data seeding complete");
    }

    private Species createSpecies(String name) {
        return speciesRepository.save(new Species(name));
    }

    private Breed createBreed(String name, Species species) {
        return breedRepository.save(new Breed(name, species));
    }

    private User createFakeUser(Role role) {
        String personalCode = faker.idNumber().valid();

        while (userRepository.existsByPersonalCode(personalCode)) {
            personalCode = faker.idNumber().valid();
        }

        String email = faker.internet().emailAddress();
        // Derive names from email addresses, these seems to be more diverse anyway for some reason
        String[] fromEmail = email.split("\\.|@");
        String firstName = capitalize(fromEmail[0]);
        String lastName = capitalize(fromEmail[1]);

        User user = new User();
        // There is faker.name().firstName() but that we need to match emails with names
        user.setFirstName(firstName);
        // There is faker.name().lastName() but that we need to match emails with names
        user.setLastName(lastName);
        user.setPersonalCode(personalCode);
        user.setEmail(email);
        user.setPhoneNumber(faker.phoneNumber().cellPhone());
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setRoles(Set.of(role));

        switch (role.getName()) {
            case RoleEnum.OWNER -> {
                OwnerProfile profile = new OwnerProfile();
                profile.setAddress(faker.address().fullAddress());
                profile.setUser(user);
                user.setOwnerProfile(profile);
            }
            case RoleEnum.VET -> {
                VetProfile profile = new VetProfile();
                String licenseNumber = faker.text().text(Text.TextSymbolsBuilder.builder()
                        .len(4)
                        .with("0123456789", 4)
                        .build());
                profile.setLicenseNumber(licenseNumber);
                String[] specializations = {
                    "Kirurgia",
                    "Dermatoloogia",
                    "Diagnostika",
                    "Stomatoloogia"
                };
                profile.setSpecialization(faker.options().option(specializations));
                profile.setUser(user);
                user.setVetProfile(profile);
            }
            default -> {
                throw new RuntimeException("Unknown role");
            }
        }

        return userRepository.save(user);
    }

    private void createFakePet(User owner, List<Breed> breeds) {
        Breed breed = breeds.get(random.nextInt(breeds.size()));

        String microchipNumber = faker.number().digits(15);
        while (microchipRepository.existsByMicrochipNumber(microchipNumber)) {
            microchipNumber = faker.number().digits(15);
        }

        Microchip microchip = new Microchip(microchipNumber, "MicroPro Ltd", true);
        microchip = microchipRepository.save(microchip);

        Pet pet = new Pet();
        pet.setName(generateEstonianPetName());
        pet.setSex(random.nextBoolean() ? 'M' : 'F');
        pet.setBirthDate(faker.timeAndDate().birthday(1, 12));
        // There is faker.color().name() but that's not localized, so use a custom method
        pet.setColor(generateEstonianColor());
        pet.setStatus(PetStatus.ACTIVE);
        pet.setBreed(breed);
        pet.setMicrochip(microchip);
        pet.setOwner(owner);

        petRepository.save(pet);
    }

    private String generateEstonianColor() {
        String[] colors = {
            "must", "pruun", "hall", "valge"
        };

        return faker.options().option(colors);
    }

    private String generateEstonianPetName() {
        String[] petNames = {
            "Muri", "Pontu", "Pitsu", "Reks", "Sämmi", "Bella", "Luna", "Donna", "Karu", "Alfred",
            "Bruno", "Miisu", "Nurr", "Kiti", "Pätu", "Kusti", "Oskar", "Liisu", "Triibu", "Jossu",
            "Notsu", "Pipi", "Juku", "Mimi", "Tom", "Miku", "Mann", "Täpi", "Sipsik"
        };

        // 90% chance for a classic pet name, 10% for a human name
        if (random.nextInt(10) < 9) {
            return faker.options().option(petNames);
        } else {
            return faker.name().firstName();
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void clearDatabase() {
        log.info("Clearing database...");

        jdbcTemplate.execute("TRUNCATE TABLE users, pets, species, breeds, microchips, roles, " +
                "shelters, pictures, ownership_transfers, pet_lifecycle_events, treatments, " +
                "veterinary_checks, shelter_admissions, user_roles, veterinary_profiles, " +
                "owner_profiles, shelter_worker_profiles RESTART IDENTITY CASCADE");
    }
}

package gr.hua.dit.ds.project20205.entities;
import jakarta.persistence.*;


@Entity
public class Property {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // Τίτλος (π.χ. "Διαμέρισμα", "Μεζονέτα")
    private String address;
    private double price;
    private int rooms; // Αριθμός δωματίων
    private int bathrooms; // Αριθμός τουαλετών
    private double squareMeters; // Τετραγωνικά μέτρα
    private int constructionYear; // Έτος κατασκευής
    private boolean hasGarden; // Κήπος
    private boolean hasBalcony; // Μπαλκόνι
    private boolean hasStorage; // Αποθήκη
    private boolean hasParking; // Ιδιωτική θέση στάθμευσης
    private String propertyType; // Τύπος ακινήτου: "Μονοκατοικία", "Διαμέρισμα", "Βίλα"
    private String description; // Περιγραφή ακινήτου

    // Default Constructor
    public Property() {}

    // Constructor με όλα τα πεδία
    public Property(String title, String address, double price, int rooms, int bathrooms,
                    double squareMeters, int constructionYear, boolean hasGarden, boolean hasBalcony,
                    boolean hasStorage, boolean hasParking, String propertyType, String description) {
        this.title = title;
        this.address = address;
        this.price = price;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.squareMeters = squareMeters;
        this.constructionYear = constructionYear;
        this.hasGarden = hasGarden;
        this.hasBalcony = hasBalcony;
        this.hasStorage = hasStorage;
        this.hasParking = hasParking;
        this.propertyType = propertyType;
        this.description = description;
    }
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // Default status

    // Getters και Setters
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    // Getters και Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(double squareMeters) {
        this.squareMeters = squareMeters;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public boolean isHasStorage() {
        return hasStorage;
    }

    public void setHasStorage(boolean hasStorage) {
        this.hasStorage = hasStorage;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


}

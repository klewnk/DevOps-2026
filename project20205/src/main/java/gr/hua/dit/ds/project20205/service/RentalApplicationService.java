package gr.hua.dit.ds.project20205.service;
import gr.hua.dit.ds.project20205.entities.RentalApplication;
import gr.hua.dit.ds.project20205.repositories.RentalApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gr.hua.dit.ds.project20205.entities.Property;
import gr.hua.dit.ds.project20205.repositories.PropertyRepository;
import java.util.List;
import gr.hua.dit.ds.project20205.entities.User;

@Service
public class RentalApplicationService {
    @Autowired
    private final RentalApplicationRepository rentalApplicationRepository;
    private final PropertyRepository propertyRepository;

    // Ένας constructor που κάνει inject και τα δύο repositories
    public RentalApplicationService(RentalApplicationRepository rentalApplicationRepository,
                                    PropertyRepository propertyRepository) {
        this.rentalApplicationRepository = rentalApplicationRepository;
        this.propertyRepository = propertyRepository;
    }

    public RentalApplication getApplicationById(Long id) {
        return rentalApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));
    }


    public void saveApplication(RentalApplication application) {
        rentalApplicationRepository.save(application);
    }
    public List<RentalApplication> getAllApplications() {
        return rentalApplicationRepository.findAll();
    }

    public void saveApplication(Long propertyId, RentalApplication application) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));
        application.setProperty(property); // Συνδέει την αίτηση με το ακίνητο
        rentalApplicationRepository.save(application); // Αποθηκεύει την αίτηση
    }

    public void deleteApplication(Long id) {
        rentalApplicationRepository.deleteById(id);
    }

    public boolean hasActiveApplication(Long propertyId) {
        return rentalApplicationRepository.existsByPropertyId(propertyId);
    }
}


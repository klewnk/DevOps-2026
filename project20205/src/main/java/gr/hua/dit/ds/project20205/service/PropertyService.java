package gr.hua.dit.ds.project20205.service;
import gr.hua.dit.ds.project20205.entities.Property;
import gr.hua.dit.ds.project20205.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final EntityManager entityManager;

    public PropertyService(PropertyRepository propertyRepository, EntityManager entityManager) {
        this.propertyRepository = propertyRepository;
        this.entityManager = entityManager;
    }

    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Property not found with ID: " + id));
    }


    public List<Property> getPropertiesByStatus(Property.Status status) {
        return propertyRepository.findAll().stream()
                .filter(property -> property.getStatus() == status)
                .collect(Collectors.toList());
    }


    public List<Property> getApprovedProperties() {
        return propertyRepository.findAll().stream()
                .filter(property -> property.getStatus() == Property.Status.APPROVED)
                .collect(Collectors.toList());
    }

    // Update property status
    public void updatePropertyStatus(Long propertyId, Property.Status newStatus) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));
        property.setStatus(newStatus);
        propertyRepository.save(property);
    }
    // Επιστρέφει όλα τα ακίνητα που είναι σε κατάσταση PENDING
    public List<Property> getPendingProperties() {
        return propertyRepository.findAll().stream()
                .filter(property -> property.getStatus() == Property.Status.PENDING)
                .collect(Collectors.toList());
    }


    public List<Property> advancedFilter(
            Integer minPrice, Integer maxPrice, Integer minRooms, Integer maxRooms,
            Integer minBathrooms, Integer maxBathrooms, Integer minSquareMeters,
            Integer maxSquareMeters, Integer minYear, Integer maxYear,
            Boolean hasGarden, Boolean hasParking, Boolean hasBalcony,
            Boolean hasStorage, String propertyType) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = cb.createQuery(Property.class);
        Root<Property> property = query.from(Property.class);

        List<Predicate> predicates = new ArrayList<>();

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("price"), maxPrice));
        }
        if (minRooms != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("rooms"), minRooms));
        }
        if (maxRooms != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("rooms"), maxRooms));
        }
        if (minBathrooms != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("bathrooms"), minBathrooms));
        }
        if (maxBathrooms != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("bathrooms"), maxBathrooms));
        }
        if (minSquareMeters != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("squareMeters"), minSquareMeters));
        }
        if (maxSquareMeters != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("squareMeters"), maxSquareMeters));
        }
        if (minYear != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("constructionYear"), minYear));
        }
        if (maxYear != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("constructionYear"), maxYear));
        }
        if (hasGarden != null) {
            predicates.add(cb.equal(property.get("hasGarden"), hasGarden));
        }
        if (hasParking != null) {
            predicates.add(cb.equal(property.get("hasParking"), hasParking));
        }
        if (hasBalcony != null) {
            predicates.add(cb.equal(property.get("hasBalcony"), hasBalcony));
        }
        if (hasStorage != null) {
            predicates.add(cb.equal(property.get("hasStorage"), hasStorage));
        }
        if (propertyType != null && !propertyType.isEmpty()) {
            predicates.add(cb.equal(property.get("propertyType"), propertyType));
        }

        query.select(property).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
    // Διαγραφή ακινήτου
    @Transactional
    public void updateProperty(Property property) {
        propertyRepository.save(property);
    }
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }


}
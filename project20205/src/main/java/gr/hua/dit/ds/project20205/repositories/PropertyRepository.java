package gr.hua.dit.ds.project20205.repositories;
import gr.hua.dit.ds.project20205.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Μέθοδος για εύρεση ακινήτων με βάση την κατάσταση
    List<Property> findByStatus(Property.Status status);
}

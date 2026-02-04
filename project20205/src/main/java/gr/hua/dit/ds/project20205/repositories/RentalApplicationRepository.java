package gr.hua.dit.ds.project20205.repositories;
import gr.hua.dit.ds.project20205.entities.RentalApplication;
import gr.hua.dit.ds.project20205.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {
    boolean existsByPropertyId(Long propertyId);
}

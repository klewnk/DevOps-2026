package gr.hua.dit.ds.project20205.controllers;
import gr.hua.dit.ds.project20205.service.UserService;
import gr.hua.dit.ds.project20205.entities.Property;
import gr.hua.dit.ds.project20205.service.PropertyService;
import gr.hua.dit.ds.project20205.service.RentalApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PropertyService propertyService;
    private final RentalApplicationService rentalApplicationService;
    private final UserService userService;

    public AdminController(PropertyService propertyService, RentalApplicationService rentalApplicationService, UserService userService) {
        this.propertyService = propertyService;
        this.rentalApplicationService = rentalApplicationService;
        this.userService = userService;
    }

    // Πίνακας επιλογών του Admin
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/admin-dashboard"; // Συνδέεται με το admin-dashboard.html
    }

    @GetMapping("/admin-dashboard")
    public String redirectToAdminDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/pending-properties")
    public String viewPendingProperties(Model model) {
        System.out.println("Fetching pending properties...");
        model.addAttribute("pendingProperties", propertyService.getPropertiesByStatus(Property.Status.PENDING));
        return "admin/pending-properties"; // Συνδέεται με το pending-properties.html
    }

    // Έγκριση ακινήτου
    @PostMapping("/approve/{id}")
    public String approveProperty(@PathVariable Long id) {
        propertyService.updatePropertyStatus(id, Property.Status.APPROVED);
        return "redirect:/admin/pending-properties";
    }

    // Απόρριψη ακινήτου
    @PostMapping("/reject/{id}")
    public String rejectProperty(@PathVariable Long id) {
        propertyService.updatePropertyStatus(id, Property.Status.REJECTED);
        return "redirect:/admin/pending-properties";
    }

    // Διαχείριση χρηστών
    @GetMapping("/auth/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "auth/users";
    }

    // Διαγραφή χρήστη
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/auth/users";
    }

    @PostMapping("/delete-property/{id}")
    public String deleteProperty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (rentalApplicationService.hasActiveApplication(id)) {
            redirectAttributes.addFlashAttribute("error", "There is an active application for this property. You must reject the application first!");
            return "redirect:/admin/manage-properties";
        }

        propertyService.deleteProperty(id);
        return "redirect:/admin/manage-properties";
    }

    @GetMapping("/manage-properties")
    public String manageProperties(Model model) {
        List<Property> properties = propertyService.getApprovedProperties();
        Map<Long, Boolean> activeApplicationsMap = new HashMap<>();

        for (Property property : properties) {
            boolean hasActiveApplication = rentalApplicationService.hasActiveApplication(property.getId());
            activeApplicationsMap.put(property.getId(), hasActiveApplication);
        }

        model.addAttribute("properties", properties);
        model.addAttribute("activeApplicationsMap", activeApplicationsMap);
        return "admin/manage-properties";
    }

    // Προβολή της φόρμας επεξεργασίας για ένα ακίνητο
    @GetMapping("/edit-property/{id}")
    public String showEditProperty(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        if (property == null) {
            return "error/404"; // Αν το property δεν βρεθεί, επιστρέφουμε error page
        }
        model.addAttribute("property", property);
        return "admin/edit-property"; // ΕΠΙΣΤΡΟΦΗ στο admin/edit-property.html
    }

    // Αποθήκευση αλλαγών στο ακίνητο
    @PostMapping("/edit-property/{id}")
    public String saveEditedProperty(@PathVariable Long id, @ModelAttribute("property") Property property, Model model) {
        Property existingProperty = propertyService.getPropertyById(id);
        if (existingProperty == null) {
            return "error/404";
        }

        // Ενημερώνουμε όλα τα πεδία του ακινήτου
        existingProperty.setTitle(property.getTitle());
        existingProperty.setAddress(property.getAddress());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setRooms(property.getRooms());
        existingProperty.setBathrooms(property.getBathrooms());
        existingProperty.setSquareMeters(property.getSquareMeters());
        existingProperty.setConstructionYear(property.getConstructionYear());
        existingProperty.setPropertyType(property.getPropertyType());
        existingProperty.setHasGarden(property.isHasGarden());
        existingProperty.setHasBalcony(property.isHasBalcony());
        existingProperty.setHasStorage(property.isHasStorage());
        existingProperty.setHasParking(property.isHasParking());

        // Αποθήκευση των αλλαγών
        propertyService.updateProperty(existingProperty);

        return "redirect:/admin/manage-properties"; // Επιστροφή στη λίστα
    }
}








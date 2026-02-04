package gr.hua.dit.ds.project20205.controllers;
import gr.hua.dit.ds.project20205.entities.Property;
import gr.hua.dit.ds.project20205.entities.RentalApplication;
import gr.hua.dit.ds.project20205.service.PropertyService;
import gr.hua.dit.ds.project20205.service.RentalApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/properties") // Κοινό path για όλα τα endpoints
public class PropertyController {


    private final PropertyService propertyService;
    private final RentalApplicationService rentalApplicationService;

    public PropertyController(PropertyService propertyService, RentalApplicationService rentalApplicationService) {
        this.propertyService = propertyService;
        this.rentalApplicationService = rentalApplicationService;
    }

    // Φόρμα προσθήκης ακινήτου
    @GetMapping("/add")
    public String showAddPropertyForm() {
        System.out.println("Navigating to Add Property Page");
        return "properties/add";
    }
    // Υποβολή νέου ακινήτου
    @PostMapping("/add")
    public String addProperty(@ModelAttribute Property property, Model model) {
        propertyService.saveProperty(property);
        model.addAttribute("message", "The property was added successfully and is pending approval!");
        return "redirect:/properties/thank-you"; // Επιστροφή στη λίστα ακινήτων
    }
    @GetMapping("/thank-you")
    public String thankYouPage() {
        return "properties/thank-you"; // Σημείωση: Αυτό αντιστοιχεί στο properties/thank-you.html
    }

    // Προβολή λεπτομερειών ακινήτου
    @GetMapping("/details/{id}")
    public String viewPropertyDetails(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        model.addAttribute("fromAdmin", false); // Δηλώνουμε ότι η κλήση έγινε από κανονικό χρήστη
        return "properties/details"; // Συνδέεται με το templates/properties/details.html
    }

    // Αναζήτηση ακινήτων με φίλτρα
    @GetMapping("/search")
    public String searchProperties(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) Integer minBathrooms,
            @RequestParam(required = false) Integer maxBathrooms,
            @RequestParam(required = false) Integer minSquareMeters,
            @RequestParam(required = false) Integer maxSquareMeters,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Boolean hasGarden,
            @RequestParam(required = false) Boolean hasParking,
            @RequestParam(required = false) Boolean hasBalcony,
            @RequestParam(required = false) Boolean hasStorage,
            @RequestParam(required = false) String propertyType,
            Model model) {

        List<Property> filteredProperties = propertyService.advancedFilter(
                minPrice, maxPrice, minRooms, maxRooms, minBathrooms, maxBathrooms,
                minSquareMeters, maxSquareMeters, minYear, maxYear,
                hasGarden, hasParking, hasBalcony, hasStorage, propertyType);

        model.addAttribute("properties", filteredProperties);
        return "properties/list";
    }

    // Φόρμα υποβολής αίτησης ενοικίασης
    @GetMapping("/apply/{id}")
    public String showApplicationForm(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        model.addAttribute("rentalApplication", new RentalApplication());
        return "properties/application"; // Συνδέεται με το templates/properties/application.html
    }
    @GetMapping("/list")
    public String listProperties(Model model) {
        List<Property> properties = propertyService.getApprovedProperties();
        System.out.println("Approved properties: " + properties); // Debugging log
        model.addAttribute("properties", properties);
        return "properties/list";
    }

}


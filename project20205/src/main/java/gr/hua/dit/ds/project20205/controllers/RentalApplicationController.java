package gr.hua.dit.ds.project20205.controllers;
import gr.hua.dit.ds.project20205.entities.RentalApplication;
import gr.hua.dit.ds.project20205.service.RentalApplicationService;
import gr.hua.dit.ds.project20205.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class RentalApplicationController {

    private final RentalApplicationService rentalApplicationService;

    @Autowired  // 🟢 Προσθήκη αυτής της γραμμής
    public RentalApplicationController(RentalApplicationService rentalApplicationService) {
        this.rentalApplicationService = rentalApplicationService;
        this.userService = userService;
    }
    @Autowired
    private UserService userService;


    @PostMapping("/apply")
    public String submitApplication(@RequestParam Long propertyId, @ModelAttribute RentalApplication application, Model model) {
        rentalApplicationService.saveApplication(propertyId, application);
        model.addAttribute("message", "Your application was submitted successfully!");
        return "redirect:/application-success";
    }

    @GetMapping("/application-success")
    public String applicationSuccess() {
        return "properties/application-success";
    }

    @PostMapping("/admin/delete-application/{id}")
    public String deleteApplication(@PathVariable Long id) {
        rentalApplicationService.deleteApplication(id);
        return "redirect:/applications";  // Επιστρέφουμε στη λίστα με τις αιτήσεις
    }


    @GetMapping("/applications")
    public String showApplications(Model model) {
        List<RentalApplication> applications = rentalApplicationService.getAllApplications();

        for (RentalApplication app : applications) {
            if (app.getProperty() == null) {
                System.out.println("⚠️ Warning: Η αίτηση με ID " + app.getId() + " δεν έχει ακίνητο!");
            }
        }

        model.addAttribute("applications", applications);
        return "properties/applications";
    }

}

package co.edu.icesi.dev.outcome_curr_mgmt.rs.curriculum_definition;

import co.edu.icesi.dev.outcome_curr.mgmt.rs.curriculum_definition.AuthAcadProgCurriculumController;
import co.edu.icesi.dev.outcome_curr_mgmt.service.curriculum_definition.AcadProgCurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthAcadProgCurriculumControllerImpl implements AuthAcadProgCurriculumController {
    private final AcadProgCurriculumService acadProgCurriculumService;

}

package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.AchievementDto;
import dbataev.nextcode.service.AchievementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping(path = "/all")
    public List<AchievementDto> getAll() {
        return achievementService.getAchievements();
    }
}

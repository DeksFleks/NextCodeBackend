package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.ModuleDto;
import dbataev.nextcode.service.ModuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/modules")
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping("/incourse/{id}")
    public List<ModuleDto> findModulesInCourse(@PathVariable Long id, @RequestHeader("Authorization") String authHeader
    ) {
        String jwt = authHeader.replace("Bearer ", "");
        return moduleService.getModulesByCourseId(id, jwt);
    }
}

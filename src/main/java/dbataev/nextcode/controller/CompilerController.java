package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.compiler.CompilerRequest;
import dbataev.nextcode.model.dto.compiler.CompilerResponse;
import dbataev.nextcode.model.dto.judge0.LanguageDto;
import dbataev.nextcode.service.CompilerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/compiler")
public class CompilerController {
    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @GetMapping("/languages")
    public List<LanguageDto> getLanguages() {
        return compilerService.getLanguages();
    }

    @PostMapping("/execute")
    public CompilerResponse execute(HttpServletRequest request,
                                    @RequestBody CompilerRequest compilerRequest) {

        System.out.println("METHOD = " + request.getMethod());
        System.out.println("CONTENT-TYPE = " + request.getContentType());
        System.out.println("REQUEST = " + compilerRequest);

        return compilerService.execute(compilerRequest);
    }
}

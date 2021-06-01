package com.semihbg.filebench.server.api;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.service.BenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;

    @PostMapping("/create")
    public void create(@RequestParam Map<String, MultipartFile> fileMap){

    }

    @GetMapping("/get/{id}")
    public Mono<Bench> getBenchById(@PathVariable("id") String id){
        return benchService.findById(id);
    }



}

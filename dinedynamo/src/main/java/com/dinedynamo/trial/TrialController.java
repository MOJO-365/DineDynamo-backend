package com.dinedynamo.trial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class TrialController {


    @Autowired
    TrialRepository trialRepository;


    @PostMapping("/dinedynamo/try/save")
    public TrialCollection saveTrial(@RequestBody TrialCollection trialCollection){

        trialRepository.save(trialCollection);

        return trialCollection;

    }

    @PostMapping("/dinedynamo/try/getall")
    public List<TrialCollection> getAll(){

        return trialRepository.findAll();
    }

}

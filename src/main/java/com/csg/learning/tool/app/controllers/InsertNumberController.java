package com.csg.learning.tool.app.controllers;

import com.csg.learning.tool.app.calculations.FibonacciSequence;
import com.csg.learning.tool.app.model.CalculationType;
import com.csg.learning.tool.app.model.FibonacciSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;

@Controller
public class InsertNumberController {

    @Autowired(required = false)
    @Qualifier("Plain")
    private FibonacciSequence fibonacciSequencePlain;


    @Autowired(required = false)
    @Qualifier("Recursion")
    private FibonacciSequence fibonacciSequenceRecursion;

    Logger logger = LoggerFactory.getLogger(InsertNumberController.class);

    @GetMapping("/showFibonacci")
    public String showFibonacci(@RequestParam(name = "upperLimit", required = false) Long upperLimit, Model model) {

        upperLimit = upperLimit == null ? 10 : upperLimit;
        model.addAttribute("upperLimit", upperLimit);

        return "showFibonacci";
    }

    @GetMapping("/insertNumber")
    public String countNumbers(Model model) {


        FibonacciSettings fibonacciSettings = new FibonacciSettings(20);
        model.addAttribute(fibonacciSettings);
        model.addAttribute("allTypes", CalculationType.values());
        return "insertNumber";
    }

    /*@PostMapping("/countNumbers", params="action=save")*/
    @RequestMapping(value = "/countNumbers", params = "calculate", method = RequestMethod.POST)
    public String countNumbers(@Valid FibonacciSettings fibonacciSettings, BindingResult result, Model model) {

        Long fib = 0L;
        if (result.hasErrors()) {
            return "insertNumber";
        }
        if (fibonacciSettings.getCalculationType().name().isEmpty() || fibonacciSettings.getCalculationType().equals(CalculationType.PLAIN)) {
            Instant before = Instant.now();
            fibonacciSettings.setFibonacciValue(fibonacciSequencePlain.calculateFibonacciSequence(fibonacciSettings.getNumber()));
            Instant after = Instant.now();
            long delta = Duration.between(before, after).toMillis();
            model.addAttribute("duration", delta);
        } else if (fibonacciSettings.getCalculationType().equals(CalculationType.RECURSION)) {
            Instant before = Instant.now();
            fibonacciSettings.setFibonacciValue(fibonacciSequenceRecursion.calculateFibonacciSequence(fibonacciSettings.getNumber()));
            Instant after = Instant.now();
            long delta = Duration.between(before, after).toMillis();
            model.addAttribute("duration", delta);
        }

        model.addAttribute("fibonacciSettings", fibonacciSettings);

        //model.addAttribute("fibRec", fibRec);
        return "showFibonacci";
    }

    @RequestMapping(value = "/countNumbers", params = "compare", method = RequestMethod.POST)
    public String compare(@Valid FibonacciSettings fibonacciSettings, BindingResult result, Model model) {

        Long fib = 0L;
        if (result.hasErrors()) {
            return "insertNumber";
        }

        Instant before = Instant.now();
        fibonacciSettings.setFibonacciValue(fibonacciSequencePlain.calculateFibonacciSequence(fibonacciSettings.getNumber()));
        Instant after = Instant.now();
        long delta = Duration.between(before, after).toMillis();
        model.addAttribute("durationPlain", delta);

        before = Instant.now();
        if(!fibonacciSettings.getFibonacciValue().equals(fibonacciSequenceRecursion.calculateFibonacciSequence(fibonacciSettings.getNumber()))){

        }

        after = Instant.now();
        delta = Duration.between(before, after).toMillis();
        model.addAttribute("durationRecursion", delta);

        model.addAttribute("fibonacciSettings", fibonacciSettings);

        //model.addAttribute("fibRec", fibRec);
        return "showFibonacciComparison";
    }


}

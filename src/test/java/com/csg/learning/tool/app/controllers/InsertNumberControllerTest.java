package com.csg.learning.tool.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.csg.learning.tool.app.calculations.FibonacciSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(InsertNumberController.class)
public class InsertNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required=false)
    @Qualifier("Plain")
    private FibonacciSequence fibonacciSequencePlain;

    @Autowired(required=false)
    @Qualifier("Recursion")
    private FibonacciSequence fibonacciSequenceRecursion;

    @Test
    public void checkFibonacciSequenceCalculations() throws Exception {
        /*assertThat(fibonacciSequencePlain.calculateFibonacciSequence(10).equals(fibonacciSequenceRecursion.calculateFibonacciSequence(10)));*/
    }






}

package com.csg.learning.tool.app.calculations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("Recursion")
public class FibonacciSequenceRecursion implements FibonacciSequence{

    @Override
    public Long calculateFibonacciSequence(Integer number){

        if(number == 0)
            return 0L;
        else if(number == 1)
            return 1L;
        else
            return calculateFibonacciSequence(number - 1) + calculateFibonacciSequence(number - 2);
    }
}

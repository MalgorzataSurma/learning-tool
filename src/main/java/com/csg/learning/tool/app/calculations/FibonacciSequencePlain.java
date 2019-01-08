package com.csg.learning.tool.app.calculations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("Plain")
public class FibonacciSequencePlain implements FibonacciSequence{

    @Override
    public Long calculateFibonacciSequence(Integer number){

        Long prev=0L, next=1L, result=0L;
        for (int i = 0; i < number-1; i++) {
            result=prev+next;
            prev=next;
            next=result;
        }
        return result;

    }

}

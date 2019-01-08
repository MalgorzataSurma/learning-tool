package com.csg.learning.tool.app.model;

import javax.validation.constraints.NotNull;


public class FibonacciSettings {

    public FibonacciSettings(Integer number){
        this.number=number;
    }

    @NotNull(message = "Number is mandatory")
    private Integer number;

    private Long fibonacciValue;

    private CalculationType calculationType;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getFibonacciValue() {
        return fibonacciValue;
    }

    public void setFibonacciValue(Long fibonacciValue) {
        this.fibonacciValue = fibonacciValue;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

}

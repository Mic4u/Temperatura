package com.example.bmi;

public class TemperatureConverter {


    TemperatureConverter(){}

    double f_to_c (double f){  //konwersja farenheitów na celsjusze
        double c=(5*f-32)/9;

        return c;
    }

    double c_to_f (double c){  //konwersja celsjuszów na farenheity
        double f=(9*c)/5+32;

        return f;


    }

    double f_to_k (double f){  //konwersja farenheitów na kelwiny
        double k=(5*f-32)/9+273;

        return k;


    }

    double k_to_f (double k){  //konwersja kelwinów na farenheity
        double f=(9*k-273)/5+32;

        return f;


    }

    double c_to_k (double c){  //konwersja celsjuszy na kelwiny
        double k=c+273;

        return k;


    }

    double k_to_c (double k){  //konwersja kelwinów na celsjusze
        double c=k-273;

        return c;


    }

}

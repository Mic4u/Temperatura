package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

/*

program Konwerter Temperatur zawiera następującą funkcjonalność

1. zamiana jednostek temperatury (główne zadanie)

2. przyjmowanie inputu zarówno z przecinkiem jak i kropką jako oddzielenie miejsc dziesiętnych

3. przyjmowanie inputu zawierającego symbole jednostek

4. przyjmowanie inputu zawierającego spacje

5. wyświetlanie outputu z rozdzieleniem cyfr kolejnych potęg 10^3

6. wyświetlanie outputu z taką samą liczbą miejsc dziesiętnych co input

7. gdy w inpucie podana została jednostka, sprawdzenie czy ta sama jednostka została wybrana do konwersji, w przeciwnym wypadku program wyswietla wynik oraz ostrzeżenie

*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTemp(View view) {

        EditText poleTemp= (EditText) findViewById(R.id.inTemp);
        Spinner spinner =(Spinner) findViewById(R.id.spinner);
        String jednostki=spinner.getSelectedItem().toString();
        TextView wynik=(TextView) findViewById(R.id.wynik);
        TemperatureConverter tempcon=new TemperatureConverter();

        double result=0;

        String str_temp = poleTemp.getText().toString(); //wzięcie Stringa z inputu
        String unit="";


        str_temp=str_temp.replace(" ", ""); //usunięcie spacji z inputu (często liczby, zwłaszcza wklejane z innych źródeł, mają 1 000 oddzielone spacjami

        if(str_temp.endsWith("f")||str_temp.endsWith("F")||str_temp.endsWith("d")||str_temp.endsWith("D")) str_temp="raczej nikomu nie przyda sie double albo float przy wpisywaniu temperatur xDD";


        String chekker=""; //zmienna która zawiera wprowadzoną jednostkę (jeśli została ona wporwadzona) do późniejszego porównania
        boolean cleaned=false; //zmienna określająca czy input zawierał symbol jednostki

        if(str_temp.length()>2) {  //zbyt krótka temperatura

            String bez_jednostek = str_temp.substring(0, str_temp.length() - 2);

            String koncowka = str_temp.substring(str_temp.length() - 2);

            if (koncowka.equals("°F") || koncowka.equals("oF")) { //warunki sprawdzające czy input zawierał symbol jednostki i jeśli tak, usuwają go
                str_temp = bez_jednostek;
                chekker = "°F";
                cleaned = true;
            } else if (koncowka.equals("°C") || koncowka.equals("oC")) {
                str_temp = bez_jednostek;
                chekker = "°C";
                cleaned = true;
            }
        }

        if (str_temp.endsWith("K")) { //temperatury z K na końcu moga być krótsze, bo K zajmuje tylko jeden znak, a nie dwa jak °F czy °C
            str_temp = str_temp.substring(0, str_temp.length() - 1);
            chekker = "K";
            cleaned = true;
        }


        boolean czy_dziesientna=false; //zmienna określająca czy zmienna jest dziesiętna i czy tym samym kwalifikuje się do określania ilości miejsc dziesiętnych
        String v="#";


        if (str_temp.contains(".")){ //warunek określający czy input zawiera miejsca dziesiętne

            czy_dziesientna=true;

        }else if (str_temp.contains(",")) {

            czy_dziesientna=true;
            str_temp=str_temp.replace(",", "."); //zamieniana przecinków na kropki, tak by aplikacja mogła je zczytać
        }


        try {
            double temperatura=Double.parseDouble(str_temp);

            if(czy_dziesientna) v = dokladnosc(temperatura); //ustalenie ilości miejsc dziesiętnych, jeśli liczba jest dziesiętna, za pomocą metody dokladnosc

            DecimalFormat format=new DecimalFormat(v); //jeśli input jest całkowity format temperatury to #, czyli również całkowity. Ilość miejsc po przecinku inputu i outputu jest taka sama

            if (jednostki.equals("°C -> °F")) {
                result = tempcon.c_to_f(temperatura); //wywołanie metody liczącej temperaturę w innych jednostkach
                unit="°F"; //przypisanie jednostki outputu
                if (cleaned && !chekker.equals("°C")) chekker="\n"+getString(R.string.sprawdzanie); //jeśli jednostka została podana w inpucie, ale nie zgadza się z wybraną z menu program wyświetla ostrzeżenie
                else chekker="";//jeśli jednostka się zgadza, lub nie została podana, program nie wyswietla ostrzeżenia
            }                   //kolejne warunki działają analogicznie
            else if (jednostki.equals("°F -> °C")) {
                result = tempcon.f_to_c(temperatura);
                unit="°C";
                if (cleaned && !chekker.equals("°F")) chekker="\n"+getString(R.string.sprawdzanie);
                else chekker="";
                }
            else if (jednostki.equals("°F -> K")) {
                result = tempcon.f_to_k(temperatura);
                unit="K";
                if (cleaned && !chekker.equals("°F")) chekker="\n"+getString(R.string.sprawdzanie);
                else chekker="";
                    }
            else if (jednostki.equals("K -> °F")) {
                result = tempcon.k_to_f(temperatura);
                unit="°F";
                if (cleaned && !chekker.equals("K")) chekker="\n"+getString(R.string.sprawdzanie);
                else chekker="";
                        }
            else if (jednostki.equals("K -> °C")) {
                result = tempcon.k_to_c(temperatura);
                unit="°C";
                if (cleaned && !chekker.equals("K")) chekker="\n"+getString(R.string.sprawdzanie);
                else chekker="";
                            }
            else if (jednostki.equals("°C -> K")) {
                result = tempcon.c_to_k(temperatura);
                unit="K";
                if (cleaned && !chekker.equals("°C")) chekker="\n"+getString(R.string.sprawdzanie);
                else chekker="";
                                }

            String bez_spacji = format.format(result); //zapisanie wyniku do zmiennej do której będą dodawane rozdzielenia między tysiącami, już po sformatowaniu do właściwej ilości miejsc zerowych

            bez_spacji=dodaj_spacje(bez_spacji); //zastosowanie metody dodającej rozdzielenia miedzy tysiącami

            wynik.setText(bez_spacji+unit+chekker);  //wysłanie wyniku do użytkownika

        } catch(NumberFormatException e) {
            wynik.setText(getString(R.string.blad)); //w przypadku gdy, nawet po wstępnym przefiltrowaniu, tekst zaweira znaki nie będace cyframi, program wyświetla komunikat o nieprawidłowym inpucie

        }



    }

    private String dodaj_spacje(String bez_spacji) {  //metoda dodająca spację miedzy cyframi kolejnych potęg 10^3

        String calkowite; //string przechowujący cyfry części całkowitej
        String dziesientne; //string przechowujący cyfry części dziesiętnej

       if(bez_spacji.contains(",")) { //warunek sprawdzający czy liczba w stringu jest dziesiętna
           calkowite = bez_spacji.substring(0,bez_spacji.indexOf(","));
           dziesientne = bez_spacji.substring(bez_spacji.indexOf(","));
       } else{
           calkowite=bez_spacji;
           dziesientne = "";

       }

        if(calkowite.length()>3){  //jeśli część całkowita zawiera więcej niż 3 cyfry zostaną dodane spacje

            StringBuilder spacje = new StringBuilder();

            for(int i=calkowite.length()/3;i>0;i--){ //dodanie po trzech cyfr od końca w każdym przejściu i spacji
                int k=3*i;
                if(calkowite.length()%3==0) k=3*i-1;

            spacje.append(calkowite.charAt(k));
            spacje.append(calkowite.charAt(k-1));
            spacje.append(calkowite.charAt(k-2));
            spacje.append(" ");
            }

            for(int i=calkowite.length()%3;i>0;i--){ //dodanie cyfr z pierwszej, niepełnej "trójki"

                spacje.append(calkowite.charAt(i-1));

            }

            if(spacje.toString().endsWith(" ")){ //jeśli wszystkie trójki są pełne, usunięcie nadprogramowej spacji

                spacje.deleteCharAt(spacje.length()-1);

            }

            spacje.reverse(); //odwrócenie ciągu znaków do właściwej postaci

            spacje.append(dziesientne); //dodanie miejsc dziesietnych

            return spacje.toString();

        }else{

            return bez_spacji; //jeśli część całkowita jest krótsza niż 4 cyfry, metoda nie ma do wprowadzenia żadnych zmian

        }

    }

    private String dokladnosc(double d) { //metoda kopiująca dokładność inputu dla outputu

        String str = String.valueOf(d);
        char sep= ".".charAt(0); //określenie separatora
        str=str.substring(str.indexOf(sep)+1); //wycięcie miejsc dziesiętnych



        StringBuilder hash = new StringBuilder(); //string składający sie z samych znaków #, będących inputem dla obiektu DecimalFormat
        hash.append("#."); //początkoy znak
        for(int i=0;i<str.length();i++){ //dodanie tylu znaków # ile jest miejsc dziesietnych

            hash.append("#");
        }

        return hash.toString();
    }
}
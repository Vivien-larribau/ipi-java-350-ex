package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    //Employé dateEmbauche avec date 2 ans avant aujourd'hui =>
    //2 année d'ancienneté

    @Test
    public void testAncienneDateEmbaucheNmoins2() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(2));

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(2);
    }

    //Date dans le futur
    @Test
    public void testAncienneDateEmbaucheNplus2() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    //Date aujourd'hui => 0
    @Test
    public void testAncienneDateEmbaucheNow() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }
    //Date d'embauche indéfinie => 0
    @Test
    public void testAncienneDateEmbaucheIndefinie() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    //Prime annuelle
    @ParameterizedTest
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 2, 0.5, 600.0",
            "1, 'T12345', 2, 1.0, 1200.0",
            "2, 'T12345', 0, 1.0, 2300.0",
            "2, 'T12345', 1, 1.0, 2400.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "1, 'M12345', 5, 1.0, 2200.0",
            "2, 'M12345', 0, 1.0, 1700.0",
            "2, 'M12345', 8, 1.0, 2500.0"
    })
    public void getPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle){
        //Given
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbYearsAnciennete), Entreprise.SALAIRE_BASE, performance, tempsPartiel);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(prime);

    }

    //Augmenter salaire avec un salaire de 0
    @Test
    public void testAugmenterSalaire0d() {
        //Given
        Employe employe = new Employe();
        employe.setSalaire(0d);

        //When
        employe.augmenterSalaire(0.2);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(0);
    }

    //Augmenter salaire null en augmentant de 0.1
    @Test
    public void testAugmenterSalaireNulld() {
        //Given
        Employe employe = new Employe();
        employe.setSalaire(null);

        //When
        employe.augmenterSalaire(0.1);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(null);
    }

    //Augmenter salaire N en augmentant de 0.0
    @Test
    public void testAugmenterSalaireNd() {
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        employe.augmenterSalaire(0.0);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1000d);
    }

    //Augmenter salaire cas nominal
    @Test
    public void testAugmenterSalaireNominal() {
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1500d);

        //When
        employe.augmenterSalaire(0.9);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2850d);
    }
}

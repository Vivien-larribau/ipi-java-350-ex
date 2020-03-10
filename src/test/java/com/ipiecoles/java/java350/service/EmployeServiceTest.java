package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {


    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    //..
    @Test
    public void testEmbaucheEmploye() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //findLastMatricule => 00345 / null
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("00345");
        //findByMatricule => null
        Mockito.when(employeRepository.findByMatricule("C00346")).thenReturn(null);

        //Then
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //When
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getMatricule()).isEqualTo("C00346");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
        //1521.22 * 1.2 * 1.0
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
    }

    public void testEmbaucheEmployeLimiteMatricule() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //findLastMatricule => 00345 / null
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When
        try{
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("Aurait du planter !");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }
    }

    //Test performance Matricule null
    @Test
    public void testPerformanceMatriculeNull() {
        //Given
        String matricule = null;
        Long caTraite = 1000l;
        Long objectifCa = 1000l;

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("Aurait du planter !");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }

    //Test performance caTraite null
    @Test
    public void testPerformancecaTraite() {
        //Given
        String matricule = "C12345";
        Long caTraite = null;
        Long objectifCa = 1000l;

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("Aurait du planter !");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
    }
    //Test performance objectifCa null
    @Test
    public void testPerformanceObjectifCa() {
        //Given
        String matricule = "C12345";
        Long caTraite = 1000l;
        Long objectifCa = null;

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("Aurait du planter !");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }

    //Test performance Faux Matriule
    @Test
    public void testPerformanceFauxMatricule() {
        //Given
        String matricule = "C123456";
        Long caTraite = 1000l;
        Long objectifCa = 1000l;

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("Aurait du planter !");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule " + matricule + " n'existe pas !");
        }
    }

    //Test Performance
    @ParameterizedTest
    @CsvSource({
            "81, 100, 4, 1",
            "95, 100, 6, 1",
            "106, 100, 7, 1",
            "200, 100, 10, 1",

            "81, 100, 3, 20",
            "95, 100, 5, 20",
            "106, 100, 6, 20",
            "200, 100, 9, 20",
    })
    public void testPerf(Long caTraite, Long objectifCa, Integer result, Double avgPerf) throws EmployeException {
        //Given
        Employe emp = new Employe();
        emp.setPerformance(5);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(avgPerf);
        Mockito.when(employeRepository.findByMatricule("C12345")).thenReturn(emp);

        //When
        employeService.calculPerformanceCommercial("C12345", caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getPerformance()).isEqualTo(result);

    }


}

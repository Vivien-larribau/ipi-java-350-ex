package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class) //Junit 5
@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;



    // testing //

    @Test
    public void testFindLastMatricule() {
        //Given

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    public void testFindLastMatricule2employes() {
        //Given
        Employe employe1 = new Employe("doe","Jhon","M12345", LocalDate.now(),1500d,1,1.0);
        Employe employe2 = new Employe("doe","Jhon","M66666", LocalDate.now(),1500d,1,1.0);

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }
}

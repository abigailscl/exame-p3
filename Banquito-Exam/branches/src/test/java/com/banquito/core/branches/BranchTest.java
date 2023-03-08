package com.banquito.core.branches;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import com.banquito.core.branches.service.BranchService;

@SpringBootTest
public class BranchTest {
    @Test
    public void testGetAll() {
        // Paso 1: Crear un objeto Branch para usar en la prueba
        Branch branch = new Branch();
        branch.setId("1");
        branch.setCode("001");
        branch.setName("Branch 1");

        // Paso 2: Crear un objeto List<Branch> que contenga el objeto Branch creado anteriormente
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);

        // Paso 3: Crear un objeto mock del repositorio de Branch usando Mockito
        BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

        // Paso 4: Configurar el comportamiento del objeto mock del repositorio para que devuelva la lista de Branch creada anteriormente cuando se llame al método findAll()
        Mockito.when(branchRepository.findAll()).thenReturn(branches);

        // Paso 5: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
        BranchService branchService = new BranchService(branchRepository);

        // Paso 6: Llamar al método getAll() del servicio y almacenar el resultado en una variable
        List<Branch> result = branchService.getAll();

        // Paso 7: Verificar que el método findAll() del objeto mock del repositorio se llamó exactamente una vez
        Mockito.verify(branchRepository, Mockito.times(1)).findAll();

        // Paso 8: Verificar que el resultado devuelto por el método getAll() del servicio es igual a la lista de Branch creada anteriormente
        assertEquals(branches, result);
    }

    // @Test
    // public void lookById() throws CRUDException {
    //     // Paso 1: Crear un objeto Branch para usar en la prueba
    //     Branch branch = new Branch();
    //     branch.setId("1");
    //     branch.setCode("001");
    //     branch.setName("Branch 1");

    //     // Paso 2: Crear un objeto List<Branch> que contenga el objeto Branch creado anteriormente
    //     List<Branch> branches = new ArrayList<>();
    //     branches.add(branch);

    //     // Paso 3: Crear un objeto mock del repositorio de Branch usando Mockito
    //     BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    //     // Paso 4: Configurar el comportamiento del objeto mock del repositorio para que devuelva la lista de Branch creada anteriormente cuando se llame al método findAll()
    //     Mockito.when(branchRepository.findById("1")).equals(branch);

    //     // Paso 5: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    //     BranchService branchService = new BranchService(branchRepository);

    //     // Paso 6: Llamar al método getAll() del servicio y almacenar el resultado en una variable
    //     Branch result = branchService.lookById("1");

    //     // Paso 7: Verificar que el método findAll() del objeto mock del repositorio se llamó exactamente una vez
    //     Mockito.verify(branchRepository, Mockito.times(1)).findAll();

    //     // Paso 8: Verificar que el resultado devuelto por el método getAll() del servicio es igual a la lista de Branch creada anteriormente
    //     assertEquals(branches, result);
    // }

    // @Test
    // public void lookByCode() {
    //     Branch branch = new Branch();
    //     branch.setId("1");
    //     branch.setCode("001");
    //     branch.setName("Branch 1");


    //     BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    //     Mockito.when(branchRepository.findByCode("001")).equals(branch);

    //     BranchService branchService = new BranchService(branchRepository);

    //     Branch result = new Branch();
    //     result = branchService.lookByCode("001");

    //     Mockito.verify(branchRepository, Mockito.times(1)).findAll();
    //     assertEquals(branch, result);
    // }
}

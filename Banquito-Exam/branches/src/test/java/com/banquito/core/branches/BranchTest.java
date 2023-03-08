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

    @Test
public void testLookById() throws CRUDException {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que devuelva el objeto Branch creado anteriormente cuando se llame al método findById() con el id correspondiente
    Mockito.when(branchRepository.findById("1")).thenReturn(Optional.of(branch));

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Llamar al método lookById() del servicio con el id correspondiente y almacenar el resultado en una variable
    Branch result = branchService.lookById("1");

    // Paso 6: Verificar que el método findById() del objeto mock del repositorio se llamó exactamente una vez con el id correspondiente
    Mockito.verify(branchRepository, Mockito.times(1)).findById("1");

    // Paso 7: Verificar que el resultado devuelto por el método lookById() del servicio es igual al objeto Branch creado anteriormente
    assertEquals(branch, result);
}

@Test
public void testLookByIdWithException() {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que devuelva el objeto Branch creado anteriormente cuando se llame al método findById()
    Mockito.when(branchRepository.findById("1")).thenReturn(Optional.of(branch));

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Llamar al método lookById() del servicio con un id que no existe y esperar una excepción CRUDException
    String id = "2";
    Exception exception = assertThrows(CRUDException.class, () -> {
        branchService.lookById(id);
    });

    // Paso 6: Verificar que la excepción tiene el código de error 404
    assertEquals("Branch with id", exception.getMessage().split(":")[0].trim());

    // Paso 7: Verificar que el método findById() del objeto mock del repositorio se llamó exactamente una vez
    Mockito.verify(branchRepository, Mockito.times(1)).findById("2");
}

@Test
public void testLookByCode() {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que devuelva el objeto Branch creado anteriormente cuando se llame al método findByCode() con el código correspondiente
    Mockito.when(branchRepository.findByCode("001")).thenReturn(branch);

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Llamar al método lookByCode() del servicio con el código correspondiente y almacenar el resultado en una variable
    Branch result = branchService.lookByCode("001");

    // Paso 6: Verificar que el método findByCode() del objeto mock del repositorio se llamó exactamente una vez con el código correspondiente
    Mockito.verify(branchRepository, Mockito.times(1)).findByCode("001");

    // Paso 7: Verificar que el resultado devuelto por el método lookByCode() del servicio es igual al objeto Branch creado anteriormente
    assertEquals(branch, result);
}



@Test
public void testCreate() throws CRUDException {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 4: Configurar el comportamiento del objeto mock del repositorio para que no haga nada cuando se llame al método save()
    Mockito.when(branchRepository.save(branch)).thenReturn(branch);

    // Paso 5: Llamar al método create() del servicio
    branchService.create(branch);

    // Paso 6: Verificar que el método save() del objeto mock del repositorio se llamó exactamente una vez con el objeto Branch creado en el Paso 1
    Mockito.verify(branchRepository, Mockito.times(1)).save(branch);
}

@Test
public void testCreateExcepcion() {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que lance una excepción cuando se llame al método save()
    Mockito.doThrow(new RuntimeException("Error en el repositorio")).when(branchRepository).save(branch);

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Llamar al método create() del servicio y verificar que lanza una excepción CRUDException con el mensaje de error esperado
    Exception exception = assertThrows(CRUDException.class, () -> {
        branchService.create(branch);
    });
    assertEquals("Branch cannot be created, error:Error en el repositorio", exception.getMessage());
}

@Test
public void testUpdate() throws CRUDException {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que devuelva el objeto Branch creado anteriormente cuando se llame al método findByCode()
    Mockito.when(branchRepository.findByCode(branch.getCode())).thenReturn(branch);

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Llamar al método update() del servicio
    String code = "001";
    Branch branchToUpdate = new Branch();
    branchToUpdate.setName("Updated Branch 1");
    branchService.update(code, branchToUpdate);

    // Paso 6: Verificar que el método findByCode() del objeto mock del repositorio se llamó exactamente una vez con el mismo parámetro que se pasó al método update() del servicio
    Mockito.verify(branchRepository, Mockito.times(1)).findByCode(code);

    // Paso 7: Verificar que se llamó al método save() del objeto mock del repositorio con el objeto Branch actualizado
    Mockito.verify(branchRepository, Mockito.times(1)).save(branch);

    // Paso 8: Verificar que el nombre del objeto Branch se actualizó correctamente
    assertEquals("Updated Branch 1", branch.getName());
}

@Test
public void testUpdateExcepxion() throws CRUDException {
    // Paso 1: Crear un objeto Branch para usar en la prueba
    Branch branch = new Branch();
    branch.setId("1");
    branch.setCode("001");
    branch.setName("Branch 1");

    // Paso 2: Crear un objeto mock del repositorio de Branch usando Mockito
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);

    // Paso 3: Configurar el comportamiento del objeto mock del repositorio para que devuelva el objeto Branch creado anteriormente cuando se llame al método findByCode()
    Mockito.when(branchRepository.findByCode(branch.getCode())).thenReturn(branch);

    // Paso 4: Crear una instancia de su servicio de Spring Boot, pasando el objeto mock del repositorio como parámetro
    BranchService branchService = new BranchService(branchRepository);

    // Paso 5: Crear un objeto Branch con los datos a actualizar
    Branch updatedBranch = new Branch();
    updatedBranch.setName("Branch Updated");

    // Paso 6: Llamar al método update() del servicio con los datos a actualizar
    branchService.update(branch.getCode(), updatedBranch);

    // Paso 7: Verificar que el método findByCode() del objeto mock del repositorio se llamó exactamente una vez
    Mockito.verify(branchRepository, Mockito.times(1)).findByCode(branch.getCode());

    // Paso 8: Verificar que el objeto Branch devuelto por el método findByCode() del objeto mock del repositorio tiene el nuevo nombre
    assertEquals(updatedBranch.getName(), branch.getName());

    // Paso 9: Verificar que el método save() del objeto mock del repositorio se llamó exactamente una vez
    Mockito.verify(branchRepository, Mockito.times(1)).save(branch);

    // Paso 10: Verificar que el método update() del servicio lanza una excepción CRUDException si se intenta actualizar un objeto Branch que no existe
    Branch nonExistingBranch = new Branch();
    nonExistingBranch.setCode("002");
    Mockito.when(branchRepository.findByCode(nonExistingBranch.getCode())).thenReturn(null);

    Exception exception = assertThrows(CRUDException.class, () -> {
        branchService.update(nonExistingBranch.getCode(), updatedBranch);
    });
    assertEquals("Branch cannot be updated, error:Branch with code: {002} does not exist", exception.getMessage());
}


}

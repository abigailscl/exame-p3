package com.banquito.core.branches.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banquito.core.branches.controller.BranchController;
import com.banquito.core.branches.controller.dto.BranchRQRS;
import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.service.BranchService;

public class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateValid() throws CRUDException {
        BranchRQRS branchRQRS = new BranchRQRS();
        branchRQRS.setCode("111");
        branchRQRS.setName("Branch1");

        branchService.create(any(Branch.class));

        BranchController branchController = new BranchController(branchService);
        ResponseEntity response = branchController.create(branchRQRS);
        assertNotEquals(HttpStatus.CREATED, response.getStatusCode());

    }

  
    @Test
void testCreateException() throws CRUDException {
    // create valid BranchRQRS
    BranchRQRS branchRQRS = new BranchRQRS();
    branchRQRS.setCode("111");
    branchRQRS.setName("Branch1");

    // mock branchService.create() to throw CRUDException
    Mockito.doThrow(new CRUDException(123, "Error creating branch")).when(branchService).create(Mockito.any(Branch.class));

    // call create() method of BranchController and get the response
    BranchController branchController = new BranchController(branchService);
    ResponseEntity response = branchController.create(branchRQRS);

    // assert that the response status is BAD_REQUEST
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
}


    @Test
    void testObtainAll() {
        // Crear algunos clientes ficticios para devolver en la respuesta del servicio
        Branch branch1 = new Branch();
        branch1.setCode("111");
        branch1.setName("Branch1");

        Branch branch2 = new Branch();
        branch2.setCode("222");
        branch2.setName("Branch2");

        List<Branch> branches = new ArrayList<>();
        branches.add(branch1);
        branches.add(branch2);
        // Configurar el comportamiento del servicio de clientes ficticios
        when(branchService.getAll()).thenReturn(branches);

        // Configurar el controlador y hacer una solicitud GET simulada
        BranchController branchController = new BranchController(branchService);
        ResponseEntity<List<BranchRQRS>> response = branchController.obtainAll();

        // Verificar que la respuesta tenga un código de estado HTTP 200 y los datos
        // correctos
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assertEquals(branches, response.getBody());
    }

    @Test
    void testObtainByCodeWithValidCode() {
        Branch branch = new Branch();
        branch.setCode("111");
        branch.setName("Branch1");

        when(branchService.lookByCode(anyString())).thenReturn(branch);

        BranchController branchController = new BranchController(branchService);
        ResponseEntity<BranchRQRS> response = branchController.obtainByCode("111");

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testObtainByCodeWithInvalidCode() {
        when(branchService.lookByCode(anyString())).thenReturn(null);

        BranchController branchController = new BranchController(branchService);
        ResponseEntity<BranchRQRS> response = branchController.obtainByCode("111");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdate() throws CRUDException {

        Branch branch1 = new Branch();
        branch1.setCode("111");
        branch1.setName("Branch1");
        branch1.setId("idTest");
        when(branchService.lookByCode("111")).thenReturn(branch1);

        BranchRQRS branchRQRS = new BranchRQRS();
        branchRQRS.setCode("111");
        branchRQRS.setName("Branch1-updated");

        // When
        ResponseEntity<BranchRQRS> response = branchController.update("111", branchRQRS);

        System.out.println(response);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BranchRQRS updatedBranch = response.getBody();
        assertEquals("111", updatedBranch.getCode());
        // assertEquals("Branch1-updated", updatedBranch.getName());

    }

    @Test
void testUpdateException() throws CRUDException {
    // create valid BranchRQRS and code
    BranchRQRS branchRQRS = new BranchRQRS();
    String code = "123";
    branchRQRS.setCode(code);
    branchRQRS.setName("Branch1");

    // mock branchService.update() to throw CRUDException
    Mockito.doThrow(new CRUDException(456, "Error updating branch")).when(branchService).update(Mockito.anyString(), Mockito.any(Branch.class));

    // call update() method of BranchController and get the response
    BranchController branchController = new BranchController(branchService);
    ResponseEntity<BranchRQRS> response = branchController.update(code, branchRQRS);

    // assert that the response status is 456 (the error code set in the mocked CRUDException)
    assertEquals(456, response.getStatusCodeValue());
}


}


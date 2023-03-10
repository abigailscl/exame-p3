package com.banquito.core.branches.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;


public class BranchServiceTest {
    private BranchService branchService;

    @Mock
    private Logger logger;

    @Mock
    private BranchRepository branchRepositoryMock;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        branchService = new BranchService(branchRepositoryMock);
    }

    @Test
    void testCreate() throws CRUDException {
        Branch branch = new Branch();
        branch.setCode("01");
        branch.setName("BranchTest");
        when(branchRepositoryMock.save(branch)).thenReturn(branch);
        branchService.create(branch);
        verify(branchRepositoryMock, times(1)).save(branch);

    }

    @Test
    public void testCreateThrowsException() throws Exception {
        Branch branch = new Branch();
        branch.setCode("B001");

        doThrow(new RuntimeException("Test exception")).when(branchRepositoryMock).save(branch);

        CRUDException thrown = assertThrows(CRUDException.class, () -> {
            branchService.create(branch);
        });

        String expectedMessage = "Branch cannot be created, error:Test exception";
        
        assertEquals(expectedMessage, thrown.getMessage());
    }


    @Test
    void testGetAll() {
        List<Branch> branches = new ArrayList<Branch>();
        Branch branch1 = new Branch();
        branch1.setCode("01");
        branch1.setName("BranchTest");
        branches.add(branch1);
        Branch branch2 = new Branch();
        branch2.setCode("222");
        branch2.setName("BranchTest2");
        branches.add(branch2);
        when(branchRepositoryMock.findAll()).thenReturn(branches);

        List<Branch> result = branchService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("01", result.get(0).getCode());
        assertEquals("222", result.get(1).getCode());
        verify(branchRepositoryMock, times(1)).findAll();
    }

    @Test
    void testLookByCode() {
        String code = "01";
        Branch branch = new Branch();
        branch.setCode("01");
        branch.setName("BranchTest");
        when(branchRepositoryMock.findByCode(code)).thenReturn(branch);

        Branch result = branchService.lookByCode(code);

        assertNotNull(result);
        assertEquals("01", result.getCode());
        assertEquals("BranchTest", result.getName());
        verify(branchRepositoryMock, times(1)).findByCode(code);
    }

    @Test
    void testLookById() throws CRUDException {
        String id = "01";
        Optional<Branch> branch = Optional.of(new Branch());
        branch.get().setId(id);
        branch.get().setCode("222");
        branch.get().setName("BranchTest");
        when(branchRepositoryMock.findById(id)).thenReturn(branch);

        Branch result = branchService.lookById(id);

        assertNotNull(result);
        assertEquals("222", result.getCode());
        assertEquals("BranchTest", result.getName());

        verify(branchRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void testLookByIdThrowsException() {
        String id = "1";
        when(branchRepositoryMock.findById(id)).thenReturn(Optional.empty());

        CRUDException thrown = assertThrows(CRUDException.class, () -> {
            branchService.lookById(id);
        });

        int expectedStatusCode = 404;
        String expectedMessage = "Branch with id: {" + id + "} does not exist";
        
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void testUpdate() throws CRUDException {
        ArgumentCaptor<Branch> argument = ArgumentCaptor.forClass(Branch.class);

        String name = "branch2";
        String code = "222";
        Branch branch = new Branch();

        branch.setCode("222");
        branch.setName("branch2");
        when(branchRepositoryMock.findByCode(any(String.class))).thenReturn(branch);

        branchService.update(code, branch);
        verify(branchRepositoryMock).save(argument.capture());
        assertEquals(name, argument.getValue().getName());

    }

    @Test
    void testUpdateWithNonExistingBranch() throws CRUDException {
        // Arrange
        String code = "222";
        Branch branch = new Branch();
        branch.setCode("222");
        branch.setName("branch2");
        when(branchRepositoryMock.findByCode(code)).thenReturn(null);
        
        // Act and Assert
        try {
            branchService.update(code, branch);
            fail("Expected CRUDException to be thrown");
        } catch (CRUDException e) {
            assertEquals(520, e.getErrorCode());
            assertEquals("Branch cannot be updated, error:Branch with code: {222} does not exist", e.getMessage());
        }
    }

}

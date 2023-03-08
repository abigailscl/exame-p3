package com.banquito.core.branches;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import com.banquito.core.branches.service.BranchService;

public class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLookByIdSuccess() throws CRUDException {
        String id = "1";
        Branch branch = new Branch();
        branch.setId(id);
        branch.setName("Test Branch");

        // Mock the branch repository to return the branch when findById() is called
        when(branchRepository.findById(id)).thenReturn(Optional.of(branch));

        // Call the service method
        Branch result = branchService.lookById(id);

        // Verify the result
        assertEquals(branch, result);
    }

    @Test
    public void testLookByIdNotFound() {
        String id = "1";

        // Mock the branch repository to return an empty Optional when findById() is called
        when(branchRepository.findById(id)).thenReturn(Optional.empty());

        // Call the service method and expect an exception to be thrown
        Exception exception = assertThrows(CRUDException.class, () -> branchService.lookById(id));

        // Verify the exception message and status code
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        assertEquals("Branch with id: {" + id + "} does not exist", exception.getMessage());
    }
}
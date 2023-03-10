package com.banquito.core.productsaccounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banquito.core.productsaccounts.controller.dto.ProductAccountRQRS;
import com.banquito.core.productsaccounts.controller.mapper.ProductAccountMapper;
import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.ProductAccount;
import com.banquito.core.productsaccounts.repository.ProductAccountRepository;
import com.banquito.core.productsaccounts.service.ProductAccountService;

public class ProductAccountControllerTest {
    
    @Mock
    private ProductAccountService productAccountService;

    @InjectMocks
    private ProductAccountController productAccountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testObtainAll() {
        List<ProductAccount> productAccounts = new ArrayList<>();
        ProductAccount productAccount1 = new ProductAccount();
        productAccount1.setId("001");
        productAccount1.setName("Product1");
        productAccount1.setDescription("Product 1 description");
        productAccount1.setMinimunBalance(new BigDecimal("100.00"));
        productAccount1.setPayInterest("Y");
        productAccount1.setAcceptsChecks("N");
        productAccount1.setState("ACT");
        productAccount1.setCreationDate(new Date());
        productAccounts.add(productAccount1);
        
        ProductAccount productAccount2 = new ProductAccount();
        productAccount2.setId("002");
        productAccount2.setName("Product2");
        productAccount2.setDescription("Product 2 description");
        productAccount2.setMinimunBalance(new BigDecimal("500.00"));
        productAccount2.setPayInterest("N");
        productAccount2.setAcceptsChecks("Y");
        productAccount2.setState("INA");
        productAccount2.setCreationDate(new Date());
        productAccounts.add(productAccount2);

        when(productAccountService.listAllActives()).thenReturn(productAccounts);

        ResponseEntity<List<ProductAccountRQRS>> response = productAccountController.obtainAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ProductAccountRQRS> responseList = response.getBody();
        assertNotNull(responseList);
        assertEquals(2, responseList.size());

        ProductAccountRQRS responseProductAccount1 = responseList.get(0);
        assertEquals("001", responseProductAccount1.getId());
        assertEquals("Product1", responseProductAccount1.getName());
        assertEquals("Product 1 description", responseProductAccount1.getDescription());
        assertEquals(new BigDecimal("100.00"), responseProductAccount1.getMinimunBalance());
        assertEquals("Y", responseProductAccount1.getPayInterest());
        assertEquals("N", responseProductAccount1.getAcceptsChecks());
        assertEquals("ACT", responseProductAccount1.getState());

        ProductAccountRQRS responseProductAccount2 = responseList.get(1);
        assertEquals("002", responseProductAccount2.getId());
        assertEquals("Product2", responseProductAccount2.getName());
        assertEquals("Product 2 description", responseProductAccount2.getDescription());
        assertEquals(new BigDecimal("500.00"), responseProductAccount2.getMinimunBalance());
        assertEquals("N", responseProductAccount2.getPayInterest());
        assertEquals("Y", responseProductAccount2.getAcceptsChecks());
        assertEquals("INA", responseProductAccount2.getState());
    }

    
    @Test
    void testObtainByCode() {
        String id = "001";
        ProductAccount productAccount = new ProductAccount();
        productAccount.setId(id);
        productAccount.setName("Product1");
        productAccount.setDescription("Product 1 description");
        productAccount.setMinimunBalance(new BigDecimal("100.00"));
        productAccount.setPayInterest("Y");
        productAccount.setAcceptsChecks("N");
        productAccount.setState("ACT");
        productAccount.setCreationDate(new Date());

        when(productAccountService.obtainById(id)).thenReturn(productAccount);

        ResponseEntity<ProductAccountRQRS> response = productAccountController.obtainByCode(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductAccountRQRS responseProductAccount = response.getBody();
        assertNotNull(responseProductAccount);
        assertEquals(id, responseProductAccount.getId());
        assertEquals("Product1", responseProductAccount.getName());
        assertEquals("Product 1 description", responseProductAccount.getDescription());
        assertEquals(new BigDecimal("100.00"), responseProductAccount.getMinimunBalance());
        assertEquals("Y", responseProductAccount.getPayInterest());
        assertEquals("N", responseProductAccount.getAcceptsChecks());
        assertEquals("ACT", responseProductAccount.getState());
    }

    @Test
    void testObtainByCodeWithInvalidId() {
        String id = "invalid-id";

        when(productAccountService.obtainById(id)).thenReturn(null);

        ResponseEntity<ProductAccountRQRS> response = productAccountController.obtainByCode(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
void testCreateSuccess() {
    ProductAccountRQRS productAccount = ProductAccountRQRS.builder()
        .name("Product1")
        .description("Product 1 description")
        .minimunBalance(new BigDecimal("100.00"))
        .payInterest("Y")
        .acceptsChecks("N")
        .state("ACT")
        .build();
    
    ResponseEntity<?> response = productAccountController.create(productAccount);
    assertEquals(HttpStatus.OK, response.getStatusCode());

}

@Test
    void testCreateProductAccount() {
        ProductAccountRQRS productAccountRQRS = ProductAccountRQRS.builder()
            .id("001")
            .name("Product1")
            .description("Product 1 description")
            .minimunBalance(new BigDecimal("100.00"))
            .payInterest("Y")
            .acceptsChecks("N")
            .state("ACT")
            .build();

        ProductAccount productAccount = ProductAccountMapper.mapToProductAccount(productAccountRQRS);
        doNothing().when(productAccountService).create(productAccount);

        ResponseEntity<?> response = productAccountController.create(productAccountRQRS);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

@Test
    void testCreateProductAccountWithException() {
        ProductAccountRQRS productAccountRQRS = ProductAccountRQRS.builder()
            .id("001")
            .name("Product1")
            .description("Product 1 description")
            .minimunBalance(new BigDecimal("100.00"))
            .payInterest("Y")
            .acceptsChecks("N")
            .state("ACT")
            .build();

        ProductAccount productAccount = ProductAccountMapper.mapToProductAccount(productAccountRQRS);
        doThrow(new CRUDException(500, "Error creating product account")).when(productAccountService).create(productAccount);

        ResponseEntity<?> response = productAccountController.create(productAccountRQRS);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}

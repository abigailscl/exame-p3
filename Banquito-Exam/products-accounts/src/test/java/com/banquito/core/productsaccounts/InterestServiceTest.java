package com.banquito.core.productsaccounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.InterestRate;
import com.banquito.core.productsaccounts.repository.InterestRateRepository;
import com.banquito.core.productsaccounts.service.InterestRateService;

@SpringBootTest
public class InterestServiceTest {
    
    @Mock
    private InterestRateRepository repository;
    
    @InjectMocks
    private InterestRateService service;
    
    @Test
    public void testObtainById() {
        InterestRate interestRate = new InterestRate();
        interestRate.setId(1);
        interestRate.setName("test");
        interestRate.setInterestRate(new BigDecimal("0.01"));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());
        
        Optional<InterestRate> interestRateOpt = Optional.of(interestRate);
        when(repository.findById(1)).thenReturn(interestRateOpt);
        
        InterestRate result = service.obtainById(1);
        assertEquals(interestRate, result);
        
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CRUDException.class, () -> service.obtainById(1));
    }
}





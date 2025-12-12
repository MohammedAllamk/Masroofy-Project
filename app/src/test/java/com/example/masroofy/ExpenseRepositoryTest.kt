package com.example.masroofy.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ExpenseRepositoryTest {


    @Mock
    lateinit var mockDao: ExpenseDao


    private lateinit var repository: ExpenseRepository


    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        repository = ExpenseRepository(mockDao)
    }


    @Test
    fun calculateBalance_returnsCorrectValue() = runBlocking {

        whenever(mockDao.getTotalAmount(true)).thenReturn(5000.0)

        whenever(mockDao.getTotalAmount(false)).thenReturn(2000.0)


        val balance = repository.calculateBalance()


        assertEquals(3000.0, balance, 0.0)
    }


    @Test
    fun addExpense_validAmount_returnsTrue() = runBlocking {

        val expense = Expense(title = "Test", amount = 100.0, isIncome = false, category = "Food")


        val result = repository.addExpense(expense)


        assertTrue(result)


        Mockito.verify(mockDao).insertExpense(expense)
    }


    @Test
    fun addExpense_negativeAmount_returnsFalse() = runBlocking {

        val expense = Expense(title = "Test", amount = -50.0, isIncome = false, category = "Food")

        val result = repository.addExpense(expense)


        assertFalse(result)
    }
}
package com.example.masroofy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)


    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)


    @Query("SELECT * FROM expenses ORDER BY id DESC")
    suspend fun getAllExpenses(): List<Expense>


    @Query("SELECT * FROM expenses WHERE isIncome = :isIncome")
    suspend fun getExpensesByType(isIncome: Boolean): List<Expense>


    @Query("SELECT * FROM expenses ORDER BY id DESC LIMIT :limit")
    suspend fun getRecentExpenses(limit: Int): List<Expense>


    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = :isIncome")
    suspend fun getTotalAmount(isIncome: Boolean): Double?

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE isIncome = 0 GROUP BY category")
    suspend fun getExpensesByCategory(): List<CategorySummary>
}
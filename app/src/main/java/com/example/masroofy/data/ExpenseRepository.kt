package com.example.masroofy.data


class ExpenseRepository(private val dao: ExpenseDao) {


    suspend fun calculateBalance(): Double {
        val income = dao.getTotalAmount(true) ?: 0.0
        val expense = dao.getTotalAmount(false) ?: 0.0
        return income - expense
    }


    suspend fun addExpense(expense: Expense): Boolean {
        if (expense.amount <= 0) return false
        dao.insertExpense(expense)
        return true
    }
}
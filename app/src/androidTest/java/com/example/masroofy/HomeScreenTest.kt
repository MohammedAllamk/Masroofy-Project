package com.example.masroofy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.masroofy.data.CategorySummary
import com.example.masroofy.data.Expense
import com.example.masroofy.data.ExpenseDao
import com.example.masroofy.ui.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysUI_and_opensDialog() {


        val fakeDao = object : ExpenseDao {


            override suspend fun insertExpense(expense: Expense) {}
            override suspend fun updateExpense(expense: Expense) {}
            override suspend fun deleteExpense(expense: Expense) {}

            override suspend fun getAllExpenses(): List<Expense> = emptyList()

            override suspend fun getExpensesByType(isIncome: Boolean): List<Expense> = emptyList()

            override suspend fun getRecentExpenses(limit: Int): List<Expense> = emptyList()


            override suspend fun getTotalAmount(isIncome: Boolean): Double? = 0.0


            override suspend fun getExpensesByCategory(): List<CategorySummary> = emptyList()
        }
       // Thread.sleep(2000)

        composeTestRule.setContent {
            HomeScreen(dao = fakeDao)
        }


        composeTestRule.onNodeWithText("Masroofy")
            .assertIsDisplayed()


        composeTestRule.onNodeWithContentDescription("Add")
            .assertIsDisplayed()
            .performClick()
      //  Thread.sleep(3000)
        composeTestRule.onNodeWithText("Add New Transaction")
            .assertIsDisplayed()
    }
}
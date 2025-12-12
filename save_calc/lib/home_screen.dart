import 'package:flutter/material.dart';
import 'logic.dart';
import 'budget_card.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final TextEditingController _salaryController =
      TextEditingController(); // [cite: 340]

  List<Map<String, dynamic>> _budgetPlan = [];

  void _generatePlan() {
    FocusScope.of(context).unfocus();

    double? salary = double.tryParse(_salaryController.text); // [cite: 684]

    if (salary != null && salary > 0) {
      setState(() {
        _budgetPlan = SalaryLogic.allocateSalary(salary);
      });
    }
  }

  void _clearData() {
    setState(() {
      _salaryController.clear();
      _budgetPlan.clear();
    }); // [cite: 366]
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Salary Divider"), // [cite: 429]
        actions: [
          IconButton(onPressed: _clearData, icon: const Icon(Icons.refresh)),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _salaryController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: "Enter Total Salary",
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.attach_money),
              ),
            ),

            const SizedBox(height: 20),

            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: _generatePlan,
                child: const Text("Calculate"),
              ),
            ),

            const SizedBox(height: 20),

            Expanded(
              child: _budgetPlan.isEmpty
                  ? const Center(
                      child: Text(
                        "Enter salary to see plan",
                        style: TextStyle(fontSize: 18, color: Colors.grey),
                      ),
                    )
                  : ListView.builder(
                      itemCount: _budgetPlan.length,
                      itemBuilder: (context, index) {
                        return BudgetCard(
                          title: _budgetPlan[index]['title'],
                          percentage: _budgetPlan[index]['percentage'],
                          amount: _budgetPlan[index]['amount'],
                          description: _budgetPlan[index]['description'],
                          color: _budgetPlan[index]['color'],
                          icon: _budgetPlan[index]['icon'],
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }
}

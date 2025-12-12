import 'package:flutter/material.dart';

class SalaryLogic {
  static List<Map<String, dynamic>> allocateSalary(double salary) {
    double needs = salary * 0.50;
    double wants = salary * 0.30;
    double savings = salary * 0.20;

    return [
      {
        'title': "Needs (Essential)",
        'percentage': "50%",
        'amount': needs,
        'description': "Rent, Groceries, Transport, Bills",
        'color': Colors.red,
        'icon': Icons.home,
      },
      {
        'title': "Wants (Lifestyle)",
        'percentage': "30%",
        'amount': wants,
        'description': "Shopping, Outings, Subscriptions",
        'color': Colors.orange,
        'icon': Icons.shopping_bag,
      },
      {
        'title': "Savings & Debt",
        'percentage': "20%",
        'amount': savings,
        'description': "Investment, Emergency Fund, Gold",
        'color': Colors.green,
        'icon': Icons.savings,
      },
    ];
  }
}

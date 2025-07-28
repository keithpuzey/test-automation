@Mobile
@SearchDoctors
Feature: TC43 - Find a Doctor

  Scenario Outline: Search doctor name from CSV

    When I search for "<doctorName>"
    Then I wait between searches
    Examples:
      | doctorName | 
      | Bill     |
      | Tom     | 


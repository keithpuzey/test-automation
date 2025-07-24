@Mobile
@SearchDoctors
Feature: TC43 - Find a Doctor

  Scenario Outline: Search doctor name from CSV
    Given I navigate to the TopDoctors site
    When I search for "<doctorName>"
    Then I wait between searches
    Examples:
      | doctorName | recDescription 	| searchKey               | searchResult                  |
      | Bill     | First Data Set	| perfecto mobile quantum       | Quantum |
      | Tom     | Second Data Set 	|perfecto mobile quantum| perfecto |


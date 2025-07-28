@Mobile
@SearchDoctors
Feature: TC43 - Find a Doctor

  Scenario Outline: Search doctor name 
   Given I navigate to the TopDoctors site
    When I search for "<doctorName>"
    Then I wait between searches
    Examples:
      | doctorName | 
      | Ms Anna Bridgens |
      | Mr Chris Nicolay |
      | Dr Julian Emmanuel |
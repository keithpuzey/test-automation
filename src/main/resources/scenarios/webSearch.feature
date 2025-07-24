@Mobile
Feature: Bing Search

  @SearchDoctors
  Feature: TC43 - Find a Doctor

  Scenario Outline: Search doctor name from CSV
    Given I navigate to the TopDoctors site
    When I search for "<doctorName>"
    Then I wait between searches

    Examples: {"datafile" : "doctorNames.csv"}
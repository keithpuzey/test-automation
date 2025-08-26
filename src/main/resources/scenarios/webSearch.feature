@Mobile
@UpdateCompany
Feature: TC56 - Search and Validate

  Scenario Outline: TC56 - Search and Validate
   Given I navigate to the Salesforce Site and login as salesexec
    When I search for "<companyName>"
    Then I wait between searches
    Examples:
      | companyName | 
      | Ms Anna Bridgens |
      | Mr Chris Nicolay |
      | Dr Julian Emmanuel |
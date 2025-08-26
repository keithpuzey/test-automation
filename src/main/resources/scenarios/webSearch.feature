@Mobile
@UpdateCompany
Feature: TC56 - Search and Validate

  Scenario Outline: TC56 - Search and Validate
   Given I navigate to the Salesforce Site and login as salesexec
    When I search for "<companyName>"
    Then I validate screen contains "<companyName>"
    Examples:
      | companyName | 
      | Pyramid Construction Inc. |
      | Express Logistics and Transport |
      | Grand Hotels & Resorts Ltd |
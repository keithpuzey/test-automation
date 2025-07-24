@Mobile
Feature: Bing Search

@MobileSearch @retry @TC-1
Scenario: TC43 - Find a Doctor
  Given I am on Bing site
  When I search for "Independent Health Doctor"
  Then it should have "Doctor" in search results
  Then I am on Bing Search Page


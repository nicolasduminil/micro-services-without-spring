Feature: Testing the Customers REST API
  Users should be able to submit GET and POST requests to the Customers API in order to CRUD customers

  Background:
    Given URI is initialized

  Scenario: Create a customer
    When user wants to create customer
    Then customer is successfully created

  Scenario: Get customers
    When user wants to get customers list
    Then customers list is returned

  Scenario: Get a customer
    When user wants to get customer associated with id
    Then customer is returned

  Scenario: Update a customer
    When user wants to update customer with id
    Then customer is updated

  Scenario: Remove a customer
    When user wants to remove customer with id
    Then customer is removed

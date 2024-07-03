Feature: Api methods to petStore
  Background: Add a pet to petStore
    Given user has access to api endpoint of "https://petstore.swagger.io/v2"
    When user hits the api with "post" method
    Then gets response code 200

  Scenario: Update pet photoUrl using Put method
    Given user has access to api endpoint of "https://petstore.swagger.io/v2"
    When update the photoUrl with "dogUpdatedPhoto"
    And user hits the api with "put" method
    Then gets response code 200

  Scenario Outline: get pet details using Get method
     Given user has access to api endpoint of "https://petstore.swagger.io/v2"
     When user enters <pet> and <status> hits api to find the count
     Then gets response code 200
    Examples:
      |pet |status  |
      |dog |available|
      |cat |available|
      |dog |sold|
      |cat |sold|

  Scenario: Delete a pet in PetStore
    Given user has access to api endpoint of "https://petstore.swagger.io/v2"
    When user hits the api with "delete" method
    Then gets response code 200
    And user hits the api with "get" method

  Scenario: Upload a image file in PetStore
    Given user has access to api endpoint of "https://petstore.swagger.io" to upload image
    When user upload the file using postMethod
    Then print the file upload message

  Scenario: Post List of users
    Given user has access to api endpoint of "https://petstore.swagger.io/v2"
    When user hits api with list of users as body
      | username | firstName | lastName | email               | password  | phone      | userStatus |
      | test1111  | test1    | Last1    | test11@example.com  | 123434 | 123456787 | 0          |
      | test2222 | test2   | Last2    | test2@example.com  | 123456 | 123454332 | 0          |
    Then validate list of users by getting it



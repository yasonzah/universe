Feature: Managing posts in a social network

  Scenario: Creating a new post
    Given a user has information about the post:
      | id | userId | title         | description                 | publicationDateTime       |
      | 1  | 1      | Title 1       | Description of the first post | 2023-05-18T10:00:00+00:00 |
    When the user creates a new post
    Then a post with id 1 should be created

  Scenario: Updating an existing post
    Given a user has information about the post:
      | id | userId | title         | description                 | publicationDateTime       |
      | 2  | 1      | Title 2       | Description of the second post | 2023-05-19T12:00:00+00:00 |
    And the user creates a new post
    When the user updates the post with id 2:
      | title         | description                 | publicationDateTime       |
      | New Title 2   | New description of the second post | 2023-05-19T15:30:00+00:00 |
    Then the post with id 2 should be updated

  Scenario: Deleting a post
    Given a post exists with id 3
    When the user deletes the post with id 3
    Then the post with id 3 should be deleted

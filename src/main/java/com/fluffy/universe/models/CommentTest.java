package com.fluffy.universe.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentTest {

    @Test
    public void testSetPublicationDateTime() {
        // Arrange
        Comment comment = new Comment();
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 16, 10, 30);
        String expectedDateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Act
        comment.setPublicationDateTime(dateTime);

        // Assert
        Assertions.assertEquals(expectedDateTime, comment.getPublicationDateTime());
    }
}

package com.fluffy.universe.services;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Comment;
import com.fluffy.universe.utils.DataSource;
import io.javalin.http.HttpCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommentServiceTest {

    private static final int POST_ID = 1;
    private static final int USER_ID = 2;
    private static final String DESCRIPTION = "Some comment description";
    private static final String PARENT_COMMENT_ID = "2";
    private static final String PARENT_COMMENT_FIRST_NAME = "Parent comment first name";
    private static final String PARENT_COMMENT_LAST_NAME = "Parent comment last name";
    private static final String USER_FIRST_NAME = "User first name";
    private static final String USER_LAST_NAME = "User last name";

    @BeforeEach
    void setup() {
        // Use an in-memory database for testing
        DataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        DataSource.setUserName("sa");
        DataSource.setPassword("");
    }

    @Test
    public void testSaveComment() {
        Comment comment = new Comment();
        comment.setPostId(POST_ID);
        comment.setUserId(USER_ID);
        comment.setDescription(DESCRIPTION);

        // Save the comment
        CommentService.saveComment(comment);

        // Check that the comment was saved successfully
        List<Map<String, Object>> comments = CommentService.getUserCommentsByPostId(POST_ID);
        Assertions.assertEquals(1, comments.size());
        Map<String, Object> commentMap = comments.get(0);
        Assertions.assertEquals(USER_FIRST_NAME, commentMap.get("User.FirstName"));
        Assertions.assertEquals(USER_LAST_NAME, commentMap.get("User.LastName"));
        Assertions.assertEquals(DESCRIPTION, commentMap.get("Comment.Description"));
    }

    @Test
    public void testUpdateComment() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setParentId(PARENT_COMMENT_ID);
        comment.setPostId(POST_ID);
        comment.setUserId(USER_ID);
        comment.setDescription(DESCRIPTION);

        // Save the comment
        CommentService.saveComment(comment);

        // Check that the comment was updated successfully
        List<Map<String, Object>> comments = CommentService.getUserCommentsByPostId(POST_ID);
        Assertions.assertEquals(1, comments.size());
        Map<String, Object> commentMap = comments.get(0);
        Assertions.assertEquals(PARENT_COMMENT_ID, commentMap.get("Comment.ParentID"));
        Assertions.assertEquals(PARENT_COMMENT_FIRST_NAME, commentMap.get("ParentCommentUser.FirstName"));
        Assertions.assertEquals(PARENT_COMMENT_LAST_NAME, commentMap.get("ParentCommentUser.LastName"));
        Assertions.assertEquals(DESCRIPTION, commentMap.get("Comment.Description"));
    }

    @Test
    public void testGetUserCommentsByPostId() {
        Comment comment1 = new Comment();
        comment1.setPostId(POST_ID);
        comment1.setUserId(USER_ID);
        comment1.setDescription(DESCRIPTION);

        Comment comment2 = new Comment();
        comment2.setPostId(POST_ID);
        comment2.setUserId(USER_ID);
        comment2.setDescription(DESCRIPTION);

        // Save the comments
        CommentService.saveComment(comment1);
        CommentService.saveComment(comment2);

        // Check that the comments were retrieved successfully
        List<Map<String, Object>> comments = CommentService.getUserCommentsByPostId(POST_ID);
        Assertions.assertEquals(2, comments.size());
        List<Map<String, Object>> comments = CommentService.getUserCommentsByPostId(POST_ID);
        Assertions.assertEquals(2, comments.size());
        Map<String, Object> commentMap1 = comments.get(0);
        Map<String, Object> commentMap2 = comments.get(1);
        Assertions.assertEquals(USER_FIRST_NAME, commentMap1.get("User.FirstName"));
        Assertions.assertEquals(USER_LAST_NAME, commentMap1.get("User.LastName"));
        Assertions.assertEquals(DESCRIPTION, commentMap1.get("Comment.Description"));
        Assertions.assertEquals(USER_FIRST_NAME, commentMap2.get("User.FirstName"));
        Assertions.assertEquals(USER_LAST_NAME, commentMap2.get("User.LastName"));
        Assertions.assertEquals(DESCRIPTION, commentMap2.get("Comment.Description"));
    }

    // Additional tests for exception handling and error cases can be added here
}

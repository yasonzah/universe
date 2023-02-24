package com.fluffy.universe.controllers;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.models.User;
import com.fluffy.universe.services.UserService;
import com.fluffy.universe.utils.AlertType;
import com.fluffy.universe.utils.ErrorBag;
import com.fluffy.universe.utils.ServerData;
import com.fluffy.universe.utils.SessionKey;
import com.fluffy.universe.utils.SessionUtils;
import com.fluffy.universe.utils.ValidationUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

import java.util.UUID;

public class UserController extends Controller {
    public UserController(Javalin application) {
        super("", application);
    }

    public void signUpPage(Context context) {
        render(context, "/views/pages/models/user/sign-up.vm");
    }

    public void signInPage(Context context) {
        render(context, "/views/pages/models/user/sign-in.vm");
    }

    public void forgotPasswordPage(Context context) {
        render(context, "/views/pages/models/user/forgot-password.vm");
    }

    public void resetPasswordPage(Context context) {
        String resetPasswordToken = context.pathParam("token");
        User user = UserService.getUserByResetPasswordToken(resetPasswordToken);

        if (user == null) {
            throw new HttpException(HttpCode.FORBIDDEN.getStatus(), "Access forbidden.");
        }

        render(context, "/views/pages/models/user/reset-password.vm");
    }

    public void accountPage(Context context) {
        render(context, "/views/pages/models/user/account.vm");
    }

    public void dashboardPage(Context context) {
        render(context, "/views/pages/models/user/dashboard.vm");
    }

    public void signUp(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("sign-up");
        String errorMessage;

        String email = context.formParam("email");
        String password = context.formParam("password");
        String confirmPassword = context.formParam("confirm-password");
        String firstName = context.formParam("first-name");
        String lastName = context.formParam("last-name");

        if (email == null || email.isEmpty()) {
            errorMessage = "Enter your email address";
        } else if (!ValidationUtils.isValidMail(email)) {
            errorMessage = "Enter a valid email address";
        } else if (UserService.getUserByEmail(email) != null) {
            errorMessage = "Email is already is use";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("email", email, errorMessage);

        if (password == null || password.isEmpty()) {
            errorMessage = "Enter a password";
        } else if (!ValidationUtils.isValidPassword(password)) {
            errorMessage = "Password must be between 8 and 30 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("password", "", errorMessage);

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errorMessage = "Confirm your password";
        } else if (!confirmPassword.equals(password)) {
            errorMessage = "Passwords do not match";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("confirm-password", "", errorMessage);

        if (firstName == null || firstName.isEmpty()) {
            errorMessage = "Enter first name";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("first-name", firstName, errorMessage);

        if (lastName == null || lastName.isEmpty()) {
            errorMessage = "Enter last name";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("last-name", lastName, errorMessage);

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Sign up failed!", "Please ensure that all required fields are filled in correctly and try again.", AlertType.WARNING);
            context.redirect("/sign-up");
            return;
        }

        User user = new User();
        user.setRoleId(Role.USER.getId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(UserService.encodePassword(password));
        UserService.saveUser(user);

        context.sessionAttribute(SessionKey.USER, user);
        serverData.setAlertWindow("Congratulations!", "You have successfully signed up. Welcome to our community!", AlertType.SUCCESS);
        context.redirect("/");
    }

    public void signIn(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("sign-in");
        String errorMessage;

        String email = context.formParam("email");
        String password = context.formParam("password");
        User user = UserService.getUserByEmail(email);

        if (email == null || email.isEmpty()) {
            errorMessage = "Enter your email address";
        } else if (!ValidationUtils.isValidMail(email)) {
            errorMessage = "Enter a valid email address";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("email", email, errorMessage);

        if (password == null || password.isEmpty()) {
            errorMessage = "Enter a password";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("password", "", errorMessage);

        if (user == null || !UserService.isCorrectPassword(password, user.getPassword())) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Sign in failed!", "Invalid email or password. Please check your input and try again.", AlertType.WARNING);
            context.redirect("/sign-in");
            return;
        }

        context.sessionAttribute(SessionKey.USER, user);
        context.redirect("/");
    }

    public void signOut(Context context) {
        context.sessionAttribute(SessionKey.USER, null);
        context.redirect("/");
    }

    public void forgotPassword(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("forgot-password");
        String errorMessage;
        String email = context.formParam("email");

        if (email == null || email.isEmpty()) {
            errorMessage = "Enter your email address";
        } else if (!ValidationUtils.isValidMail(email)) {
            errorMessage = "Enter a valid email address";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("email", email, errorMessage);

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Invalid input!", "The email you entered is not valid.", AlertType.WARNING);
            context.redirect("/forgot-password");
            return;
        }

        User user = UserService.getUserByEmail(email);
        if (user != null) {
            String resetPasswordToken = UUID.randomUUID().toString();
            user.setResetPasswordToken(resetPasswordToken);
            UserService.saveUser(user);
        }

        serverData.setAlertWindow("Success!", "We have sent you an email with instructions on how to reset your password.", AlertType.SUCCESS);
        context.redirect("/sign-in");
    }

    public void resetPassword(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        ErrorBag errorBag = new ErrorBag("sign-up");
        String errorMessage;

        String resetPasswordToken = context.pathParam("token");
        String password = context.formParam("password");
        String confirmPassword = context.formParam("confirm-password");

        User user = UserService.getUserByResetPasswordToken(resetPasswordToken);
        if (user == null) {
            throw new HttpException(403, "Access forbidden.");
        }

        if (password == null || password.isEmpty()) {
            errorMessage = "Enter a new password";
        } else if (!ValidationUtils.isValidPassword(password)) {
            errorMessage = "Password must be between 8 and 30 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("password", "", errorMessage);

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errorMessage = "Confirm your new password";
        } else if (!confirmPassword.equals(password)) {
            errorMessage = "Passwords do not match";
        } else {
            errorMessage = ErrorBag.NO_ERROR_MESSAGE;
        }
        errorBag.add("confirm-password", "", errorMessage);

        if (errorBag.hasErrors()) {
            serverData.addErrorBag(errorBag);
            serverData.setAlertWindow("Invalid input!", "Please make sure your new password meets the requirements and try again.", AlertType.WARNING);
            context.redirect("/reset-password/" + resetPasswordToken);
            return;
        }

        user.setResetPasswordToken(null);
        user.setPassword(UserService.encodePassword(password));
        UserService.saveUser(user);

        serverData.setAlertWindow("Congratulations!", "Your password has been successfully reset. You can now use your new password to log in to your account.", AlertType.SUCCESS);
        context.redirect("/sign-in");
    }

    public void updateAccount(Context context) {

    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get("/sign-in", this::signInPage, Role.GUEST);
        application.get("/sign-up", this::signUpPage, Role.GUEST);
        application.get("/forgot-password", this::forgotPasswordPage, Role.GUEST);
        application.get("/reset-password/{token}", this::resetPasswordPage, Role.GUEST);
        application.get("/account", this::accountPage, Role.USER);
        application.get("/dashboard", this::dashboardPage, Role.USER);
        application.post("/sign-in", this::signIn, Role.GUEST);
        application.post("/sign-up", this::signUp, Role.GUEST);
        application.post("/sign-out", this::signOut, Role.USER);
        application.post("/forgot-password", this::forgotPassword, Role.GUEST);
        application.post("/reset-password/{token}", this::resetPassword, Role.GUEST);
        application.post("/account", this::updateAccount, Role.USER);
    }
}

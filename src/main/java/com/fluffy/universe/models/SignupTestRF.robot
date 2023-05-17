*** Settings ***
Library    SeleniumLibrary

*** Test Cases ***
Signup Test
    Open Signup Page
    Fill Signup Form
    Click Signup Button
    Click Alert OK Button

*** Keywords ***
Open Signup Page
    Open Browser    http://127.0.0.1:7000/sign-up    Chrome
    Set Window Size    1552    832

Fill Signup Form
    Input Text    id=sign-up__first-name    Test
    Input Text    id=sign-up__last-name    Test
    Input Text    id=sign-up__email    testtesttest@test.com
    Input Text    id=sign-up__password    Qwerty123$
    Input Text    id=sign-up__confirm-password    Qwerty123$

Click Signup Button
    Click Button    css=.form__button

Click Alert OK Button
    Click Button    css=.alert__button

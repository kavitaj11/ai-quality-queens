# Keyword-driven tests & syntax

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · ROBOT FRAMEWORK

Robot Framework is a keyword-driven, tabular automation framework — tests read almost like English, which lowers the barrier for manual QA and business stakeholders. A .robot file has Settings (libraries, setup), Variables, Test Cases, and Keywords sections. Built on Python; extend it by writing Python keyword libraries.

## Worked reference

````text
*** Settings ***
Library           SeleniumLibrary
Suite Setup       Open Browser    ${URL}    chrome
Suite Teardown    Close Browser

*** Variables ***
${URL}            https://shop.dev/login
${VALID_USER}     qa@test.dev
${PASSWORD}       s3cret

*** Test Cases ***
Valid Login Succeeds
    Login    ${VALID_USER}    ${PASSWORD}
    Page Should Contain    Dashboard

Invalid Login Is Rejected
    Login    ${VALID_USER}    wrong-pass
    Page Should Contain    Invalid credentials

*** Keywords ***
Login
    [Arguments]    ${user}    ${pass}
    Input Text       id:email       ${user}
    Input Password   id:password    ${pass}
    Click Button     id:submit
# Run:  robot login.robot   ->  report.html + log.html
````

## Exercise

Write a login.robot with a Settings section importing SeleniumLibrary and two test cases (valid + invalid login) that share a custom 'Login' keyword.

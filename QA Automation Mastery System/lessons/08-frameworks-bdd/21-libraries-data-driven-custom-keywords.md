# Libraries, data-driven & custom keywords

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · ROBOT FRAMEWORK

Robot's power is its library ecosystem: SeleniumLibrary/Browser (Playwright-based) for web, RequestsLibrary for APIs, DatabaseLibrary for DB checks. Data-driven testing uses Test Templates to run one keyword over many rows. When built-in keywords aren't enough, write your own in Python and import the file as a Library.

## Worked reference

````text
*** Settings ***
Library           Browser
Library           RequestsLibrary
Library           helpers.py          # your custom Python keywords
Test Template     Login Should Be

*** Test Cases ***      USER              PASS        EXPECTED
Valid                   qa@test.dev       s3cret      Dashboard
Empty Password          qa@test.dev       ${EMPTY}    Required
Locked Out              locked@test.dev   s3cret      Account locked
Bad Password            qa@test.dev       nope        Invalid
Unknown User            ghost@test.dev    s3cret      Invalid

*** Keywords ***
Login Should Be
    [Arguments]    ${user}    ${pass}    ${expected}
    ${email}=      Unique Email          # <- custom Python keyword
    Fill Text      id=email    ${user}
    Fill Secret    id=password    ${pass}
    Click          id=submit
    Get Text       .msg    contains    ${expected}

# helpers.py
#   from robot.api.deco import keyword
#   import uuid
#   @keyword("Unique Email")
#   def unique_email():
#       return f"qa+{uuid.uuid4().hex[:6]}@test.dev"
````

## Exercise

Convert a login test to a data-driven Template running 5 credential rows, and add one custom Python keyword that generates a unique email.

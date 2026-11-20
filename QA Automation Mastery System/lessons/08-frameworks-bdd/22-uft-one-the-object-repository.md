# UFT One & the object repository

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · UFT / UFT DEVELOPER

UFT One (formerly QTP) is the long-standing commercial tool for enterprise GUI/API automation, scripted in VBScript. Its signature is the Object Repository — a stored map of UI objects and their identification properties — plus keyword-view and expert (code) view. Strong for legacy desktop, SAP, mainframe, and Windows apps that open-source tools struggle with.

## Worked reference

````text
' ── UFT One — Expert View (VBScript) ──
' Objects are referenced through the Object Repository:
'   Browser > Page > WebEdit / WebButton ...

Browser("Shop").Page("Login").WebEdit("email").Set "qa@test.dev"
Browser("Shop").Page("Login").WebEdit("password").SetSecure "s3cret"
Browser("Shop").Page("Login").WebButton("Sign in").Click

' Checkpoint: verify the dashboard loaded
If Browser("Shop").Page("Dashboard").WebElement("welcome").Exist(10) Then
    Reporter.ReportEvent micPass, "Login", "Dashboard shown"
Else
    Reporter.ReportEvent micFail, "Login", "Dashboard missing"
End If

' Descriptive Programming — identify WITHOUT the repository:
Browser("Shop").Page("Login").WebButton("html id:=submit").Click
````

## Exercise

In UFT, record a login flow, inspect the captured objects in the Object Repository, and rewrite the recorded script by hand in Expert View with a checkpoint.

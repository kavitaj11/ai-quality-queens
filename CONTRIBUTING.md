# Contributing to AI Quality Queens

A useful contribution can be as small as a missing setup step or as substantial as a lab that teaches someone to diagnose a failure.

## Choose a change

For a correction, link directly to the affected lesson in your pull request. For a new learning track or a change that moves several tutorials, [open an issue](https://github.com/kavitaj11/ai-quality-queens/issues) describing the audience, learning goal, and proposed location so contributors can coordinate.

## Prepare a lesson or example

- Give the reader a specific task, the prerequisites, and the directory from which to run each command.
- Show a worked example with an observable result. For a test, explain which defect would make it fail and what the failure would look like.
- Put examples in the matching language track. When adding or moving a numbered tutorial, update its heading, filename, guide entry, and incoming links together.
- Use synthetic data and examples you have permission to share. Remove credentials and personal or client information from logs, screenshots, and fixtures.

## Verify and submit

Fork the repository and create a branch from the current `main`. Follow your instructions from a fresh checkout, run the affected example where possible, and open each changed relative link from the rendered guide. For an AI evaluation, identify the model or fixture used and the criteria for judging the output so reviewers can interpret differences between runs.

[Open a pull request](https://github.com/kavitaj11/ai-quality-queens/compare) describing who the change helps, what you changed, and what you observed during verification. If a check requires a service you could not access, name the outstanding check. Reviewers should be able to reproduce the example and explain what a learner will take away.

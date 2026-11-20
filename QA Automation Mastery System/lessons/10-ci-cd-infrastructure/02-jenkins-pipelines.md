# Jenkins pipelines

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · PIPELINES

Declarative Jenkinsfile with stages, agents, and post-actions — still the backbone of enterprise QA. Know the stage/parallel/post vocabulary.

## Worked reference

````text
pipeline {
  agent { docker { image 'python:3.12' } }
  stages {
    stage('Smoke') {
      steps { sh 'pip install -r requirements.txt && pytest -m smoke' }
    }
    stage('Regression') {
      when { triggeredBy 'TimerTrigger' }
      steps { sh 'pytest -m regression -n 4' }
    }
  }
  post {
    always { junit 'reports/*.xml' }
  }
}
````

## Exercise

Write a Jenkinsfile that runs smoke on every commit and full regression nightly.

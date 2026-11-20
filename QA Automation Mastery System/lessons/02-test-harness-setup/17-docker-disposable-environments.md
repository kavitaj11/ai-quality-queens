# Docker & disposable environments

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · CI/CD FOR TEST SUITES

Works-on-my-machine dies here: the app, its database, and the test runner all come up from one compose file, run, and are thrown away. Testcontainers brings the same idea inside integration tests.

## Worked reference

````text
# docker-compose.test.yml — whole env, disposable
# services:
#   db:
#     image: postgres:16
#     environment: { POSTGRES_PASSWORD: test }
#   app:
#     build: .
#     depends_on: [db]
#   tests:
#     build: ./tests
#     depends_on: [app]
#     command: pytest -m regression

# One command, from-scratch every run:
#   docker compose -f docker-compose.test.yml \
#       run --rm tests
````

## Exercise

Containerize your suite: compose file with app + db + tests services, one command runs everything from scratch.

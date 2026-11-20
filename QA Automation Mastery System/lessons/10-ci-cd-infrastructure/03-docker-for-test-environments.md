# Docker for test environments

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · PIPELINES

Containerized browsers and app-under-test mean identical environments locally and on CI — the end of 'works on my machine' for test suites.

## Worked reference

````text
# docker-compose.yml (Python suite)
services:
  app:
    build: .
    ports: ["3000:3000"]
  e2e:
    image: mcr.microsoft.com/playwright/python:v1.49.0
    depends_on: [app]
    environment: { BASE_URL: http://app:3000 }
    command: pytest -n 4
    volumes: [".:/work"]
    working_dir: /work
````

## Exercise

Run your suite inside the official Playwright image; then docker-compose the app + tests together.

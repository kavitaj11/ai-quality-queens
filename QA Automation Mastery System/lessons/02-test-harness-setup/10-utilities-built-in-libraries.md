# Utilities & built-in libraries

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

The standard-library toolbox every suite leans on: unique IDs, timestamps, files and paths for artifacts, JSON, and environment variables for config.

## Worked reference

````text
import json, os, uuid
from datetime import datetime, timezone
from pathlib import Path

BASE_URL = os.environ.get("BASE_URL",
                          "https://staging.shop.dev")

def unique_email() -> str:
    return f"qa+{uuid.uuid4().hex[:6]}@test.dev"

def save_snapshot(name: str, data: dict) -> Path:
    out = Path("artifacts"); out.mkdir(exist_ok=True)
    stamp = datetime.now(timezone.utc).strftime("%H%M%S")
    path = out / f"{name}-{stamp}.json"
    path.write_text(json.dumps(data, indent=2))
    return path
````

## Exercise

Write a helpers module — unique email generator, artifact-dir creator, JSON snapshot writer — and reuse it from two tests.

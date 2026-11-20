# Test data & factories

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

Factories with sensible defaults plus per-test overrides beat shared fixture files. dataclasses + Faker, object spreads, Partial<T> builders, and Java records — same idea, four shapes.

## Worked reference

````text
from dataclasses import dataclass, field
from faker import Faker
fake = Faker()

@dataclass
class User:
    email: str = field(default_factory=fake.email)
    name: str = field(default_factory=fake.name)
    role: str = "customer"

admin = User(role="admin")            # override one field
guest = User(email="qa+guest@test.dev")
````

## Exercise

Build a user factory with defaults and overrides in your primary language; use it in three tests that each override exactly one field.

from pathlib import Path
import subprocess

import pytest


@pytest.fixture(scope="session")
def java_classes(tmp_path_factory):
    classes = tmp_path_factory.mktemp("classes")
    source = Path(__file__).resolve().parents[3] / "test-automation-in-java" / "examples" / "java" / "Shipping.java"
    probe = Path(__file__).with_name("Quote.java")
    subprocess.run(["javac", "-d", str(classes), str(source), str(probe)], check=True)
    return classes


@pytest.mark.parametrize(("subtotal", "expected"), [(4999, "499"), (5000, "0")])
def test_shipping(java_classes, subtotal, expected):
    result = subprocess.run(
        ["java", "-cp", str(java_classes), "Quote", str(subtotal)],
        capture_output=True, text=True, check=True,
    )
    assert result.stdout.strip() == expected

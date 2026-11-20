"""Build the GitHub Pages curriculum from the repository's Markdown guides."""
import html
import json
import re
import shutil
import sys
from pathlib import Path

WEB = Path(__file__).resolve().parent
ROOT = WEB.parent
OUTPUT = Path(sys.argv[1]) if len(sys.argv) > 1 else WEB / "_site"


def paragraph(text):
    return "<p>" + html.escape(text.strip()) + "</p>"


def lesson_html(source):
    body = source.split("**Category**", 1)[1].split("\n", 1)[1].strip()
    chunks = re.split(r"^````text\n(.*?)^````\s*$", body, flags=re.M | re.S)
    result = []
    for index, chunk in enumerate(chunks):
        if index % 2:
            result.append("<pre><code>" + html.escape(chunk.rstrip()) + "</code></pre>")
            continue
        for block in re.split(r"\n\s*\n", chunk.strip()):
            if block.startswith("## "):
                result.append("<h2>" + html.escape(block[3:].strip()) + "</h2>")
            elif block:
                result.append(paragraph(block))
    return "".join(result)


def modules():
    result = []
    indexed = set()
    for path in sorted((ROOT / "modules").glob("*.md")):
        source = path.read_text()
        topics = []
        category = None
        for line in source.splitlines():
            if line.startswith("### "):
                category = line[4:]
            match = re.fullmatch(r"- \[(.+?)\]\((\.\./lessons/[^)]+)\)", line)
            if match:
                title, link = match.groups()
                lesson = (path.parent / link).resolve()
                if lesson in indexed or not category:
                    raise ValueError(f"Invalid lesson index at {link}")
                indexed.add(lesson)
                topics.append(dict(id=lesson.stem, title=title, category=category,
                                   body=lesson_html(lesson.read_text())))
        result.append(dict(id=path.stem, title=source.splitlines()[0].split(" · ", 1)[1],
                           weeks=source.split("**Study window** · ")[1].splitlines()[0],
                           description=source.split("## What to study")[1].split("##")[0].strip(),
                           practice=source.split("## Practice assignment")[1].split("##")[0].strip(),
                           topics=topics))
    if indexed != set((ROOT / "lessons").rglob("*.md")):
        raise ValueError("The module index and lesson directory disagree")
    return result


def build():
    cards = []
    for section in re.split(r"^## ", (ROOT / "flashcards.md").read_text(), flags=re.M)[1:]:
        category, content = section.split("\n", 1)
        for q, a in re.findall(r"<summary>(.*?)</summary>\s*(.*?)\s*</details>", content, re.S):
            cards.append(dict(category=category, question=html.unescape(q), answer=a))
    curriculum = modules()
    plan = (ROOT / "planner.md").read_text()
    weeks = re.findall(r"^- \[ \] \*\*Week (\d+)\*\* · \[([^\]]+)\]\(modules/([^/)]+)\.md\)$", plan, re.M)
    ai = re.findall(r"^- \[ \] \*\*AI (\d+)\*\* · \[([^\]]+)\]\((https://github\.com/[^)]+)\) · (.+)$", plan, re.M)
    if [row[2] for row in weeks] != [m["id"] for m in curriculum] or not ai or not cards:
        raise ValueError("Check planner module links, AI articles and flashcards")
    OUTPUT.mkdir(parents=True, exist_ok=True)
    for name in ("index.html", "app.js", "style.css"):
        shutil.copyfile(WEB / name, OUTPUT / name)
    data = dict(modules=curriculum, weeks=weeks, ai=ai, cards=cards)
    (OUTPUT / "curriculum.json").write_text(json.dumps(data, ensure_ascii=False))
    (OUTPUT / ".nojekyll").touch()


if __name__ == "__main__":
    build()

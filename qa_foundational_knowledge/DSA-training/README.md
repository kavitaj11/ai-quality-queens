# DSA training in Java

Start with searching and sorting, study the structures that support them, then apply those ideas to the practice problems. Each lesson links to its Java implementation.

- [Sorting and searching](algorithms/sorting-and-searching/README.md)
- [Problem-solving techniques](algorithms/problem-solving/README.md)
- [Data structures](data-structures/README.md)
- [Practice problems](practice/README.md)

Compile all examples from this directory with JDK 17 or later:

```sh
mkdir -p out
find algorithms data-structures practice -name "*.java" -print0 | xargs -0 javac -d out
```

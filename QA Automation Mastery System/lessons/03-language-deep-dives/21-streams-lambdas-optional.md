# Streams, lambdas & Optional

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVA DEEP DIVE

Lambdas are inline functions (d -> d.isLoaded()); streams process collections declaratively — filter, map, collect — instead of manual loops. Optional models 'maybe a value' and ends NullPointerException-by-forgetting. This trio makes Java test code read like intent, not plumbing.

## Worked reference

````text
// Lambda: an inline function passed as an argument
users.forEach(u -> System.out.println(u.email()));

// Stream pipeline: filter -> map -> sort -> collect
List<String> adminEmails = users.stream()
    .filter(u -> u.active())
    .filter(u -> u.role().equals("admin"))
    .map(User::email)                  // method reference
    .sorted()
    .toList();

// Reduce / count / any-match
long activeCount = users.stream().filter(User::active).count();
boolean anyLocked = users.stream().anyMatch(User::locked);

// Optional: no more NullPointerException
Optional<User> maybe = repo.findById("1");
String email = maybe.map(User::email).orElse("unknown");

repo.findById("1")
    .ifPresentOrElse(
        u -> login(u),
        () -> fail("user missing"));
````

## Exercise

From a list of users, stream to the emails of active admins, sorted, as a List — in one expression. Then use Optional to fetch-or-default from a cache.

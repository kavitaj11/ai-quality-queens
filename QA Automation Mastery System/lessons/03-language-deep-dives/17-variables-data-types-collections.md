# Variables, data types & collections

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVA DEEP DIVE

Java is statically typed: every variable declares its type. Eight primitives (int, long, double, boolean, char, byte, short, float) hold raw values; everything else is an object, with wrapper classes (Integer, Double…) for when you need objects. String is immutable. var (Java 10+) infers local types. Then the Collections framework — List, Set, Map, Queue — carries every bit of test data.

## Worked reference

````text
// ── PRIMITIVES (8) — raw values, not objects ──
int qty = 2;
long id = 100_000L;
double price = 19.99;
boolean ok = true;
char grade = 'A';
byte b = 8; short s = 300; float f = 1.5f;

// ── WRAPPERS & String (objects) ──
Integer boxed = 42;              // object form of int
String sku = "A-100";            // immutable
var email = "a@test.dev";        // var: type inferred (String)

// ── LIST — ordered, allows duplicates ──
List<String> emails = new ArrayList<>();
emails.add("a@test.dev");
emails.get(0);                   // indexed access
emails.contains("a@test.dev");

// ── SET — unique elements, fast membership ──
Set<String> suites = new HashSet<>(List.of("checkout", "login"));
suites.add("checkout");          // no-op, already present

// ── MAP — key -> value lookups ──
Map<String, Order> byId = new HashMap<>();
byId.put(sku, order);
byId.getOrDefault("X", Order.none());

// Tally statuses with merge (common report task)
Map<String, Integer> tally = new HashMap<>();
for (Result r : results) tally.merge(r.status(), 1, Integer::sum);

// ── QUEUE / DEQUE — FIFO work backlog ──
Deque<String> queue = new ArrayDeque<>();
queue.offer("job-1"); queue.poll();

// Immutable snapshots
List<String> fixed = List.of("a", "b");   // unmodifiable
````

## Exercise

Declare each primitive, then build a List of results, a Set of suite names, and a Map<String,Integer> status tally using merge.

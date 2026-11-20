# Interfaces, abstract classes & generics

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVA DEEP DIVE

An interface is a contract (WebDriver is one — ChromeDriver and FirefoxDriver implement it, so your tests don't care which). Abstract classes share partial implementation. Generics (<T>) give type-safe reuse. Together they explain why Selenium code targets interfaces, not concrete drivers — swappable, mockable, clean.

## Worked reference

````text
// Interface = contract; tests depend on this, not Chrome
interface Repository<T> {              // generic contract
    void save(T entity);
    Optional<T> findById(String id);
}

// Concrete implementation
class UserRepository implements Repository<User> {
    private final Map<String, User> store = new HashMap<>();
    public void save(User u) { store.put(u.id(), u); }
    public Optional<User> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}

// Program to the interface — swap impls freely
Repository<User> repo = new UserRepository();   // or a fake in tests
repo.save(new User("1", "a@test.dev"));

// This is why Selenium uses interfaces:
WebDriver driver = new ChromeDriver();  // or Firefox/Remote —
driver.get("https://shop.dev");         // tests never change
````

## Exercise

Define a Repository<T> interface with save/findById, implement it for User, and swap a real impl for a fake in a test.

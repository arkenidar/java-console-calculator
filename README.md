# Java Console Calculator (Maven)

This project is a simple console-based calculator implemented in Java, mirroring the behavior of the JavaScript `calc-formula.js` version.

It is intended to live at:
- `https://github.com/arkenidar/java-console-calculator`

Supported features (kept in sync with JS):
- Operators: `+`, `-`, `*`, `/`, `^`
- Parentheses: `(`, `)`
- Decimal numbers (e.g. `-0.5`, `5/2`)

The core logic lives in `Calculator.calculate(String)` in `src/main/java/com/example/calculator/Calculator.java`.

## Build and Run with Maven

Prerequisites:
- JDK installed
- Maven installed (`mvn -v` works)

From the project root:

```bash
cd /home/arkenidar/temporary/java-console-calculator
mvn -q clean compile
mvn -q -DskipTests exec:java
```

This will run `com.example.calculator.Calculator.main`, which executes a small test suite (same tests as in `calc-formula.js`) and prints whether each one is `true`.

## Keeping JS and Java in Sync

- JS reference repo: `https://github.com/arkenidar/calculator-raw`
	- File: `calc-formula.js` (`calculate(formula)` and tests at the top).
- Java port repo: `https://github.com/arkenidar/java-console-calculator`
	- File: `src/main/java/com/example/calculator/Calculator.java`.

When you add a new test or operator in JS:
- Update the Java `calculate` method in the same way.
- Add the corresponding test line in `Calculator.main`.
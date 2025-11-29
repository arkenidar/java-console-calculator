# GitHub Copilot Instructions for `java-console-calculator`

These guidelines help AI coding agents work effectively in this repo.

## Project Overview
- Simple Java console app that parses and evaluates arithmetic expressions.
- Core logic is in `src/main/java/com/example/calculator/Calculator.java`.
- The `main` method runs a small set of self-check tests by calling `calculate(String formula)` with example expressions.

## Core Logic & Design
- Expression evaluation is implemented manually, without external parser libraries.
- `Calculator.calculate(String)`:
  - Iterates over the input formula character-by-character.
  - Uses a `StringBuilder` (`digits`) to accumulate numeric tokens including decimals.
  - Tracks current numeric value in a `Double value` (nullable to signal “no value yet”).
  - Maintains a simple operator stack `OperFrame[] operStack` and `operTop` index to handle parentheses.
  - Uses `operPush` to push a new `OperFrame` when encountering `(` and resets `value` to start a new subexpression.
  - Calls `parse(...)` whenever it hits an operator, closing parenthesis, or end-of-string.
- `OperFrame` (inner static class):
  - Fields: `String operator` (e.g. `"+"`, `"*"`, `"-"`, `"/"`, `"^"`) and `Double operand` (left-hand value waiting for an operator application).
  - Operator precedence is effectively handled by when and how `parse` is called, not by an explicit precedence table.
- `parse(...)` behavior (central pattern):
  - Flushes any pending digits into a `Double` when `digits.length() != 0`.
  - Treats unary minus at the start of an expression or subexpression as `0 - value` via `if (ch == '-' && value == null) value = 0.0;`.
  - Applies the stored operator from the current `OperFrame` to `operand` and `value` (supports `+`, `-`, `*`, `/`, `^`).
  - Stores the just-seen operator and operand back into the current `OperFrame` for the next step.
- Parentheses handling:
  - On `(`: pushes a new `OperFrame` and resets `value` to `null`.
  - On `)`: calls `parse` for the closing point, then pops the stack and calls `parse` again to fold the subexpression result into the previous frame.

## Conventions & Patterns
- **Logging / debug**: the current code prints debug information to stdout (e.g. `"!formula: " + formula`, `"value!"`, `"calc!"`).
  - Preserve or adjust these logs carefully if changing parsing logic; they may be used for manual debugging.
- **Numeric handling**:
  - Numeric characters detected via `numeric(char)` helper; decimal point handled by explicit `ch == '.'` check.
  - Parsing uses `Double.parseDouble(digits.toString())` with no localization or error recovery.
- **Operators**:
  - Valid operators are `+`, `-`, `*`, `/`, `^`; centralized in `isOperator(char)`.
  - Any new operator should be added consistently in `isOperator`, `parse`, and tests in `main`.

## Testing & Workflows
- There is no formal test framework configured; quick checks are via `main`:
  - `Calculator.main` prints `TEST #n true/false` for several example expressions.
  - When changing `calculate`, update or extend these example tests to preserve coverage of features like parentheses, unary minus, and exponentiation.
- Typical workflow for changes:
  - Build/run using your preferred Java toolchain (e.g. `javac`/`java`) against `Calculator.java`.
  - Confirm all `TEST #n` lines evaluate to `true` before and after refactors.

## How AI Agents Should Modify Code
- Keep the public API stable unless explicitly requested:
  - Preserve `public static double calculate(String formula)` signature and behavior contract (string in, numeric result out; basic arithmetic, parentheses, unary minus, exponentiation).
- When refactoring:
  - Maintain the core evaluation semantics (e.g. how unary minus, parentheses, and `^` are handled) and verify via `main` tests or new equivalent checks.
  - Prefer small, incremental changes—avoid rewriting the parser with entirely different algorithms unless asked.
- When extending functionality:
  - Mirror existing patterns: update `isOperator`, the `parse` operator application block, and add new examples to `main`.
  - Be explicit about new behavior (e.g. operator precedence, associativity) in code comments and, if needed, in a short note in this file.

## Files to Look At First
- `src/main/java/com/example/calculator/Calculator.java` — core and only important class right now; contains parsing algorithm, operator stack, and test harness.

If you adjust architecture (e.g. extract a separate parser class or add a test suite), update this file so future AI agents understand the new structure and workflows.
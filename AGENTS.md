# Agent Instructions

## Commit and Push

- After completing any code or documentation changes, and after verification (tests, lint, etc.) passes:
  1. Inspect `git status` and `git diff` to ensure no secrets or unintended files are included.
  2. Stage all intended changes with `git add -A`.
  3. Create a commit with a concise conventional-commit message matching the repo style.
  4. Push to the current remote tracking branch with `git push`.
- Do not ask for confirmation before committing/pushing unless the user has explicitly opted out in this session.
- If there are no changes to commit, do not create an empty commit.

## Verification

- Run the project's test command (`mvn test` for this project) after non-trivial changes and before committing.
- If lint or typecheck commands are defined in the build configuration, run those as well.

## Style

- Follow existing code style and naming conventions.
- Keep responses concise and direct.
- Use Chinese when the user communicates in Chinese.

## Cross-Repository Bug Fix Strategy

When any bug is found in `hyperscan-java-test`, evaluate whether the same bug also exists in the sibling projects (`hyperscan-java-panama` and `hyperscan-java-native`):

1. Identify the root cause and whether it is specific to the test adapter / test harness code in `hyperscan-java-test` or belongs to a shared concept (e.g., native memory management, JNI/FFM binding patterns, compile/scan argument handling, loader behavior).
2. Check the corresponding code paths in `../hyperscan-java-panama` and `../hyperscan-java-native` for the same or similar anti-pattern.
3. If the same bug exists in a sibling project, fix it there as well; do not leave the same defect in the upstream wrapper/native code.
4. If the bug is purely local to the test adapter or test fixture, fix it in `hyperscan-java-test` and document why it does not affect the sibling projects.

## Cross-Repository Optimization Strategy

For any performance optimization or architectural change in this repository, first evaluate whether the same improvement can be applied to the sibling `../hyperscan-java-panama` project:

1. Determine if the change belongs more naturally in the `hyperscan-java-panama` layer (e.g., generated bindings, JNI facade, callback helpers, native build flags).
2. Determine if the change is reusable in `hyperscan-java-panama` (e.g., MethodHandle caching, direct upcall stubs, functional-interface conversion patterns).
3. If either answer is yes, implement the change in `../hyperscan-java-panama` first (or in parallel), then adapt and apply it here.

Do not optimize locally in `hyperscan-java-test` only when the same optimization should live in, or can be shared with, the shared library.

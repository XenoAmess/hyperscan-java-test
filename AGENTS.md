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

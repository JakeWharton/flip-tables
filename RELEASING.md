# Releasing

1. Update the `VERSION_NAME` in `gradle.properties` to the release version.

2. Update the `CHANGELOG.md`:
   1. Change the `Unreleased` header to the release version.
   2. Add a link URL to ensure the header link works.
   3. Add a new `Unreleased` section to the top.

3. Update the `README.md` so the "Download" section reflects the new release version.

4. Commit

   ```
   $ git commit -am "Prepare version X.Y.X"
   ```

5. Publish

    ```
    $ ./gradlew clean publish
    ```

6. Tag

   ```
   $ git tag -am "Version X.Y.Z" X.Y.Z
   ```

7. Update the `VERSION_NAME` in `gradle.properties` to the next "SNAPSHOT" version.

8. Commit

   ```
   $ git commit -am "Prepare next development version"
   ```

9. Push!

   ```
   $ git push && git push --tags
   ```

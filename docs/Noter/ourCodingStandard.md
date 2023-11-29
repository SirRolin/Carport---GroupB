# Arbejde med hver user story

## Så censor (og underviser) bliver glade ;-)

Når man går i gang med en user story er der altid nogle trin som man skal overveje:

0. Udarbejd gruppens kodestandard
   - Kamelcase
   - Navngivning
   - Overordnede arkitektur (packages i koden). Hvis man tilføjer packages skal det lige vendes med resten af gruppen.
   - Hvornår skal der fx laves unit-test af metoder?
   - Definition of Done (DoD). Hvornår er en kodestump færdig?
1. Først skal man overveje om ens US skal nedbrydes i flere tasks
   - Udvid domæne model + ERD
   - Overvej om man skal lave en ekstra "teknisk" user story eller `task` først. F.eks: `opsætning af database`, configuration af et eller andet.
2. Er der andre diagrammer som skal udvides eller evt. nye diagrammer som skal tegnes for at komme i gang?
3. Er der behov for en mockup af UI/UX i Figma eller et navigationsdiagram (tilstandsdiagram).
4. Lav diverse tilpasninger af databasens tabeller
5. Lav en ny feature branch i GitHub - gerne fra en Issue
6. Gå i gang med at kode ......
7. Sørg for at validere input og give gode fejlmeddelelser (specielt på de første US). På den måde viser I at I kan kode med en vis kvalitet for øje. At det bliver brugervenlig osv.
8. Lav fejlhåndtering (exeception handling) med gode beskeder.
9. Når koden virker - så laven UAT (user acceptance test) på baggrund af det I har formuleret i jeres US.
10. Push koden up til Github. Check koden ind via en pull request. Dvs, at der er mindst et gruppemedlem som skal godkende (og review'e koden).
11. Husk at skrive detaljer og beslutninger I gør undervejs ned i jeres log-bog. F.eks. hvis I har lagt noget data i en session variabel osv.
12. Opdater jeres KanBan board - også undervejs. Tag gerne lidt screenshots en gang imellem.

I må gerne lave jeres egen variant af denne kogebogsopskrift og indsætte i rapporten for at dokumentere **JERES** arbejdsgang.

# Forslag til kodestandar (fra ChatGPT)

A typical coding standard for Java programmers focuses on various aspects like naming conventions, code layout, best practices for programming, and documentation. Here's an outline of common guidelines:

1. **Naming Conventions**:
   - Classes: Use uppercase letters for the first letter of each word, e.g., `StudentRecord`.
   - Methods: Start with a lowercase letter and use camelCase for subsequent words, e.g., `calculateTotal`.
   - Variables: Similar to methods, e.g., `employeeCount`.
   - Constants: All uppercase with underscores to separate words, e.g., `MAX_SIZE`.
   - Short and meaningfull names! (looking at you Christian!)

2. **Code Layout**:
   - Indentation: Use spaces (commonly 4) for indentation to maintain code readability.
   - Braces: Use the "Egyptian style" braces, where the opening brace is at the end of the line, and the closing brace is aligned with the start of the line, e.g.,

     ```java
     if (x < 10) {
         // code block
     }
     ```

   - Line Length: Preferably keep lines to a maximum of 80-100 characters for better readability.

3. **Commenting and Documentation**:
   - Use comments to explain the purpose of complex code segments.
   - For methods, use Javadoc standard comments to describe the purpose, parameters, and return values.

4. **Best Practices**:
   - Avoid using public fields; use private fields with public getters and setters.
   - Minimize the use of global variables.
   - Follow the principle of 'DRY' (Don't Repeat Yourself).
   - Exception Handling: Use meaningful catch blocks and avoid generic exceptions where possible.
   - Code Refactoring: Regularly refactor code to improve readability and maintainability.

5. **Testing and Debugging**:
   - Write unit tests for methods, especially public APIs.
   - Use logging instead of `System.out.println` for debugging purposes.

6. **Performance Considerations**:
   - Be mindful of memory management, especially in loops.
   - Use StringBuilder/StringBuffer for string concatenation in loops.

7. **Security Best Practices**:
   - Avoid exposing sensitive data in exceptions or logs.
   - Validate inputs to prevent SQL injection and other forms of attacks.

8. **Version Control**:
   - Use meaningful commit messages.
   - Regularly update and merge changes from the main branch to avoid conflicts.

Each organization or project may have its specific additions or variations to these guidelines, but this serves as a general foundation for Java coding standards.

[Link til Fogs hjemmeside](https://www.johannesfog.dk/byg-selv-produkter/carporte/quick-byg-carporte)

![Carport billede](https://cms.johannesfog.dk/cdn-cgi/image/format=auto,width=1920/media/zcznkc5a/carport.jpg?ud=EISlXcKQ2gg)
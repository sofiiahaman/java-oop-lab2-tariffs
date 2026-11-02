# Laboratory Work: XML Processing - "Mobile Operator Tariffs"

This project is a Java application designed to process XML documents containing information about mobile operator tariffs. The application implements the complete XML processing lifecycle: XSD schema validation, parsing using three different parsers (SAX, DOM, and StAX), domain data sorting via custom Comparators, and XSLT transformation to restructure XML elements into grouped lists.

## 🛠 Technologies and Stack

* **Java**: 11+
* **Build Tool**: Apache Maven
* **XML Parsers**:
    * DOM (`javax.xml.parsers.DocumentBuilder`)
    * SAX (`javax.xml.parsers.SAXParser`)
    * StAX (`javax.xml.stream.XMLInputFactory`)
* **Validation & Transformation**: Java XML Validation API (`javax.xml.validation`), TrAX XSLT Transformer (`javax.xml.transform`)
* **Testing**: JUnit 5
* **Logging**: Log4j2

## 📊 XML/XSD Data Structure

Each tariff (`Tariff`) is defined with the following fields and constraints:

* **ID** (`id`) — Unique identifier for the tariff (`xs:ID` type, required attribute).
* **Name** (`name`) — The name of the tariff plan.
* **Operator Name** (`operatorName`) — The mobile operator offering the tariff.
* **Payroll** (`payroll`) — Monthly subscription fee ($\ge 0$ UAH).
* **Call Prices** (`callPrices`) — Prices for calls ($\ge 0$ UAH/min):
    * On-network calls (`insideNetwork`)
    * Off-network calls (`outsideNetwork`)
    * Landline calls (`landline`)
* **SMS Price** (`smsPrice`) — Price per SMS ($\ge 0$ UAH).
* **Parameters** (`parameters`):
    * Favorite numbers count (`favouriteNumbersCount`, $\ge 0$)
    * Tarification plan (`tarification`: `TWELVE_SECONDS` or `EVERY_MINUTE`)
    * Connection fee (`connectionFee`, $\ge 0$ UAH).

## 📁 Project Structure

```
tariffs-lab/
├── src/
│   ├── main/
│   │   ├── java/ua/org/example/
│   │   │   ├── model/          # POJO classes (Tariff, CallPrices, Parameters, Tarification) and Builder
│   │   │   ├── parser/         # DOM, SAX, StAX parser implementations and TariffCollector
│   │   │   ├── sort/           # Comparator-based sorting (TariffOrder)
│   │   │   ├── transform/      # XSLT transformation logic (XslTransformer)
│   │   │   ├── validation/     # XSD validation classes (XsdValidator, ErrorCollector)
│   │   │   └── App.java        # Main application entry point
│   │   └── resources/
│   │       ├── tariffs.xml          # Primary XML data file
│   │       ├── tariffs.xsd          # XSD schema definition
│   │       ├── tariffs-grouped.xsl  # XSLT grouping stylesheet
│   │       └── log4j2.xml           # Log4j2 logging configuration
│   └── test/
│       ├── java/org/example/tariffs/    # Unit test suite covering all modules
│       └── resources/              # Test XML/XSD/XSL resources (valid and malformed)
├── pom.xml
└── README.md
```

## 🚀 Build and Run Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/sofiiahaman/java-oop-lab2-tariffs
cd tariffs-lab
```

### 2. Build the Project and Run Tests

To compile the project and execute all unit tests, run:

```bash
mvn clean test
```

### 3. Execute the Application

You can run the application directly using Maven:

```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```

Or package and run the executable JAR file:

```bash
mvn clean package
java -jar target/tariffs-lab-1.0-SNAPSHOT.jar
```

## 🔄 XSLT Transformation Example

The original flat list of tariffs in `tariffs.xml` is transformed into sub-lists grouped by specific characteristics (e.g., Operator Name) using `tariffs-grouped.xsl`.

**Original Structure (Conceptual):**

```xml
<Tariffs>
    <Tariff id="t1"><OperatorName>Kyivstar</OperatorName>...</Tariff>
    <Tariff id="t2"><OperatorName>Vodafone</OperatorName>...</Tariff>
    <Tariff id="t3"><OperatorName>Kyivstar</OperatorName>...</Tariff>
</Tariffs>
```

**Transformed Structure:**

```xml
<GroupedTariffs>
    <Operator name="Kyivstar">
        <Tariff id="t1">...</Tariff>
        <Tariff id="t3">...</Tariff>
    </Operator>
    <Operator name="Vodafone">
        <Tariff id="t2">...</Tariff>
    </Operator>
</GroupedTariffs>
```

## 🧪 Unit Testing

The project incorporates comprehensive unit test coverage using JUnit 5:

* `TariffParsersTest` — Verifies correct parsing across DOM, SAX, and StAX implementations.
* `XsdValidatorTest` — Tests XML validation against both valid and invalid schema instances.
* `TariffOrderTest` — Validates sorting behavior using custom `Comparator` rules.
* `XslTransformerTest` — Ensures accurate XSLT transformation and structural grouping.
## Data-Encryption-Standard-DES-based-Encryptor-Application

### 1. Introduction
In this application, I implement Data Encryption Standard (DES) algorithm project by following the description in FIPS PUB 46-3: Data Encryption Standard, which was published by NIST on October 25, 1999. It consists an encryptor and a decryptor, in encryptor, user could type the key and plaintext to generate cipher. While in decryptor, user could type the kew and cipher to generate the plaintext.

* ***Language***: Java 8
* ***Integrated Development Environment (IDE)***: IntelliJ IDEA
* ***JDK Version***: 1.8 Oracle OpenJDK version 1.8.0_333

### 2. Architecture
The figure below shows the overall structure of our project, demonstrated by class diagram. There are three layers in my project: "View layer", "Tool layer" and "Algorithm layer". In terms of "Tool layers", two packages "Formatter" and "Translator" in this layer. To implement the DES algorithm, it uses the table data and primitive functions stored in the "Primitive Data" package, and takes advantage of some functions related to formatting and transferring bytes. As for the "View layer", "Demo" class call the initUI() functions in "GUI Decryption" and "GUI Encryption" class, in order to call two windows: decryptor and encryptor.
<div align=center><img src="https://github.com/WangHewei16/Data-Encryption-Standard-DES-based-Encryptor-Application/blob/main/Figures/achitectures.png?raw=true" width="540"/></div>

### 3. Tree
```
.
|-- DES-Encryptor.iml
|-- jars
|   `-- commons-codec-1.15.jar
`-- src
    |-- algorithm
    |   `-- DES.java
    |-- formatter
    |   `-- Formatter.java
    |-- primitives
    |   `-- Primitive.java
    |-- translator
    |   `-- Translator.java
    `-- view
        |-- Demo.java
        |-- GUI_Decryptor.java
        `-- GUI_Encryptor.java
```

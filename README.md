# WaxLibrary3

**WaxLibrary3** is a comprehensive Java library featuring rich custom Swing components and various utilities, actively developed over more than 16 years, suitable for diverse application development contexts.

---

## 📚 Main Modules

### 🌟 Core

- Advanced property change management (undo/redo functionality).
- Dynamic property merging using annotations (`TemplatePropertyMerger`).
- Automatic application updating mechanism (`Updater`).
- Runtime exception handling (`RuntimeExceptionHandler`).
- Advanced collection filtering and sorting utilities (`TemplateCollectionFilter`, `TemplateGroupAndSorter`).
- PDF text extraction utilities, NTP client functionalities, XPath navigation utilities, and runtime profiling with 'TikTak'.
- Additional core utilities supporting robust Java application development.

### 🖥 WaxLibSwingComponents

- Interactive property editors (`PropertiesEditorPanel`).
- Enhanced file and color selectors (`JFileChooserButton`, `JColorChooserButton`).
- Custom graphical components such as advanced sliders and curve editors (`CurvePanel`).
- Comprehensive graphical file browser (`FileBrowserPanel`).
- User-friendly splash screens and loading management utilities (`ProgressGlassPane`).
- Many other specialized Swing components designed for improved user interaction and experience.

### 🎨 W2D (Wax 2D Engine)

W2D is a sophisticated 2D graphics engine built upon Java Swing, enabling rapid development of interactive and precise graphical applications.

#### In-depth Features:
- Comprehensive affine transformation support (translate, zoom, rotate).
- Interactive selection and context management (`CurrentSelectionContext`).
- Advanced curve systems, including Bézier, NURBS, and spirograph curves, enabling smooth and complex graphical renderings.
- Rich demonstration examples showcasing engine capabilities (`VUE2D_TestBed`, `VUE2D_Spiro`).
- Modular and extendable architecture designed for high flexibility and customization.
- Specific utilities for precise graphical alignments, selection highlights, and real-time rendering optimizations.

### 🛠 OBJ2GUI

- Automatic graphical user interface generation based on annotated Java objects.
- For managing collections and more complex scenarios, the enhanced [OBJ2GUI2](https://github.com/warnotte/obj2gui2) version is recommended.

---

## 🚀 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.warnotte</groupId>
    <artifactId>waxlib3</artifactId>
    <version>0.3.7</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.warnotte:waxlib3:0.3.7'
```

---

## 📖 Documentation and Examples

Explore numerous provided examples in the folder [`src/main/java/io/github/warnotte/waxlib3/`](src/main/java/io/github/warnotte/waxlib3/) to understand practical implementations and integration of each module.

---

## 🔗 Useful Links

- [Maven Central Repository](https://mvnrepository.com/artifact/io.github.warnotte/waxlib3)
- [OBJ2GUI2 (enhanced version)](https://github.com/warnotte/obj2gui2)

---

## ❓ FAQ

**Is WaxLibrary3 compatible with Java 8 and newer?**\
Yes, fully compatible with Java 8 and all newer versions.

**How can I contribute?**\
Contributions are welcome via forks and pull requests on GitHub.

---

© [warnotte](https://github.com/warnotte) — 2008–2025


# SIMPLIFICATION ARCHITECTURALE - Repère Standard

## ✅ NOUVELLE APPROCHE SIMPLIFIÉE

**Principe** : Utiliser un repère de référence STANDARD et laisser le moteur W2D gérer les transformations.

### Repère de Référence Standard
```
        Y+ (haut)
         |
         |
         +--------> X+ (droite)
       (0,0)
```

- **X+ → DROITE**
- **Y+ → HAUT**
- **Rotations : sens anti-horaire**

## 🔴 Problème Architectural Précédent (RÉSOLU)

**Le problème fondamental**: Je compliquais tout en essayant de gérer les inversions d'axes dans RenderContext.

### Phase 1: Régression initiale (corrigée)
J'avais introduit `ctx.drawShape()` qui utilisait `Graphics2D` directement au lieu de passer par les méthodes du moteur W2D.

### Phase 2: Problème architectural (CORRIGÉ DÉFINITIVEMENT)
**"L'affichage des texte doit rester dans la taille de la fenêtre et pas l'espace monde"**

#### Cause Racine:
- `RenderContext.drawText()` passait des **coordonnées MONDE** à `view.drawString()`
- Mais `view.at` contenait DÉJÀ la transformation de zoom
- Donc même avec `txtCantChangeSize=true`, le texte zoomait quand même!
- Même problème pour les **pointes de flèches** dans `drawArrow()`

#### Explication Technique:
```
Coordonnées MONDE (en mètres)  →  [view.at avec zoom]  →  Coordonnées ÉCRAN (en pixels)
```

Quand on passe des coordonnées monde à `view.drawString()`, la transformation `view.at` les multiplie par le zoom.
Résultat: le texte se retrouve à la MAUVAISE position et ZOOM avec la vue.

## ✅ Solution Simplifiée FINALE

### Configuration du repère (CADWorkshopView)

```java
// Repère STANDARD : X+ droite, Y+ haut
setInvertXAxis(false);  // X+ vers la droite (normal)
setInvertYAxis(true);   // Y+ vers le haut (Java2D a Y+ vers le bas par défaut)
```

**Le moteur W2D gère TOUT. RenderContext ne fait RIEN de spécial.**

### RenderContext.drawText() - SOLUTION FINALE CORRECTE

**ERREUR INITIALE #1 (conversion monde→écran):**
```java
public Shape drawText(..., double worldX, double worldY, ...) {
    Point2D screenPos = view.convertRealXYToViewXY(worldX, worldY);  // ❌ FAUX!
    return view.drawString(g, text,
        (float) screenPos.getX(), (float) screenPos.getY(),  // ❌ Coordonnées ÉCRAN!
        ...
        true,  // txtCantChangeSize
        1.0f   // Size
    );
}
```

**ERREUR #2 (coordonnées monde OK, mais Size constant):**
```java
public Shape drawText(..., double worldX, double worldY, ...) {
    return view.drawString(g, text,
        (float) worldX, (float) worldY,  // ✅ Coordonnées MONDE (OK!)
        ...
        true,    // txtCantChangeSize = true
        1.0f     // ❌ Size CONSTANT (FAUX!)
    );
}
```

**✅ SOLUTION FINALE CORRECTE:**
```java
public Shape drawText(String text, double worldX, double worldY, double rotation,
                      AlignTexteX alignX, AlignTexteY alignY) {
    // CLEF: Pour que txtCantChangeSize fonctionne, Size doit être inversement
    // proportionnel au zoom : Size = 1.0 / Zoom
    double zoom = getZoom();
    float size = (float) (1.0 / zoom);

    return view.drawString(g, text,
        (float) worldX, (float) worldY,  // ✅ Coordonnées MONDE
        (float) rotation,
        alignX, alignY,
        true,    // txtCantChangeSize = true
        size     // ✅ Size = 1/zoom pour taille écran constante!
    );
}
```

**EXPLICATION**:
- ✅ Passer les coordonnées MONDE directement (PAS de conversion!)
- ✅ **CRUCIAL**: `Size = 1.0 / Zoom` pour que le texte reste à taille constante
- Le moteur W2D avec `txtCantChangeSize=true` et `Size=1/Zoom` gère :
  - ✅ Annulation du zoom (texte taille fixe)
  - ✅ Application des inversions d'axes (via setInvertXAxis/setInvertYAxis)
  - ✅ Rotations
  - ✅ Retours à la ligne (`\n`)

### RenderContext.drawArrow() - CORRECTION TAILLE POINTES

**AVANT (CASSÉ):**
```java
public Shape drawArrow(...) {
    return view.drawArrow2(g,
        new Point2D.Double(startX, startY),
        new Point2D.Double(endX, endY),
        1.0f, 1.0f,  // ❌ Taille fixe → pointes zoomaient!
        ...
    );
}
```

**APRÈS (CORRIGÉ):**
```java
public Shape drawArrow(...) {
    // Calcule le facteur de zoom actuel
    double zoom = view.at.getScaleX();

    // Inverse le zoom pour garder les pointes à taille écran fixe
    float arrowScale = (float) (1.0 / zoom);

    return view.drawArrow2(g,
        new Point2D.Double(startX, startY),  // Coordonnées monde (OK pour la ligne)
        new Point2D.Double(endX, endY),
        arrowScale, arrowScale,  // ✅ Pointes à taille écran fixe!
        ...
    );
}
```

### Autres méthodes corrigées:
- ✅ `drawTextWithBackground()` - même technique que `drawText()` (screen coords + inversions-only transform)
- ✅ `drawDimension()` - inverse-scale les pointes de flèches avec `arrowSize / zoom`

## 📋 Comportement Attendu Maintenant

### Texte (drawText, drawTextWithBackground)
- ✅ Position suit les coordonnées monde (si objet bouge, texte suit)
- ✅ Taille reste FIXE en pixels écran (zoom n'affecte PAS la taille)
- ✅ Inversions d'axes PRÉSERVÉES (si axe Y inversé, texte aussi)

### Flèches (drawArrow, drawDimension)
- ✅ LIGNE suit les coordonnées monde (si objet bouge, ligne suit)
- ✅ LIGNE s'étire avec le zoom (objet plus grand = ligne plus longue)
- ✅ POINTES restent à taille écran FIXE (zoom n'affecte PAS les pointes)

### Objets Métier
- **FloatingPlatform** → UNIQUEMENT `ctx.drawArrow()` + `ctx.drawText()`
- **SolarPanel** → UNIQUEMENT via FloatingPlatform.renderPanel()
- **Mooring** → UNIQUEMENT `ctx.drawArrow()` + `ctx.drawText()`
- **CADDimension** → UNIQUEMENT `ctx.drawDimension()`
- **CADMachine** → UNIQUEMENT `ctx.drawText()`

### ❌ INTERDIT pour les objets métier:
- `ctx.getGraphics()` - sauf pour overlay/console
- `g.setColor()`, `g.setStroke()`, `g.fill()`, `g.draw()`
- Manipulation directe de `view.at`

## 🎯 Résultat Final Attendu

**Quand l'utilisateur ZOOM:**
- ✅ Les objets (plateformes, panneaux) s'agrandissent (comportement normal)
- ✅ Les lignes/contours s'agrandissent (comportement normal)
- ✅ Le TEXTE reste à la MÊME taille en pixels écran (taille fixe!)
- ✅ Les POINTES de flèches restent à la MÊME taille en pixels écran (taille fixe!)

**Tout le texte et symboles UI:**
- ✅ Noms des plateformes
- ✅ Dimensions (m, kg)
- ✅ Puissances des panneaux (Wc)
- ✅ Profondeurs des amarrages (m, kN)
- ✅ Légendes et statistiques
- ✅ Cotations entre objets
- ✅ Pointes de flèches et symboles

## 📝 Leçons Apprises

### ❌ Ce qu'il NE faut PAS faire
- Essayer de gérer les inversions d'axes dans RenderContext
- Convertir monde→écran manuellement
- Manipuler `view.at` directement
- Sur-compliquer une API simple

### ✅ Ce qu'il FAUT faire
- Utiliser un repère de référence STANDARD (X+ droite, Y+ haut)
- Laisser le moteur W2D gérer les transformations
- Configurer `setInvertYAxis(true)` pour avoir Y+ vers le haut
- Garder RenderContext ultra-simple : juste un wrapper

### 🎯 Résultat Final

**API RenderContext - EXTRÊMEMENT SIMPLE** :
```java
// Texte
ctx.drawText("Hello", worldX, worldY);

// Flèches
ctx.drawArrow(startX, startY, endX, endY, false, false);

// Dimensions
ctx.drawDimension("50m", startX, startY, endX, endY, offset, arrowSize);
```

**Configuration Vue - UNE SEULE FOIS** :
```java
setInvertXAxis(false);  // X+ droite (standard)
setInvertYAxis(true);   // Y+ haut (inverse Java2D)
```

C'EST TOUT. Pas de magie, pas de complications.

Date: 2025-10-11 (Simplification architecturale complète)

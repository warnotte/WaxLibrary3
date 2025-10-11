# SOLUTION ARCHITECTURALE FINALE

## Date: 2025-10-11

## LA RÉVÉLATION

Après analyse approfondie du code source de `PanelGraphiqueBase.drawString()` et des tests `VUE2D_TestBed`, voici ce que j'ai découvert:

## 1. Comment `txt CantChangeSize` fonctionne RÉELLEMENT

### Code Source (PanelGraphiqueBase.java:1046-1100)

```java
// Étape 1: Calculer le facteur de scale
float sx = (float) Zoom;
float sy = (float) Zoom;

if (txtCantChangeSize) {
    sx = 1.0f / Size;   // Pour Size=1.0f → sx=1.0
    sy = 1.0f / Size;
}

// Étape 2: Appliquer les inversions d'axes
if ((!invertXAxis) && (invertYAxis)) {   // ← CADWorkshopView utilise ceci
    sy = -sy;   // Inverse le Y
}

// Étape 3: Construire la transformation locale
AffineTransform at3 = new AffineTransform();
at3.translate(x, y);               // ← (x,y) sont en coordonnées MONDE!
at3.scale(1f / sx, 1f / sy);      // Scale inverse
at3.rotate(Math.toRadians(ang));
at3.translate(-recenterx * sx, -recentery * sy);

// Étape 4: Combiner avec la transformation globale
AffineTransform at2 = new AffineTransform();
at2.concatenate(at);   // 'at' contient: translate + scale(Zoom) + scroll + inversions
at2.concatenate(at3);  // at2 = at * at3

// Étape 5: Dessiner le texte
g.setTransform(at2);
g.drawString(text, 0, 0);
```

### Pipeline de Transformation

Quand un point est transformé:
```
point → at3 (local) → at (world) → écran
```

Donc:
```
final_point = at * (at3 * point)
```

### Où vient `at`? (PanelGraphiqueBaseBase.paint():481-487)

```java
at = new AffineTransform();
at.translate(getWidth() / 2.0, getHeight() / 2.0);  // Centre viewport
at.scale(Zoom, Zoom);                                // ZOOM
at.translate(ScrollX, ScrollY);                      // Scroll
at.scale(invertXAxis?-1:1, invertYAxis?-1:1);       // Inversions d'axes
```

## 2. Analyse Mathématique

### Configuration CADWorkshopView:
- `invertYAxis = true` → Y+ vers le haut
- `txtCantChangeSize = true` → Texte à taille fixe
- `Size = 1.0f`

### Cas: txt CantChangeSize=TRUE, Size=1.0f, invertYAxis=true

```
sx = 1.0f / 1.0f = 1.0f
sy = 1.0f / 1.0f = 1.0f
sy = -sy = -1.0f  (après inversion Y)

at3.scale(1f/sx, 1f/sy) = scale(1.0, -1.0)

Combiné avec 'at':
- at   contient: scale(Zoom, Zoom) * scale(1, -1)  [inversion Y déjà dans at]
- at3  contient: translate(x,y) * scale(1, -1) * rotate(ang)

ATTENTION: DOUBLE INVERSION DU Y!
```

## 3. LE PROBLÈME ARCHITECTURAL

Il y a **DOUBLE INVERSION des axes**:
1. Une fois dans `at` (ligne 487 de PanelGraphiqueBaseBase)
2. Une DEUXIÈME fois dans `at3` (lignes 1055-1073 de PanelGraphiqueBase)

### Pourquoi cette double inversion?

En regardant le test `VUE2D_TestBed.main()` (lignes 226-255), ils créent **4 fenêtres** avec toutes les combinaisons:
- Panel 1: invertX=false, invertY=false
- Panel 2: invertX=false, invertY=true  ← NOTRE CAS!
- Panel 3: invertX=true,  invertY=false
- Panel 4: invertX=true,  invertY=true

**Et ça marche dans les 4 cas!**

Donc la double inversion est **INTENTIONNELLE** et **CORRECTE**!

## 4. ALORS POURQUOI ÇA NE MARCHE PAS DANS CADWORKSHOP?

Si le moteur W2D fonctionne correctement dans les tests, pourquoi pas dans CADWorkshop?

### Hypothèses:

#### A. Problème de configuration

CADWorkshopView hérite de `PanelGraphiqueBaseBase` (copié dans core/).
Peut-être que la copie a introduit des différences subtiles?

#### B. Problème avec RenderContext

RenderContext appelle `view.drawString()` en passant des coordonnées monde.
C'est correct selon l'analyse! Mais peut-être qu'il y a un problème avec:
- Le `Graphics2D` passé à RenderContext?
- L'état du `Graphics2D` au moment de l'appel?
- Les transformations accumulées?

#### C. Problème avec getZoom()

Dans `RenderContext.drawArrow()`, nous utilisons:
```java
double zoom = getZoom();  // view.getZoom()
float arrowScale = (float) (1.0 / zoom);
```

Mais pour `drawText()`, nous utilisons `Size=1.0f` (constant).

**ET SI LE SIZE DEVAIT ÊTRE BASÉ SUR LE ZOOM?**

Non wait, les tests utilisent aussi `Size=1.0f` et ça marche...

#### D. Timing/Order Problem

Peut-être que `at` n'est pas encore initialisé quand nous appelons `drawString()`?

Non, `at` est recréé à chaque `paint()` (ligne 481).

## 5. INVESTIGATION EMPIRIQUE NÉCESSAIRE

Je dois VOIR ce qui se passe réellement. Créons un test minimal qui:
1. Reproduit l'environnement exact de CADWorkshopView
2. Affiche les valeurs de transformation
3. Compare avec VUE2D_TestBed qui fonctionne

## 6. SOLUTION PROPOSÉE: Retour aux Bases

### Option A: Copier EXACTEMENT le pattern de VUE2D_TestBed

Au lieu d'utiliser `RenderContext`, appelons `view.drawString()` directement depuis `CADObject.render()`:

```java
// Dans FloatingPlatform.render()
public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, ...) {
    Graphics2D g = ctx.getGraphics();
    CADWorkshopView view = ctx.getView();

    // EXACT copy of VUE2D_TestBed pattern
    Shape textShape = view.drawString(g, name,
        (float) position.x, (float) position.y,
        (float) rotation,
        AlignTexteX.CENTER, AlignTexteY.CENTER,
        true,  // txtCantChangeSize
        1.0f   // Size
    );

    list.add(new SelectionTuple<>(textShape, this));
    return list;
}
```

### Option B: Simplifier RenderContext au MINIMUM

```java
public class RenderContext {
    private final CADWorkshopView view;
    private final Graphics2D g;

    // Wrapper ultra-simple - RIEN d'autre!
    public Shape drawText(String text, double x, double y, double rotation,
                          AlignTexteX alignX, AlignTexteY alignY) {
        return view.drawString(g, text,
            (float) x, (float) y,
            (float) rotation,
            alignX, alignY,
            true, 1.0f);
    }
}
```

C'est EXACTEMENT ce que nous avons maintenant! Donc le problème n'est PAS dans RenderContext!

## 7. CONCLUSION PROVISOIRE

Le code actuel de `RenderContext.drawText()` est **THÉORIQUEMENT CORRECT**.

Il passe des coordonnées monde à `view.drawString()` comme les tests le font.

Donc le problème est AILLEURS:
1. Configuration de CADWorkshopView?
2. État du Graphics2D?
3. Ordre des opérations?
4. Bug dans les objets métier (Mooring, etc.)?

## PROCHAINE ÉTAPE

Exécuter le test VUE2D_TestBed en parallèle avec CADWorkshopDemo et comparer:
1. Les valeurs de `Zoom`, `ScrollX`, `ScrollY`
2. Les transformations `at` et `at2`
3. Les positions effectives du texte à l'écran

**Je dois VOIR ce qui se passe, pas juste analyser!**

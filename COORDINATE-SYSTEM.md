# SYSTÈME DE COORDONNÉES - W2D Engine

## ✅ REPÈRE DE RÉFÉRENCE STANDARD

Le moteur W2D utilise un repère **STANDARD MATHÉMATIQUE** :

```
        Y+ (haut)
         |
         |
         |
         +--------> X+ (droite)
       (0,0)
```

### Axes
- **X+ → DROITE** (positif vers la droite)
- **Y+ → HAUT** (positif vers le haut)
- **Origine (0,0)** → Centre de la vue par défaut

### Rotations
- **0°** → Horizontal (vers la droite)
- **90°** → Vertical (vers le haut)
- **Sens anti-horaire** (trigonométrique standard)

## 🎯 Simplification

### Ce que RenderContext FAIT
- Passer les coordonnées MONDE à `view.drawString()`
- Utiliser `txtCantChangeSize=true` pour texte à taille fixe
- Utiliser `1.0 / zoom` pour flèches à taille fixe
- **RIEN D'AUTRE**

### Ce que RenderContext NE FAIT PAS
- ❌ Gérer les inversions d'axes
- ❌ Convertir monde→écran
- ❌ Manipuler `view.at`
- ❌ Compliquer les choses

### Si l'utilisateur veut inverser les axes
Il utilise directement les méthodes du moteur W2D :
- `view.setInvertXAxis(true/false)`
- `view.setInvertYAxis(true/false)`

**Ce n'est PAS la responsabilité de RenderContext !**

## 📋 Configuration par défaut

Pour le CAD Workshop Demo :
```java
// Repère standard : X+ droite, Y+ haut
view.setInvertXAxis(false);
view.setInvertYAxis(true);   // Java2D a Y+ vers le bas, donc on inverse
```

## 🔧 API Simplifiée RenderContext

```java
// Texte (coordonnées monde)
ctx.drawText("Hello", worldX, worldY);

// Flèches (coordonnées monde)
ctx.drawArrow(startX, startY, endX, endY, false, false);

// Dimensions
ctx.drawDimension("50m", startX, startY, endX, endY, offset, arrowSize);
```

C'est TOUT. Pas de complications.

Date: 2025-10-11 (Simplification complète)

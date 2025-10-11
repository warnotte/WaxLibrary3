# CAD Workshop Demo - Guide de Démarrage Rapide

## Lancer la démo

### Depuis votre IDE (IntelliJ, Eclipse, etc.)
1. Ouvrez le projet WaxLibrary3_2
2. Naviguez vers `io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo`
3. Cliquez-droit → Run As → Java Application

### Depuis Maven
```bash
cd WaxLibrary3_2
mvn clean compile
mvn exec:java -Dexec.mainClass="io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo"
```

### Depuis la ligne de commande
```bash
cd WaxLibrary3_2
javac -d target/classes -sourcepath src/main/java src/main/java/io/github/warnotte/waxlib3/W2D/PanelGraphique/demo/cadworkshop/*.java
java -cp target/classes io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo
```

## Premiers pas

### Interface
```
┌────────────────────────────────────┬──────────────────┐
│                                    │                  │
│        Vue CAD Workshop            │  Panneau Debug   │
│                                    │                  │
│  [Machines, cotations, labels...]  │  [Infos temps    │
│                                    │   réel]          │
│                                    │                  │
│                                    │  [Contrôles]     │
│                                    │                  │
└────────────────────────────────────┴──────────────────┘
```

### Contrôles de base

**Souris**
- 🖱️ Molette : Zoom in/out
- 🖱️ Bouton milieu + drag : Déplacer la vue (pan)
- 🖱️ Bouton gauche : Sélectionner un objet
- 🖱️ Bouton gauche + drag : Rectangle de sélection

**Clavier**
- `A` : Active/désactive l'animation
- `T` : Change le mode de test (Normal → Stress → Rotation → Text → Arrow)
- `M` : Active le mode mesure
- `S` : Outil sélection
- `P` : Outil placement
- `R` : Outil rotation
- `L` : Outil annotation
- `K` : Outil lien
- `SHIFT` : Ajouter à la sélection
- `CTRL` : Retirer de la sélection
- `Flèches` : Déplacer la vue
- `+/-` : Zoom

### Panneau de Debug

Le panneau de droite affiche en temps réel :

- **VIEWPORT** : Zoom, scroll, angle, taille
- **AFFINE TRANSFORM** : ScaleX/Y, ShearX/Y, TransX/Y
- **MOUSE** : Coordonnées monde/vue, delta, état
- **VIEW RECTANGLE** : Zone visible en coordonnées monde
- **WORKSPACE** : Nombre d'objets par type
- **SELECTION** : Objets sélectionnés avec détails
- **CONFIGURATION** : Axes inversés, grille, etc.
- **STATE** : Outil actif, mode de test

**Boutons du panneau**
- `Reset View` : Remet zoom=1 et centré
- `Toggle Axes` : Inverse l'axe Y
- `Next Test` : Change le mode de test
- `Clear Select` : Vide la sélection

## Modes de Test

### 1. Normal Mode (par défaut)
Vue standard de l'atelier CAO avec :
- 3 machines pré-placées (Turbine, Pompe, Compresseur)
- 2 cotations
- Échantillons de texte avec différents alignements

**Ce qu'on teste :**
- Sélection d'objets
- Texte avec `txtCantChangeSize=true`
- Rotation d'objets
- Alignements de texte

### 2. Stress Test
Génère 100 machines en grille (10×10) pour tester :
- Performance de rendu avec nombreux objets
- FPS avec beaucoup de drawString
- Système de sélection sous charge

### 3. Rotation Test
Animation continue avec :
- Machine qui tourne en temps réel
- 8 flèches tournantes
- Test des rotations combinées

**Ce qu'on teste :**
- `drawString()` avec objet en rotation
- `drawArrow2()` dans toutes les orientations
- Synchronisation animation/rendu

### 4. Text Test
Matrice complète des 9 alignements :
- LEFT × TOP/CENTER/BOTTOM
- CENTER × TOP/CENTER/BOTTOM
- RIGHT × TOP/CENTER/BOTTOM

**Ce qu'on teste :**
- Tous les cas de `AlignTexteX` × `AlignTexteY`
- Point d'ancrage (marqué en rouge)
- `txtCantChangeSize` à différents zooms

### 5. Arrow Test
Teste `drawArrow2()` avec :
- Flèches horizontales
- Flèches verticales
- Flèches diagonales
- Différentes tailles de pointes
- Future: `drawArrowWithString()`

## Scénarios de Test Recommandés

### Test 1 : Texte à Taille Fixe
1. Mode : Normal
2. Zoomez de 0.5x à 10x (molette)
3. **Résultat attendu** : Les textes "Fixed Size" gardent la même taille à l'écran
4. **Validation** : txtCantChangeSize fonctionne ✅

### Test 2 : Rotation + Texte
1. Mode : Rotation Test
2. Activez l'animation (touche `A`)
3. Zoomez pendant la rotation
4. **Résultat attendu** : Le texte suit la rotation, taille constante
5. **Validation** : GlyphVector + rotation ✅

### Test 3 : Axes Inversés
1. Menu View → Invert Y Axis
2. Vérifiez que la sélection fonctionne
3. Menu View → Invert X Axis aussi
4. **Résultat attendu** : Tout fonctionne dans les 4 combinaisons
5. **Validation** : Axes inversés OK ✅

### Test 4 : Sélection Multiple
1. Mode : Normal
2. Cliquez sur Turbine → sélectionnée (magenta)
3. SHIFT + clic sur Pompe → 2 objets sélectionnés
4. CTRL + clic sur Turbine → retirée de la sélection
5. **Résultat attendu** : Panneau debug montre "Selected: 1"
6. **Validation** : Sélection SHIFT/CTRL ✅

### Test 5 : Performance
1. Mode : Stress Test
2. Regardez les FPS (coin haut gauche)
3. Zoomez/déplacez la vue
4. **Résultat attendu** : FPS > 30 avec 100 objets
5. **Validation** : Performance acceptable ✅

### Test 6 : Tous les Alignements
1. Mode : Text Test
2. Zoomez pour voir les détails
3. Vérifiez que chaque texte est correctement aligné sur son point rouge
4. **Résultat attendu** : 9 cas corrects
5. **Validation** : Alignements parfaits ✅

## Debugging

### Problème : Texte mal orienté
**Solution** : Vérifiez dans le panneau debug :
- `InvertX/Y` : Axes inversés ?
- `Angle` : Rotation du viewport ?
- Comparez avec mode Text Test

### Problème : Sélection ne fonctionne pas
**Solution** :
- Activez "Debug Selection Shapes" (checkbox)
- Vous verrez les zones cliquables en orange
- Vérifiez que les shapes sont correctes

### Problème : FPS faible
**Solution** :
- Vérifiez le nombre d'objets (panneau debug)
- En mode Stress, c'est normal avec 100 objets
- Désactivez l'animation (touche `A`)

## Cas Limites Testés

### ✅ drawString qui "déconne si on rotate AT"
**Fix** : Utilise GlyphVector au lieu de g.setTransform()
**Test** : Mode Rotation avec animation

### ✅ txtCantChangeSize ne marche pas
**Fix** : Calcul correct du scaling avec sx/sy
**Test** : Mode Normal, zoomez extrême

### ✅ Sélection incorrecte avec axes inversés
**Fix** : Rectangle de sélection corrigé (CorrigeSelection)
**Test** : Invertir Y puis X, sélection rectangle

### ✅ Flèches mal orientées
**Fix** : drawArrow2 avec AffineTransform local
**Test** : Mode Arrow Test, 8 directions

## Développement

### Ajouter un nouvel objet CAO
1. Créez une classe qui implémente `CADObject`
2. Implémentez `render()` qui retourne les `SelectionTuple`
3. Ajoutez-le au workspace : `workspace.addObject(monObjet)`

### Ajouter un nouveau mode de test
1. Ajoutez une valeur à `TestMode` enum
2. Créez une méthode `renderMonTest(Graphics2D g)` dans `CADWorkshopView`
3. Ajoutez le case dans le switch de `doPaint()`

### Ajouter un nouvel outil
1. Ajoutez une valeur à `CADTool` enum
2. Créez une classe `MonOutilTool` dans `controller/tools/`
3. Gérez la logique dans les events souris de la vue

## Support

Problèmes ? Vérifiez d'abord :
1. Le panneau de debug pour l'état système
2. La console pour les erreurs
3. Les checkbox "Debug Selection Shapes"

Cette démo valide **TOUS** les bugs historiques du moteur W2D !
Bon test ! 🎨🔧

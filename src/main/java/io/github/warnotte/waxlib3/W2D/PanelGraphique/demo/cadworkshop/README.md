# CAD Workshop Demo - Test Complet du Moteur W2D

## Vue d'ensemble

Cette démonstration implémente un **atelier CAO interactif** qui teste exhaustivement toutes les capacités du moteur graphique W2D, notamment :

- ✅ Rendu de texte avec taille fixe malgré le zoom
- ✅ Rotations combinées (objets + texte + viewport)
- ✅ Flèches et cotations avec annotations
- ✅ Sélection multi-objets dans toutes les configurations
- ✅ 4 combinaisons d'axes inversés (X/Y)
- ✅ GlyphVector vs drawString
- ✅ Transformations AffineTransform complexes

## Lancer la démo

```java
java io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo
```

## Fonctionnalités

### Objets CAO
1. **Machines** - Équipements industriels avec rotation 3D
2. **Dimensions** - Cotations avec flèches et mesures
3. **Labels** - Étiquettes annotées avec leader lines
4. **Links** - Connexions entre machines

### Outils interactifs
- **[S]elect** - Sélection et manipulation d'objets
- **[P]lace** - Placer une nouvelle machine
- **[R]otate** - Rotation interactive d'objets
- **[M]easure** - Créer des cotations
- **[L]abel** - Annoter un objet
- **[K]** - Créer un lien entre 2 machines

### Panneau de debug
Affiche en temps réel :
- État des transformations AffineTransform
- Coordonnées souris (world/view)
- Objet sélectionné avec détails
- Statistiques de rendu
- Flags système

## Tests automatisés

### Régression
- Placement dans les 4 quadrants
- Zoom extrême (0.5x à 20x)
- Rotation continue (0° à 360°)
- Toutes combinaisons d'axes inversés
- Sélection avec rectangle

### Stress
- 100+ objets simultanés
- Animations continues
- Mesure de FPS

## Architecture

```
demo.cadworkshop/
├── model/              // Objets métier
├── view/               // Rendu graphique
├── controller/         // Logique d'interaction
└── tests/              // Tests automatisés
```

## Cas d'usage validés

Cette démo valide **tous les bugs historiques** :
- ❌ drawStringOLD qui "déconne si on rotate AT" → ✅ Résolu
- ❌ Texte qui change de taille au zoom → ✅ txtCantChangeSize fonctionne
- ❌ Sélection incorrecte avec axes inversés → ✅ Corrigé
- ❌ Flèches mal orientées → ✅ drawArrow2/3 testés

## Notes techniques

### Gestion du texte
Utilise la nouvelle méthode `drawString()` avec GlyphVector qui gère correctement :
- Les rotations combinées
- Les inversions d'axes
- La taille fixe indépendante du zoom

### Gestion des flèches
Utilise `drawArrow2()` et `drawArrowWithString()` pour les cotations.

### Transformations
Architecture en 3 niveaux :
1. **Global AT** - Viewport (zoom, pan, rotation)
2. **Local AT** - Position/rotation de l'objet
3. **Element AT** - Transformation du texte/flèche

---

**Auteur** : Démo complète du moteur W2D
**Date** : 2025
**License** : Même que WaxLibrary3

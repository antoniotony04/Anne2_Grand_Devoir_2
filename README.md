# **POO - Grand Devoir 2**

Ce projet fait partie du cours de **Programmation Orientée Objet (POO)** et consiste à créer un jeu de survie 2D avec une interface graphique. Il est basé sur la logique et les éléments du premier grand devoir.

---

## **Consignes du projet**

- **Travail en équipe** : Travaillez par équipes de **2 à 3 membres**.
- **Soumission** : Déposez le projet sur **Moodle**. En cas de problème de téléchargement, contactez les assistants (**Iulia Stanica** ou **Alexandru Bratosin**) via **Teams**.
- **Date limite** :
  - **Lundi 13 janvier 2025, 8:00 AM** (13e semaine).
  - **Présentation obligatoire** pendant la séance de TP correspondante.
  - Un devoir déposé sur Moodle mais non présenté obtiendra une note de **0**.
- **Questions** : Posez vos questions via le groupe Teams de POO ou par message privé.

---

## **Spécifications techniques**

### **Technologies**
### **JavaFX**

### **Tâches principales**
1. Implémenter l’interface graphique du jeu (2D).
2. Créer un menu principal avec les boutons suivants :
   - **Resume** : Charger une partie précédente à partir d’un fichier.
   - **New Game** : Générer une nouvelle carte aléatoire.
   - **Options** : Modifier les paramètres (ex. fond d’écran, musique).
   - **Help** : Afficher les instructions (contrôles, objectifs).
   - **Exit** : Quitter le jeu.
3. Afficher la carte sous forme de matrice où le joueur peut se déplacer avec les touches **WASD**.
4. Interagir avec les cellules :
   - Cellules vides : Créer un objet.
   - Cellules contenant un objet : Ramasser l’objet.
   - Cellules contenant un ennemi : Déclencher un combat (alternance coups joueur/ennemi).
5. Assurer des changements en temps réel dans l’interface :
   - Afficher des objets, ennemis, et changements graphiques spécifiques.
   - Ajouter des animations lors des combats dans une fenêtre séparée.
6. Afficher les ressources et l’inventaire du joueur.
7. Intégrer un système de points et de conditions de victoire/défaite.

### **Critères graphiques**
- Tous les objets doivent être représentés graphiquement (images, icônes, couleurs, etc.).
- En **JavaFX**, l’utilisation de fichiers CSS est obligatoire.

---

## **Barème**

| Fonctionnalité                                    | Points |
|---------------------------------------------------|--------|
| Scène Menu                                       | 0.5    |
| Boutons fonctionnels dans le menu (5 x 0.2)      | 1.0    |
| Scène Combat                                     | 1.0    |
| Interface graphique plaisante                    | 2.0    |
| Génération aléatoire des cartes                  | 0.5    |
| Changements en temps réel dans l’interface       | 1.5    |
| Système de points / Victoire-Défaite             | 0.5    |
| Inventaire                                       | 0.5    |
| Affichage des ressources dans l’interface        | 0.5    |
| Intégration des classes du premier devoir        | 1.0    |
| Gestion des cas extrêmes (pas de crash)          | 1.0    |

---

## **Livrables**

- **Code source** : Déposé sur Moodle.
- **Documentation** : Instructions pour exécuter le projet.
- **Présentation** : Pendant la séance de TP correspondante.

---

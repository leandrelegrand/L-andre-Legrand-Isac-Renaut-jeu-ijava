Présentation de Shark'itecte
===================
Plusieurs joueurs s'affronte dans un quizz de culture général afin construire la plus grande tour !
Pour ce faire, choisissez le nombre de joueur et commencez à vous amusez ! 
Répondez à différentes question de différentes difficultées: Facile, Normal, Difficile et gagnez un nombre de point équivalant !

## Boutique
Les points vous servirons à acheter différentes choses dans la boutique: 

*   Des blocs pour faire grandir votre tour
*   Des bonus
*   Des malus


## Condition de victoire
Le vainqueur est celui dont sa tour à atteint 10 en premier ! Les points ne servent uniquement qu'à acheter des choses dans la boutique
Ils ne comptent pas pour la victoire.


## Ordre d'un tour
L'ordre du tour se fait comme ceci:

*  Le joueur choisit une difficulté
*  Il répond à la question
*  Si la réponse est bonne: il gagne un nombre de points équivalent à la difficulté choisie
*  Si la réponse est fausse: le joueur ne gagne pas de point
*  Le joueur à accès à la boutique ainsi que son inventaire afin d'acheter / d'utiliser un objet
*  On passe au tour du prochain joueur 


## Ajout de questions
Pour ajouter des questions:

*   Ouvrez "Questions.csv"
*   Ajoutez votre question, votre réponse et votre difficulté en respectant le format des autres questions (Question, réponse, difficulté (1= facile, 2= moyen, 3= difficile) )
*   Sauvegarder le document 
*   Pensez à recompiler avant de relancer le jeu !

## Système de sauvegarde
Si vous voulez à tout moment quitter le jeu mais ne pas perdre la progression de la partie. Un système de sauvegarde est intégré !
Il vous suffit de taper "exit" ce qui sauvegardera la partie et vous fera quitter celle-ci

Pour reprendre une partie en cours, au lancement du jeu, une option est implémentée pour vous permettre de reprendre une partie sauvegardée précédemment
Il vous suffira de choisir "Continuer la partie"



## Utilisation
ATTENTION !!! Il faudra bien faire attention à se trouver dans le fichier qui contient le jeu afin de lancer celui-ci.

Afin d'utiliser le projet, il suffit de taper les commandes suivantes:

./compile.sh
//compilation des fichiers présents dans 'src' et création des fichiers '.class' dans 'classes'

./run.sh Shark'itecte
//lancement de la simulation


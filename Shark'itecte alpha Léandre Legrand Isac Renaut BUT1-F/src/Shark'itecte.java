import extensions.File;
import extensions.CSVFile;

class Sharkitec extends Program {

    final int NIVEAU_MAX = 10;

    // Créer une nouvelle question
    Question newQuestion(String question, String reponse, String difficulte) {
        Question q = new Question();
        q.question = question;
        q.reponse = reponse;
        q.dejafait = false;
        q.difficulte = difficulte;
        return q;
    }

    // Créer un nouveau joueur
    Joueur newJoueur(String pseudo) {
        Joueur j = new Joueur();
        j.pseudo = pseudo;
        j.dernieretage = 0;
        j.score = 0;
        j.inventaire = new String[4]; // Inventaire limité à 4 objets
        return j;
    }

    // Fonction de trim personnalisée
    String trimTexte(String texte) {
        int start = 0;
        int end = length(texte) - 1;
        while (start <= end && (charAt(texte, start) == ' ' || charAt(texte, start) == '\n' || charAt(texte, start) == '\r')) {
            start++;
        }
        while (end >= start && (charAt(texte, end) == ' ' || charAt(texte, end) == '\n' || charAt(texte, end) == '\r')) {
            end--;
        }
        return texte.substring(start, end + 1);
    }

    // Fonction pour séparer une chaîne en utilisant un séparateur personnalisé
    String[] séparer(String texte, String séparateur) {
        int count = 0;
        int index = 0;

        // Comptage du nombre de séparateurs
        while (index != -1) {
            index = texte.indexOf(séparateur, index); // j'arrivais pas à le coder
            if (index != -1) {
                count++;
                index += séparateur.length();
            }
        }

        // Créer un tableau avec le nombre de segments
        String[] result = new String[count + 1];
        int start = 0;
        int i = 0;
        index = 0;

        // Séparation de la chaîne
        while ((index = texte.indexOf(séparateur, start)) != -1) {
            result[i++] = texte.substring(start, index);
            start = index + séparateur.length();
        }
        result[i] = texte.substring(start);
        return result;
    }

    // Trouver l'indice d'une sous-chaîne dans une chaîne
    int trouverIndice(String texte, String sousChaine, int debut) {
        for (int i = debut; i <= length(texte) - length(sousChaine); i++) {
            if (equals(texte.substring(i, i + length(sousChaine)), sousChaine)) {
                return i;
            }
        }
        return -1;
    }

    // Vérifier si la réponse donnée par le joueur est valide
    boolean estReponseValide(String reponseJoueur, String reponsesValides) {
        // Séparer les différentes réponses possibles en utilisant le séparateur "|"
        String[] reponses = séparer(reponsesValides, "|");

        // Normaliser la réponse du joueur : mettre en minuscules et enlever les espaces superflus
        reponseJoueur = trimTexte(toLowerCase(reponseJoueur));

        // Comparer la réponse du joueur avec chaque réponse valide dans le tableau
        for (int i = 0; i < length(reponses); i++) {
            // Normaliser aussi chaque réponse valide
            String reponsePossible = trimTexte(toLowerCase(reponses[i]));
            println(reponsePossible);

            // Vérifier si la réponse du joueur correspond à la réponse valide
            if (equals(reponseJoueur, reponsePossible)) {
                return true;  // Si une correspondance est trouvée, la réponse est valide
            }
        }

        // Si aucune correspondance n'est trouvée, la réponse est invalide
        return false;
    }

    // Affichage du tour d'un joueur
    String afficherTour(Joueur joueur) {
        String affichage = joueur.pseudo + " : ";
        for (int i = 0; i < joueur.dernieretage; i++) {
            affichage += (i + 1) + " | ";
        }
        affichage += "Score : " + joueur.score;
        return affichage;
    }

    // Jouer un tour avec gestion de la difficulté et réinitialisation des questions
    void jouerTour(Joueur[] joueurs, Question[] questionnaireFacile, Question[] questionnaireMoyen, Question[] questionnaireDifficile) {
        clearScreen(); // Efface l'écran avant d'afficher les informations

        // Affichage de l'état du jeu avant de commencer le tour
        println("----------- État du jeu -----------");
        for (int i = 0; i < length(joueurs); i++) {
            println(afficherTour(joueurs[i]));
        }

        // Demander la difficulté aux joueurs
        println("\nChoisissez la difficulté :\n1. Facile (+10 points)\n2. Moyen (+20 points)\n3. Difficile (+30 points)");
        int choixDifficulte = (int)(readInt());

        // Sélectionner le bon questionnaire en fonction de la difficulté
        Question[] questionnaireChoisi;
        if (choixDifficulte == 1) {
            questionnaireChoisi = questionnaireFacile;
        } else if (choixDifficulte == 2) {
            questionnaireChoisi = questionnaireMoyen;
        } else {
            questionnaireChoisi = questionnaireDifficile;
        }

        // Vérifier si toutes les questions du questionnaire choisi ont été posées
        if (toutesQuestionsPosees(questionnaireChoisi)) {
            println("Toutes les questions de cette difficulté ont été posées !");
            println("Réinitialisation des questions...");
            reinitialiserQuestions(questionnaireChoisi);
        }

        // Jouer le tour pour chaque joueur
        for (int j = 0; j < length(joueurs); j++) {
            Joueur joueurActuel = joueurs[j];

            println("\nTour de " + joueurActuel.pseudo);

            // Sélection d'une question non posée
            Question question = null;
            for (int i = 0; i < length(questionnaireChoisi); i++) {
                if (!questionnaireChoisi[i].dejafait) {
                    question = questionnaireChoisi[i];
                    questionnaireChoisi[i].dejafait = true;
                    break; // désolé ça créer des bug
                }
            }

            // Si aucune question n'est trouvée, signaler un problème
            if (question == null) {
                println("Erreur : aucune question disponible. Veuillez vérifier le questionnaire.");
                return;
            }

            // Poser la question
            println(question.question);
            String reponseJoueur = readString();

            if (equals(reponseJoueur, "EXIT")) {
                sauvegarderPartie(joueurs);
                println("Partie sauvegardée. À bientôt !");
                return;
            }

            // Vérification de la réponse et attribution des points
            if (estReponseValide(reponseJoueur, question.reponse)) {
                println("Bonne réponse !");
                if (equals(question.difficulte, "1")) {
                    joueurActuel.score += 10;
                } else if (equals(question.difficulte, "2")) {
                    joueurActuel.score += 20;
                } else if (equals(question.difficulte, "3")) {
                    joueurActuel.score += 30;
                }
            } else {
                println("Mauvaise réponse. La bonne réponse était : " + question.reponse);
            }

            // Affichage du score actuel du joueur
            println("Score actuel de " + joueurActuel.pseudo + " : " + joueurActuel.score);

            // Affichage de l'état du jeu après le tour de ce joueur
            println("----------- État du jeu -----------");
            for (int i = 0; i < length(joueurs); i++) {
                println(afficherTour(joueurs[i]));
            }
        }
    }

    // Charger les questions d'un fichier CSV
    Question[] creerQuestionnaire(String filename, String difficulte) {
        CSVFile fichier = loadCSV(filename);
        int row = 0;

        // Compter le nombre de questions correspondant à la difficulté choisie
        for (int i = 0; i < rowCount(fichier); i++) {
            if (equals(getCell(fichier, i, 2), difficulte)) {
                row++;
            }
        }

        // Initialiser un tableau pour les questions
        Question[] questionnaire = new Question[row];
        int index = 0;

        // Remplir le tableau avec les questions correspondant à la difficulté
        for (int j = 0; j < rowCount(fichier); j++) {
            String question = getCell(fichier, j, 0);
            String reponse = getCell(fichier, j, 1);
            String diff = getCell(fichier, j, 2);

            if (equals(diff, difficulte)) {
                questionnaire[index] = newQuestion(question, reponse, diff);
                index++;
            }
        }
        return questionnaire;
    }

    // Mélanger les questions pour les rendre aléatoires
    void melangerQuestions(Question[] questionnaire) {
        for (int i = 0; i < length(questionnaire); i++) {
            int j = (int)(random() * length(questionnaire));  // Correction ici : conversion vers int
            Question temp = questionnaire[i];
            questionnaire[i] = questionnaire[j];
            questionnaire[j] = temp;
        }
    }

    // Vérifier si toutes les questions ont été posées
    boolean toutesQuestionsPosees(Question[] questionnaire) {
        for (int i = 0; i < length(questionnaire); i++) {
            if (!questionnaire[i].dejafait) {
                return false;
            }
        }
        return true;
    }

    // Réinitialisation des questions
    void reinitialiserQuestions(Question[] questionnaire) {
        for (int i = 0; i < length(questionnaire); i++) {
            questionnaire[i].dejafait = false;
        }
    }

    // Ajouter une boutique avec des bonus et malus
    void boutique(Joueur joueur) {
        println("Bienvenue dans la boutique, " + joueur.pseudo + " !");
        println("1. Malus d'adversaire (-50 points à un autre joueur) : 80 points");
        println("2. Acheter un étage (coût : 20 points) : 20 points");
        println("3. Quitter la boutique");

        int choix = (int)(readInt());

        if (choix == 1 && joueur.score >= 80) {
            joueur.score -= 80;
            // Ajouter l'objet au joueur
            println("Vous avez acheté un Malus Adversaire !");
        } else if (choix == 2 && joueur.score >= 20) {
            joueur.score -= 20;
            joueur.dernieretage++;
            println("Nouveau étage ajouté !");
        } else if (choix == 3) {
            println("À bientôt !");
        } else {
            println("Action impossible. Revenez avec plus de points !");
        }
    }

    // Sauvegarder la partie
    void sauvegarderPartie(Joueur[] joueurs) {
        String[][] contenu = new String[length(joueurs)][4];
        for (int i = 0; i < length(joueurs); i++) {
            contenu[i][0] = joueurs[i].pseudo;  // Nom du joueur
            contenu[i][1] = "" + joueurs[i].score;  // Score du joueur
            contenu[i][2] = "" + joueurs[i].dernieretage;  // Dernier étage du joueur
            contenu[i][3] = String.join(",", joueurs[i].inventaire);  // Inventaire du joueur sous forme de chaîne
        }
        // Sauvegarder les données dans un fichier CSV
        saveCSV(contenu, "../ressources/Sauvegarde.csv");
    }

    // Charger la partie depuis un fichier CSV
    Joueur[] chargerPartie() {
        CSVFile fichier = loadCSV("../ressources/Sauvegarde.csv");
        int rowCount = rowCount(fichier);
        Joueur[] joueurs = new Joueur[rowCount];

        for (int i = 0; i < rowCount; i++) {
            String pseudo = getCell(fichier, i, 0);
            int score = Integer.parseInt(getCell(fichier, i, 1));
            int dernieretage = Integer.parseInt(getCell(fichier, i, 2));
            String[] inventaire = séparer(getCell(fichier, i, 3), ",");

            Joueur joueur = newJoueur(pseudo);
            joueur.score = score;
            joueur.dernieretage = dernieretage;
            joueur.inventaire = inventaire;
            joueurs[i] = joueur;
        }
        return joueurs;
    }

    void testCreerJoueur() {
        Joueur j = newJoueur("TestPlayer");
        
        // Vérification du pseudo
        assertEquals(j.pseudo, "TestPlayer");
        
        // Vérification du dernier étage
        assertEquals(j.dernieretage, 0);
        
        // Vérification du score
        assertEquals(j.score, 0);
        
        // Vérification que l'inventaire n'est pas null et de sa taille
        assertNotNull(j.inventaire);
        assertEquals(j.inventaire.length, 4);  // L'inventaire doit contenir 4 objets
    }


    void testReponseValide() {
        String reponsesValid = "oui|yes|si";
        
        // Test avec une réponse correcte
        assertTrue(estReponseValide("oui", reponsesValid));  
        
        // Test avec une autre variante correcte
        assertTrue(estReponseValide("yes", reponsesValid));  
        
        // Test avec une réponse incorrecte
        assertFalse(estReponseValide("non", reponsesValid));  
    }

    void testChoisirDifficulte() {
        Question[] questionnaireFacile = creerQuestionnaire("../ressources/Question.csv", "1");
        Question[] questionnaireMoyen = creerQuestionnaire("../ressources/Question.csv", "2");
        Question[] questionnaireDifficile = creerQuestionnaire("../ressources/Question.csv", "3");

        assertTrue(questionnaireFacile.length > 0);  // Vérifier qu'il y a des questions pour la difficulté facile
        assertTrue(questionnaireMoyen.length > 0);  // Vérifier qu'il y a des questions pour la difficulté moyenne
        assertTrue(questionnaireDifficile.length > 0);  // Vérifier qu'il y a des questions pour la difficulté difficile
    }

    void testBonnesReponses() {
        Joueur j = newJoueur("TestPlayer");
        Question q = newQuestion("Quel est le capital de la France ?", "Paris", "1");

        // Réponse correcte
        String reponseJoueur = "paris";
        if (estReponseValide(reponseJoueur, q.reponse)) {
            j.score += 10;  // Ajout de points pour une bonne réponse
        }

        assertEquals(j.score, 10);  // Le score du joueur devrait être 10
    }

    void testAchatBoutique() {
        Joueur j = newJoueur("TestPlayer");
        j.score = 100;  // On donne des points au joueur

        // Achat d'un malus d'adversaire
        boutique(j);
        assertEquals(j.score, 20);  // Le score devrait être réduit de 80

        // Achat d'un étage
        boutique(j);
        assertEquals(j.dernieretage, 1);  // Le joueur devrait avoir un étage supplémentaire
        assertEquals(j.score, 0);  // Le score devrait être réduit de 20
    }








    // Méthode principale pour démarrer le jeu
    void algorithm() {
        println("Bienvenue dans Sharkitec !");
        println("1. Nouvelle Partie");
        println("2. Charger la Partie");
        println("3. Quitter");
        int choixMenu = (int)(readInt());

        Joueur[] joueurs = null;
        if (choixMenu == 1) {
            println("Entrez le nombre de joueurs : ");
            int nbJoueurs = (int)(readInt());
            joueurs = new Joueur[nbJoueurs];
            for (int i = 0; i < nbJoueurs; i++) {
                println("Entrez le pseudo du joueur " + (i + 1) + " : ");
                joueurs[i] = newJoueur(readString());
            }
        } else if (choixMenu == 2) {
            joueurs = chargerPartie();
        } else {
            println("Au revoir !");
            return;
        }

        // Charger les questionnaires pour chaque difficulté
        Question[] facile = creerQuestionnaire("../ressources/Question.csv", "1");
        Question[] moyen = creerQuestionnaire("../ressources/Question.csv", "2");
        Question[] difficile = creerQuestionnaire("../ressources/Question.csv", "3");

        // Boucle principale du jeu
        while (true) {
            for (int i = 0; i < length(joueurs); i++) {
                jouerTour(joueurs, facile, moyen, difficile);
                boutique(joueurs[i]);
            }
       }
    }

}


<?php
include("no-lang.php");
//Header block
//define("STR_HEADER_", "");
define("STR_HEADER_TITLE", "Site Internet officiel de Rhatec et TransportMontréal");

//Menu block
//define("STR_MENU_", "");
define("STR_MENU_HOME", "Accueil");
define("STR_MENU_FEATURES", "Fonctionnalités");
define("STR_MENU_SCREENSHOTS", "Images");
define("STR_MENU_CONTACT", "Nous contacter");
define("STR_MENU_FAQ", "FAQ");

//Home block
//define("STR_HOME_", "");
define("STR_HOME_FIRST", "TransportMontréal vous permet d'accéder facilement dans un mode hors-ligne à l'horaire des sociétés de transport montréalaises (c'est à dire la Société de transport de Laval, la Société de transport de Montréal,  le Réseau de transport de Longueuil, l'Agence métropolitaine de transport et les différentes CIT et MRC). Trouver facilement vos départs en effectuant une recherche ou bien en parcourant les horaires. Ajouter à vos favoris les arrêts qui vous intéressent pour une consultation rapide. Vous pouvez également ajouter une notification afin d'être notifié avant le passage de l'autobus.");
define("STR_HOME_APPLICATION_AVAILABLE", "Application maintenant disponible sur le <a href=\"http://market.android.com/details?id=net.rhatec.amtmobile\">Android Market</a>.");

//Features block
//define("STR_FEATURES_", "");
define("STR_FEATURES_Q1", "1. Les horaires enregistrés directement sur votre téléphone mobile! ");
define("STR_FEATURES_A1", "Pourquoi vous limiter au site internet des différents réseaux de transport quand vous pouvez voir les horaires directement de votre téléphone sans avoir besoin d'un réseau WiFi ou d'un abonnement de données!");
define("STR_FEATURES_Q2", "2. Les horaires de quelles sociétés de transport sont-il disponible ?");
define("STR_FEATURES_A2", "Nous sommes conscients que beaucoup de personnes vivent en banlieue, c'est pour cette raison que nous avons implémenté le système dans les plus grosses banlieues entourant la ville de Montréal. Voici une liste des sociétés supportés:");
define("STR_FEATURES_Q3", "3. Complètement bilingue.");
define("STR_FEATURES_A3", "Montréal devient de plus en plus une ville internationale, nous avons donc développé notre application en français et en anglais. Des versions pour autres langues pourront être ajoutées si la demande est faite.");
define("STR_FEATURES_Q4", "4. Arrêts favori pour plus de rapidité.");
define("STR_FEATURES_A4", "La gestion de favoris permet de rapidement trouver les horaires que vous voulez consulter souvent.");
define("STR_FEATURES_Q5", "5. Notification avant l'heure de passage de votre autobus.");
define("STR_FEATURES_A5", "Ne manquez plus votre autobus grâce à une notification de votre appareil mobile avant son heure de passage.");
define("STR_FEATURES_Q6", "6. Voir tous les horaires des autobus qui passent à votre arrêt en un clin d'oeil.");
define("STR_FEATURES_A6", "Ceci vous permettra de gagner du temps si plusieurs autobus peuvent vous mener à destination.");
define("STR_FEATURES_Q7", "7. Autobus adapté aux personnes handicapées.");
define("STR_FEATURES_A7", "Si vous êtes une personne handicapée, notre application vous permettra de savoir quels sont les autobus adaptés à votre situation (lorsque l'information est disponible).");
define("STR_FEATURES_Q8", "8. Gestion des réseaux.");
define("STR_FEATURES_A8", "Vous pouvez restreindre notre application pour l'empêcher de faire des mises à jour sur le WiFi ou le réseau cellulaire. Ceci vous permettra de ne pas avoir de surprise à la fin du mois!");


//Screenshots block
//define("STR_SCREENSHOTS_", "");
define("STR_GALLERY_HOME", "Retourner aux images");
define("STR_GALLERY_STOP_TITLE", "Liste des arrêts d'autobus");
define("STR_GALLERY_STOP_DESCRIPTION", "Ceci est une liste des différents arrêts d'un autobus");
define("STR_GALLERY_HORAIRE_TITLE", "Horaire");
define("STR_GALLERY_HORAIRE_DESCRIPTION", "Voici l'horaire d'un autobus. Il est possible de choisir d'autres autobus du même arrêt, d'autres horaires et de voir de l'information complémentaire sur les passages (par exemple, il supporte les personnes handicapées).");
define("STR_GALLERY_BUS_TITLE", "Liste des autobus");
define("STR_GALLERY_BUS_DESCRIPTION", "Ceci est une liste de tous les autobus d'un des différents services de transport.");
define("STR_GALLERY_UPDATE_TITLE", "Mise à jour");
define("STR_GALLERY_UPDATE_DESCRIPTION", "Voici une liste des différentes mises à jour, leur grosseur et leur importance.");
define("STR_GALLERY_MENU_TITLE", "Options");
define("STR_GALLERY_MENU_DESCRIPTION", "Il est possible de rajouter des favoris  et d'accéder à plusieurs autres menus via le menu des options.");
define("STR_GALLERY_TRANSPORT_TITLE", "Fournisseur de transport en commun");
define("STR_GALLERY_TRANSPORT_DESCRIPTION", "Ceci est une liste de tous les fournisseurs de transport en commun.");
define("STR_GALLERY_NOTIFICATIONTRIGGER_TITLE", "Notifications activées ");
define("STR_GALLERY_NOTIFICATIONTRIGGER_DESCRIPTION", "Voici ce qui arrive quand une notification est déclenchée (en plus d'une sonnerie et de la vibration).");
define("STR_GALLERY_NOTIFICATION_TITLE", "Gestion des notifications");
define("STR_GALLERY_NOTIFICATION_DESCRIPTION", "Pour ajouter une notification, il suffit de sélectionner une heure dans l'horaire.");
define("STR_GALLERY_PREFERENCE_TITLE", "Préférences");
define("STR_GALLERY_PREFERENCE_DESCRIPTION", "Dans le menu des préférences, il est possible de choisir quelle type de connexion le téléphone utilisera. Ainsi, le programme n'utilisera pas votre connexion cellulaire si vous n'avez pas de forfait de données");
define("STR_GALLERY_BOOKMARKS_TITLE", "Favoris");
define("STR_GALLERY_BOOKMARKS_DESCRIPTION", "Voici une liste des passages favoris avec les huit prochains passages pour chacun d'eux.");
define("STR_GALLERY_SEARCH_TITLE", "Recherche d'autobus ou d'arrêt");
define("STR_GALLERY_SEARCH_DESCRIPTION", "Il est possible de rechercher un autobus par numéro d'autobus ou par numéro d'arrêt.");
//FAQ block
//define("STR_FAQ_", "");
define("STR_FAQ_TITLE", "Foires aux questions");
define("STR_FAQ_GENERAL", "Questions d'ordre général");
define("STR_FAQ_GENERAL_Q1", "Pouvez-vous traduire votre application dans ma langue?");
define("STR_FAQ_GENERAL_A1", "Nous aimerions offrir notre application dans plus de langue, mais nous avons besoin d'aide. Si vous pouvez en faire la traduction, <a href='?page=contact'>contactez-nous</a>.");
define("STR_FAQ_GENERAL_Q2", "Est-ce que vous pouvez développer une application pour ma ville?");
define("STR_FAQ_GENERAL_A2", "D'autres villes sont planifiées, mais nous voulons commencer par terminer l'application de Montréal pour avoir une base solide pour les prochaines villes. Vous pouvez toujours nous contacter pour nous proposer des villes.");
define("STR_FAQ_GENERAL_Q3", "Est-ce que vous pouvez ajouter cette fonctionnalité à votre application?");
define("STR_FAQ_GENERAL_A3", "Envoyez-nous votre idée par la page de contact et nous l'examinerons. Gardez en tête que nous avons déjà beaucoup d'idées pour les prochaines versions et que nous ne pourrons peut-être pas le faire dans la prochaine version, mais nous allons essayer de l'inclure dans une version future.");
define("STR_FAQ_GENERAL_Q4", "Qu’elles sont les futures fonctionnalités prévues?");
define("STR_FAQ_GENERAL_A4", "- Support pour plusieurs notifications (avec une possibilité de répétition)<br />- Gestion de trajet (c'est-à-dire un ensemble de favoris) <br />- Support du temps réel (mode connecté) pour les sociétés de transport qui le supporte comme la STL <br />- Géolocalisation (votre position par rapport à un arrêt, position de l'autobus) <br />- Heure d'arrivé de l'autobus à un autre arrêt <br />- Possibilité de télécharger les mises à jour à l'avance (lorsque possible), avec déploiement automatique lorsque les horaires changent. <br />- Plus d'informations sur le train (bicyclette, terminus d'arrivé)");
define("STR_FAQ_SUPPORT", "Support");
define("STR_FAQ_SUPPORT_Q1", "Les heures de passage ne sont pas bonnes (les bases de données ne sont pas à jour)");
define("STR_FAQ_SUPPORT_A1", "Il y a plusieurs possibilités, commencez par regarder si vous avez la dernière version de la base de données à l'aide de l'option de mises à jour. Si c'est le cas, essayer de voir si l'horaire est le même que sur le site du réseau de transport. Si ce n'est pas le cas, utilisez notre page de contact pour nous signaler la discordance.");
define("STR_FAQ_SUPPORT_Q2", "Je ne peux pas ajouter plus d'une notification à la fois, est-ce normal?");
define("STR_FAQ_SUPPORT_A2", "Oui, dans la première version, nous ne supportons qu'une notification.");
define("STR_FAQ_ARTWORK", "Question sur le design et les images de votre site internet et de votre application");
define("STR_FAQ_ARTWORK_Q1", "J'aime votre logo et/ou vos cartes, qui en est l'artiste?");
define("STR_FAQ_ARTWORK_A1", "C'est un ami, il se nomme <a href='http://www.davidcerat.net' target='_top'>David Cérat</a>.");
define("STR_FAQ_ARTWORK_Q2", "Wow vous avez de beaux nuages!");
define("STR_FAQ_ARTWORK_A2", "Nous aussi nous trouvons! Il s'agit de brush créé par <a href='http://javierzhx.deviantart.com/' target='_top'>javierzhx</a>.");

//Contact block
//define("STR_CONTACT_", "");
define("STR_CONTACT_FORM", "Contactez-nous");
define("STR_CONTACT_SEND", "Envoyer");
define("STR_CONTACT_INFORMATION", "Vos informations");
define("STR_CONTACT_INFORMATION_NAME", "Nom");
define("STR_CONTACT_INFORMATION_NAME_ADD", "Votre nom complet");
define("STR_CONTACT_INFORMATION_EMAIL", "Adresse courriel");
define("STR_CONTACT_INFORMATION_ADD_EMAIL", "Votre adresse courriel valide");
define("STR_CONTACT_QUESTION", "Votre question");
define("STR_CONTACT_QUESTION_SUBJECT", "Sujet");
define("STR_CONTACT_QUESTION_SUBJECT_ADD", "Écrivez le sujet de votre question ou commentaire.");
define("STR_CONTACT_QUESTION_CATEGORIE", "Catégorie");
define("STR_CONTACT_QUESTION_CATEGORIE_CHOOSE", "Choisisez votre catégorie");
define("STR_CONTACT_QUESTION_CATEGORIE_SELL", "Ventes");
define("STR_CONTACT_QUESTION_CATEGORIE_SUPPORT", "Support");
define("STR_CONTACT_QUESTION_CATEGORIE_BADDATABASE", "Erreur de base de données");
define("STR_CONTACT_QUESTION_DESCRIPTION", "Description");
define("STR_CONTACT_QUESTION_DESCRIPTION_WRITE", "Écrivez votre question ou votre commentaire");
define("STR_CONTACT_CAPTCHA", "Captcha");
define("STR_CONTACT_CAPTCHA_HUMAN", "Êtes-vous humain?");
define("STR_CONTACT_SENT_ERROR", "Erreur dans le formulaire de contact.");
define("STR_CONTACT_SENT_CORRECTLY", "Merci, votre question ou commentaire a été envoyé.");

//Footer block
define("STR_FOOTER_COPYRIGHT", "Copyright Rhatec 2011.");
?>
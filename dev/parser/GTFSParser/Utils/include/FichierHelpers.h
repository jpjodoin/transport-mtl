///
/// \file FichierHelpers.h
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef FICHIER_H
#define FICHIER_H

#ifndef _IOSTREAM_
#include <iostream>
#endif

#ifndef _FSTREAM_
#include <fstream>
#endif

#ifndef _STRING_
#include <string>
#endif


///
/// \brief Fichier est un wrapper a boost pour le système de fichier.
///
namespace Utils
{
namespace IO
{
namespace Fichier
{
	// Ouvrir un fichier en lecture/ecriture et ecraser le contenu
	bool OuvrirEcritureLectureEcraser(std::string nom, std::string path, std::fstream& fichier);
	// Ouvrir un fichier en lecture/ecriture et ecrire a la fin
	bool OuvrirEcritureLectureFin(std::string nom, std::string path, std::fstream& fichier);

	// Ouverture d'un fichier en lecture
	bool OuvrirFichierLecture(std::string nom, std::string path, std::ifstream& fichier);

	// Conserver le contenu du fichier et ecrire a la fin
	bool OuvrirFichierEcritureFin(std::string nom, std::string path, std::ofstream& fichier);
	// Ecraser le fichier s'il n'est pas vide
	bool OuvrirFichierEcritureEcraser(std::string nom, std::string path, std::ofstream& fichier);

	int ObtenirPositionEnOctetDansFichier(std::fstream& fichier, std::string fileName, std::string stringToFind);

	int ObtenirPositionEnOctetDansFichier(std::string fileName, std::string stringToFind);




};

}
}

#endif
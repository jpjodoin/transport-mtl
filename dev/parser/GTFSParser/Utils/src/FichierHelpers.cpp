///
/// \file Fichier.cpp
/// \brief Méthode de la classe fichier
/// \version 0.1
/// \date 28 mai 2009
///

#include "FichierHelpers.h"
#include <fstream>
#include <sstream>

namespace Utils
{
namespace IO
{
namespace Fichier
{

///
/// \brief Ouvre un fichier
/// Cette fonction permet d'ouvrir un fichier en Lecture ecriture.
/// Si le fichier existe, son contenu sera vidé.
///
/// \param[in]			nom			nom du fichier a ouvrir
/// \param[in]			path		path du fichier
/// \param[in, out]		fichier		fstream du fichier
/// \return fstream
///
bool OuvrirEcritureLectureEcraser(std::string nom, std::string path, std::fstream& fichier)
{
	nom = path + nom;

	fichier.open(nom.c_str(), std::ios::in | std::ios::out |std::ios::trunc);
	//std::fstream monFichier(nom.c_str(), std::ios::in | std::ios::out |std::ios::trunc);
	return !fichier.fail();
}

///
/// \brief Ouvre et écrit à la fin d'un fichier. Cette fonction permet d'ouvrir un fichier en Lecture ecriture, et d'y ecrire a la fin.
/// \param[in]			nom				nom du fichier a ouvrir
/// \param[in]			path			path du fichier
/// \param[in, out]		fichier			ifstream
///
/// \return true si l'ouverture du fichier a reussi
///
bool OuvrirEcritureLectureFin(std::string nom, std::string path, std::fstream& fichier)
{
	nom = path + nom;
	fichier.open(nom.c_str(), std::ios::in | std::ios::out | std::ios::app);
	return !fichier.fail();
}
///
/// \brief. Cette fonction permet d'ouvrir un fichier en Lecture.
///
/// \param[in]			nom			nom du fichier a ouvrir
/// \param[in]			path		path du fichier
/// \param[in, out]		fichier		ifstream
///
/// \return				true si l'ouverture du fichier a reussi
///
bool OuvrirFichierLecture(std::string nom, std::string path, std::ifstream& fichier)
{
	nom = path + nom;
	fichier.open(nom.c_str(), std::ios::in);
	return !fichier.fail();

}
///
/// Cette fonction permet d'ouvrir un fichier en Ecriture et de se positionner a la fin.
///
/// \param[in] nom				nom du fichier a ouvrir
/// \param[in] path				path du fichier
/// \param[in, out] fichier		fstream
///
/// \return true si l'ouverture du fichier a reussi
///
bool OuvrirFichierEcritureFin(std::string nom, std::string path, std::ofstream& fichier)
{
	nom = path + nom;
	fichier.open(nom.c_str(), std::ios::out | std::ios::app);
	return !fichier.fail();
}
///
/// Cette fonction permet d'ouvrir un fichier en Ecriture et d'ecraser
/// son contenu s'il existe deja
///
/// \param[in] nom			nom du fichier a ouvrir
/// \param[in] path			path du fichier
/// \param[in, out] fichier	ofstream
///
/// \return true si l'ouverture du fichier a reussi
bool OuvrirFichierEcritureEcraser(std::string nom, std::string path, std::ofstream& fichier)
{
	nom = path + nom;
	fichier.open(nom.c_str(), std::ios::out | std::ios::trunc);
	return !fichier.fail();
}

int ObtenirPositionEnOctetDansFichier(std::fstream& fichier, std::string fileName, std::string stringToFind)
{
	int nPosInFile=0;
	fichier.flush();
	std::ifstream fListeByteCounter(fileName.c_str(), std::ios::in);
	std::stringstream buf;
	buf << fListeByteCounter;
	std::string debug(buf.str());
	nPosInFile=buf.str().find(stringToFind);
	return nPosInFile;
}


int ObtenirPositionEnOctetDansFichier(std::string fileName, std::string stringToFind)
{
	int nPosInFile=0;

	std::ifstream fListeByteCounter(fileName.c_str(), std::ios::in);
	std::ostringstream buf;
	buf << fListeByteCounter;
	nPosInFile=buf.str().find(stringToFind);
	return nPosInFile;
}



}
}
}
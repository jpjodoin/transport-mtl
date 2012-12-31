///
/// \file Horaire.h
/// \brief Déclaration de la classe Horaire
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef HORAIRE
#define HORAIRE

#include "Exports.h"

//Library
#ifndef _QUEUE_
#include <queue>
#endif

#ifndef _STRING_
#include <string>
#endif

///
/// \brief Classe contenant l'horraire à un arrêt
///
class RHATECCOREDLL_API Horaire
{

public:
	Horaire(unsigned int strNom); 
	virtual ~Horaire();
	void AjouterHeure(unsigned int nHeure);
	bool DefinirExtraInfoHoraire(const std::string& strCode);
	std::string ObtenirExtraInfoHoraire();
	unsigned int ObtenirNom();
	std::string ObtenirHeures(std::string strSeparator=";");
	void ChangerNomHoraire(unsigned int);
	//std::vector<unsigned int>* ObtenirPtrVecteurHeure() { return &m_VecteurHeure; }
	std::vector<unsigned int>& ObtenirVecteurHeure() { return m_VecteurHeure; }
	void DefinirVecteurHeure(const std::vector<unsigned int>& vH) { m_VecteurHeure = vH;}
	bool EstVide();
	void Vider();
		

protected:	
	unsigned int m_nom;
	std::string m_strExtraInfoCode;
	std::vector<unsigned int> m_VecteurHeure;
public:
	unsigned int m_start_date;
	unsigned int m_end_date;


};
///
/// \brief Retourne le nom de l'horaire
/// \return					Le string contenant le nom de l'horaire.
///
inline unsigned int Horaire::ObtenirNom()
{
	return m_nom;
}

///
/// \brief Permet de changer le nom de l'horaire
/// \param[in]		strNom		Nom de l'horaire
///
inline void Horaire::ChangerNomHoraire(unsigned int newName)
{
	m_nom=newName;
}

///
/// \brief Permet de savoir si l'horaire contient des heures
///	\return					True si l'horaire est vide, sinon false
///
inline bool Horaire::EstVide()
{
	return m_VecteurHeure.empty();

}

inline void Horaire::Vider()
{
	m_VecteurHeure.clear();
}
#endif //HORAIRE




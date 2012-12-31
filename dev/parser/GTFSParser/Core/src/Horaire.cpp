///
/// \file Horaire.cpp
/// \brief Déclaration de la classe Horaire
/// \version 0.1
/// \date 28 mai 2009
///

#include "Horaire.h"
#include "StringHelpers.h"
#include <assert.h>
#include <iostream>
///
/// \brief Contructeur de l'horraire
/// \param[in]		strNom		Nom de l'horraire.
///
Horaire::Horaire(unsigned int nom)
: m_nom(nom)
, m_strExtraInfoCode("")
{
}
///
/// \brief Destructeur d'horaire
///
Horaire::~Horaire()
{
}
///
/// \brief Ajouté une heure à l'horraire
/// \param[in]		nHeure			Horraire sous format interne.
///
void Horaire::AjouterHeure(unsigned int nHeure)
{
	m_VecteurHeure.push_back(nHeure);
}

///
/// \brief Retourne les heures
/// \param[in]		strSeparator		Separateur entre chaque heure.
/// \return					Le string contenant les heures.
///
std::string Horaire::ObtenirHeures(std::string strSeparator)
{
	std::string strHeure="";

	for(unsigned int i=0; i <m_VecteurHeure.size(); i++)
	{
		std::string strHeureUnit(Utils::String::UIntToString(m_VecteurHeure[i]));
		while(strHeureUnit.size()<4)
			strHeureUnit="0"+strHeureUnit;
		strHeure.append(strHeureUnit).append(strSeparator);
	}
	return strHeure;

}

std::string Horaire::ObtenirExtraInfoHoraire()
{
	return m_strExtraInfoCode;
}

bool Horaire::DefinirExtraInfoHoraire( const std::string& strCode )
{
	bool success = true;
	if((m_strExtraInfoCode != "") &&  (m_strExtraInfoCode != strCode))
	{	
		//std::cout << "ERROR, multiple extra info for horaire " << strCode << std::endl;
		success = false;
	}

	m_strExtraInfoCode=strCode;
	return success;
}
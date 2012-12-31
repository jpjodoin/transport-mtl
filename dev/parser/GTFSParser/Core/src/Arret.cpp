///
/// \file Arret.cpp
/// \brief Méthodes de la classe Arret.
/// \version 0.1
/// \date 28 mai 2009
///
#include "Horaire.h"
#include "Arret.h"



///
/// \brief Constructeur de l'arret.
/// \param[in]	nArret				Numéro de l'arret.
/// \param[in]	strIntersection1	Nom de la première rue.
/// \param[in]	strIntersection2	Nom de la seconde rue.
///
Arret::Arret(std::string arretId, std::string strIntersection1, std::string strIntersection2)
: m_arretId(arretId)
, m_strIntersection1(strIntersection1)
, m_strIntersection2(strIntersection2)
, m_nStationTrain(false)
, m_nStationMetro(false)
{

}

///
/// \brief Destructeur d'un arret.
///
Arret::~Arret()
{
	for (unsigned int i =0; i <m_VectorHoraire.size(); ++i)
	{
		delete m_VectorHoraire.at(i);
	}

}

///
/// \brief Ajouter un arret au vecteur d'arret.
/// \param[in]	oHoraire			L'arret à ajouter.
///
void Arret::AjouterHoraire(Horaire* oHoraire)
{
	m_VectorHoraire.push_back(oHoraire);
}

///
/// \brief Obtenir la liste l'horaire a un arret.
/// \param[in]	strNom			Le nom de l'arret.
/// \return						L'horaire de l'arret.
///
std::string Arret::ObtenirHoraire(unsigned int nom)
{
	std::string strHoraire("");
	bool bFound=false;

	std::vector<Horaire*>::iterator iter = m_VectorHoraire.begin();

	while(iter!=m_VectorHoraire.end() && !bFound)
	{
		Horaire* buf(*iter);
		if(buf->ObtenirNom() == nom)
		{
			bFound=true;
			strHoraire.append(buf->ObtenirHeures());
		}
		else
		{
			iter++;
		}
	}	
	return strHoraire;
}

void Arret::SupprimerTousLesHoraires()
{
	m_VectorHoraire.clear();
}

std::vector<Horaire*> Arret::ObtenirHoraireParNom(unsigned int nomHoraire)
{
	std::vector<Horaire*> vHoraire;
	std::vector<Horaire*>::iterator iter = m_VectorHoraire.begin();

	while(iter!=m_VectorHoraire.end())
	{
		Horaire* buf =(*iter);
		if(buf->ObtenirNom() == nomHoraire)
		{
			vHoraire.push_back(buf);
		}
		++iter;
	}	
	return vHoraire;
}

///
/// \file Circuit.cpp
/// \brief Méthodes de la classe Circuit.
/// \version 0.1
/// \date 28 mai 2009
///
#include "Circuit.h"
#include "StringHelpers.h"

///
/// \brief Constructeur de Circuit.
/// \param[in]		strNom			String contenant le nom du circuit.
/// \param[in]		nNumero			Int contenant le numéro du circuit.
/// \param[in]		Direction		enum contenant la direction du circuit.
/// \param[in]		strExtraInfo	string des extra info.
///
Circuit::Circuit(std::string strNom, unsigned int nNumero, eDirection Direction, std::string strExtraInfoDirection, std::string strExtraInfoCircuit)
: m_busName(strNom)
, m_eDirection(Direction)
, m_nNumero(nNumero)
, m_strExtraInfoDirection(strExtraInfoDirection)
, m_strExtraInfoCircuit(strExtraInfoCircuit)
{

}
///
/// \brief Destructeur de Circuit.
///
Circuit::~Circuit()
{
	for(unsigned int i=0; i< m_vArret.size(); ++i)
	{
		delete m_vArret.at(i);
	}
}

Circuit::Circuit(const Circuit& rhs) 
: m_busName(rhs.m_busName)
, m_eDirection(rhs.m_eDirection)
, m_nNumero(rhs.m_nNumero)
, m_strExtraInfoDirection(rhs.m_strExtraInfoDirection)
, m_strExtraInfoCircuit(rhs.m_strExtraInfoCircuit)
{
	
	for(unsigned int i=0; i< rhs.m_vArret.size(); ++i)
	{
		m_vArret.push_back(&(*rhs.m_vArret.at(i)));
	}


}
Circuit& Circuit::operator=(const Circuit& rhs) 
{
	if(this != &rhs)
	{
		for(unsigned int i=0; i< m_vArret.size(); ++i)
		{
			delete m_vArret.at(i);
		}
		m_vArret.clear();
		for(unsigned int i=0; i< rhs.m_vArret.size(); ++i)
		{
			m_vArret.push_back(&(*rhs.m_vArret.at(i)));
		}

		m_busName = rhs.m_busName;
		m_eDirection = rhs.m_eDirection;
		m_nNumero = rhs.m_nNumero;
		m_strExtraInfoDirection = rhs.m_strExtraInfoDirection;
		m_strExtraInfoCircuit = rhs.m_strExtraInfoCircuit;
	}
	
	return *this;
}


std::string m_strNom;
eDirection m_eDirection;
unsigned int m_nNumero;
std::string m_strExtraInfoDirection;
std::string m_strExtraInfoCircuit;
std::vector<Arret*> m_vArret;



///
/// \brief Ajouter un arret au circuit.
/// \param[in]		oArret		Le nouvelle arret.
///
void Circuit::AjouterArret(Arret* oArret)
{
	m_vArret.push_back(oArret);
}
///
/// \brief Obtenir l'horaire d'un arret..
/// \param[in]		nArret		Numéro d'arrêt demandé.
/// \param[in]		strHoraire	Les horaires en strings.
///	\return						String avec les horaires à l'arret.
///
std::string Circuit::ObtenirHoraireArret(std::string arretId , unsigned int strHoraire)// Retourne l'horaire de l'arrêt
{
	bool bFound=false;
	std::vector<Arret*>::iterator iter = m_vArret.begin();
	std::string strHoraireArret;
		
	while(iter!=m_vArret.end() && !bFound)
	{
		Arret* buf = (*iter);
		if(buf->ObtenirArretId() == arretId)
		{
			bFound=true;
			strHoraireArret = buf->ObtenirHoraire(strHoraire);
		}
		else
		{
			iter++;
		}
	}	
	return strHoraireArret;
}
///
/// \brief Obtenir les différents arrets dans une liste.
/// \param[in]		strSeparator	Ce qui séparer chacun des éléments.
///	\return							La liste des arrets pour un circuit.
///
std::string Circuit::ObtenirListeArret(std::string strSeparator)//Retourne une liste d'arrêt formaté
{
	std::string strListeArret("");
	std::vector<Arret*>::iterator iter=m_vArret.begin();
	for(; iter < m_vArret.end(); ++iter)
	{
		Arret* buf = (*iter);
		strListeArret+=buf->ObtenirArretId();
		strListeArret+=strSeparator;
		strListeArret+=buf->ObtenirNomIntersection1();
		strListeArret+=strSeparator;
		strListeArret+=buf->ObtenirNomIntersection2();
		strListeArret+=strSeparator;
		strListeArret+="\n";
	}
	return strListeArret;
}
///
/// \brief Obtenir le vecteur des arrets.
///	\return	Retourne une référence vers le vecteur d'arrêt.
///
std::vector<Arret*>* Circuit::ObtenirVecteurArret()
{
	return &m_vArret;
}


/// 
/// \brief  Méthode qui permet de voir si un fichier contient un circuit portant un nom particulier
/// \param[in]	std::string _nomIntersection1	Nom de l'intersection1
/// \param[in]	std::string _nomIntersection2	Nom de l'intersection2
/// \return	Arret*	Retourne un pointeur vers l'arrêt ou nulle si le circuit n'existe pas
///
Arret* Circuit::ObtenirArret( std::string _nomIntersection1, std::string _nomIntersection2 )
{
	Arret* arretCourant=0;
	unsigned int i=0;
	while(i < m_vArret.size() && arretCourant == 0)
	{
		if(m_vArret.at(i)->ObtenirNomIntersection1() == _nomIntersection1 && m_vArret.at(i)->ObtenirNomIntersection2() == _nomIntersection2)
		{
			arretCourant = m_vArret.at(i);
		}
		++i;
	}
	return arretCourant;
}

Arret* Circuit::ObtenirArret(std::string arretId)
{
	Arret* arretCourant = NULL;
	unsigned int i=0;
	while(i < m_vArret.size() && arretCourant == NULL)
	{
		if(m_vArret.at(i)->ObtenirArretId() == arretId)
		{
			arretCourant = m_vArret.at(i);
		}
		++i;
	}
	return arretCourant;

}

std::vector<Arret*>::iterator Circuit::ObtenirArretIterator( const std::string& arretId )
{
	std::vector<Arret*>::iterator it;
	for (it = m_vArret.begin(); it != m_vArret.end(); ++it)
	{
		if((*it)->ObtenirArretId() == arretId)
		{
			break;
		}
	}
	return it;

}

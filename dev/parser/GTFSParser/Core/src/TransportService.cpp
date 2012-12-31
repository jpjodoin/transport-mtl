///
/// \file TransportService.cpp
/// \brief Méthodes de la classe TransportService.
/// \version 0.1
/// \date 28 mai 2009
///
#include "TransportService.h"
#include "StringHelpers.h"
#include "DirectionHelpers.h"

/// 
/// \brief Constructeur d'un service de transport.
/// \param[in]		strName			Nom long du service de transport.
/// \param[in]		strShortName	Nom court du service de transport.
/// 
TransportService::TransportService(Information _info) 
: m_info(_info)
{

}
/// 
/// \brief Deconstructeur d'un service de transport.
///
TransportService::~TransportService()
{	
	for(unsigned int i=0; i<m_vCircuit.size(); i++)
		delete m_vCircuit[i];
}
/// 
/// \brief Ajouter un circuit à la liste des circuits.
/// \param[in]		oCircuit à ajouté.
///
void TransportService::AjouterCircuit(Circuit* oCircuit)
{
	m_vCircuit.push_back(oCircuit);
}
/// 
/// \brief Obtenir la liste des circuits.
/// \param[in]		strSeparator		le séparateur entre chaque circuit.
/// \return								String contenant la liste des circuit séparer par \b strSeparator.
///
std::string TransportService::ObtenirListeCircuit(std::string strSeparator)
{
	std::string strListeCircuit("");
	std::vector<Circuit*>::iterator iter = m_vCircuit.begin();
	for(; iter < m_vCircuit.end(); ++iter)
	{
		Circuit* buf = *iter;
		strListeCircuit += Utils::String::UIntToString(buf->ObtenirNumero());
		strListeCircuit += strSeparator;
		strListeCircuit += buf->ObtenirExtraInfoCircuit();
		strListeCircuit += strSeparator;
		strListeCircuit += Utils::String::DirectionToChar(buf->ObtenirDirection());
		strListeCircuit += strSeparator;
		strListeCircuit += buf->ObtenirNom();
		strListeCircuit += strSeparator;
		strListeCircuit += buf->ObtenirExtraInfoDirection();
		strListeCircuit += strSeparator;
		strListeCircuit += buf->ObtenirCouleur();
		strListeCircuit += strSeparator;
		strListeCircuit += "\n";
	}
	return strListeCircuit;
}



/// 
/// \brief Méthode qui permet de voir si un fichier contient un circuit portant un nom particulier. 
/// \param[in]	std::string _nameOfCircuit	Nom du circuit
/// \return	Circuit* Retourne le circuit ou 0 si le circuit n'existe pas
///
Circuit* TransportService::ObtenirCircuit( std::string _nameOfCircuit )
{
	Circuit* circuitCourant=0;
	unsigned int i=0;
	while(i<m_vCircuit.size() && circuitCourant==0)
	{
		if(m_vCircuit.at(i)->ObtenirNom()==_nameOfCircuit)
		{
			circuitCourant=m_vCircuit.at(i);
		}
		i++;
	}
	return circuitCourant;
}

Circuit* TransportService::ObtenirCircuit(unsigned int numero, eDirection direction)
{
	Circuit* circuitCourant = NULL;
	unsigned int i=0;
	while(i<m_vCircuit.size() && circuitCourant==0)
	{
		if(m_vCircuit.at(i)->ObtenirNumero() ==numero && m_vCircuit.at(i)->ObtenirDirection() == direction)
		{
			circuitCourant=m_vCircuit.at(i);
		}
		++i;
	}
	return circuitCourant;
}



/// 
/// \brief  Méthode qui permet de voir si un service de transport contient un circuit portant un nom particulier et un extra info particulier
/// \param[in]	std::string _nameOfCircuit	Nom du circuit 
/// \param[in]	std::string _nameOfExtrainfo	Extra info direction
/// \return	Circuit* Retourne le circuit ou 0 si le circuit n'existe pas
///
Circuit* TransportService::ObtenirCircuit( std::string _nameOfCircuit, std::string _nameOfExtrainfoDirection/*, std::string _nameOfExtrainfoCircuit*/ )
{
	Circuit* circuitCourant=0;
	unsigned int i=0;
	while(i<m_vCircuit.size() && circuitCourant==0)
	{
		if(m_vCircuit.at(i)->ObtenirNom()==_nameOfCircuit && m_vCircuit.at(i)->ObtenirExtraInfoDirection()==_nameOfExtrainfoDirection /*&&  m_vCircuit.at(i)->ObtenirExtraInfoCircuit() ==  _nameOfExtrainfoCircuit*/)
		{
			circuitCourant=m_vCircuit.at(i);
		}
		i++;
	}
	return circuitCourant;
}


Circuit* TransportService::ObtenirCircuit(unsigned int numero, std::string _nameOfExtrainfoDirection)
{
	Circuit* circuitCourant=0;
	unsigned int i=0;
	while(i<m_vCircuit.size() && circuitCourant==0)
	{
		Circuit* c = m_vCircuit.at(i);
		if(c->ObtenirNumero() == numero && c->ObtenirExtraInfoDirection() == _nameOfExtrainfoDirection)
		{
			circuitCourant=m_vCircuit.at(i);
		}
		++i;
	}
	return circuitCourant;
}
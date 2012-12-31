///
/// \file TransportService.h
/// \brief Déclaration de la classe TransportService
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef TRANSPORT_SERVICE
#define TRANSPORT_SERVICE
#include "Exports.h"
#include <vector>
#include "Circuit.h"
#include <string>
//#include "HttpDatabaseCreator.h"
#include "Information.h"

///
/// \brief Un service de transport qui contient la liste des circuits.
///
class RHATECCOREDLL_API TransportService
{
public:
	TransportService(Information info);
	virtual ~TransportService();
	std::string ObtenirNom();
	std::string ObtenirNomCourt();
	const Information& ObtenirInfo() const {return m_info;}
	void DefinirInfo(const Information& info) { m_info = info; }
	const bool AfficherExtraInfo() const;
	virtual void AjouterCircuit(Circuit* oCircuit);
	Circuit* ObtenirCircuit(unsigned int numero, eDirection direction);
	Circuit* ObtenirCircuit(std::string _nameOfCircuit);
	Circuit* ObtenirCircuit(std::string _nameOfCircuit, std::string _nameOfExtrainfoDirection);
	Circuit* ObtenirCircuit(unsigned int numero, std::string _nameOfExtrainfoDirection);
	TransportTypeAffichage ObtenirType(){return m_info.m_type;}
	std::string ObtenirListeCircuit(std::string strSeparator=";");
	std::vector<Circuit*>* ObtenirVecteurCircuit();

	std::vector<std::pair<unsigned int, unsigned int>> m_intervallesHoraires;// Rajouter pour supporter le fait que des données GTFS peuvent couvrir plusieurs périodes de validité

private:
	Information m_info;
	std::vector<Circuit*> m_vCircuit;

};
/// 
/// \brief Obtenir la liste des circuits.
/// \return				Le pointeur vers le vecteur de la liste des circuits.
/// 
inline std::vector<Circuit*>* TransportService::ObtenirVecteurCircuit()
{
	return &m_vCircuit;
}


/// 
/// \brief Obtenir le nom du service de transport.
/// \return				Le nom en string du service de transport.
/// 
inline std::string TransportService::ObtenirNom()
{
	return m_info.m_LongName;
}
/// 
/// \brief Obtenir le nom du service de transport.
/// \return				Le nom court en string du service de transport.
/// 
inline std::string TransportService::ObtenirNomCourt()
{
	return m_info.m_ShortName;
}
/// 
/// \brief  Obtenir si on doit oui ou non afficher l'extra info dans la liste de circuit
/// \return	Si on doit afficher l'extra info dans la liste de circuit			
/// 
inline const bool TransportService::AfficherExtraInfo() const
{
	return m_info.m_AffichageExtraInfo;
}
#endif //TRANSPORT_SERVICE




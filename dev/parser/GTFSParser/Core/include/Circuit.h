///
/// \file Circuit.cpp
/// \brief Déclaration de la classe Circuit.
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef CIRCUIT
#define CIRCUIT

#include "Exports.h"

#include <vector>
#include "TransportStruct.h"
#include "Arret.h"

#include <string>





///
/// \brief Conteneur pour un circuit d'un service de transport. Il contient la liste de tout les horraires du circuit.
///
class RHATECCOREDLL_API Circuit
{

public:
	Circuit(std::string strNom, unsigned int nNumero, eDirection Direction, std::string strExtraInfoDirection = "null", std::string strExtraInfoCircuit = "null");
	Circuit(const Circuit& obj);
	Circuit& operator=(const Circuit& rhs);

	const std::string& ObtenirNom();
	const eDirection ObtenirDirection() const;
	void ChangerDirection(eDirection Direction);

	//std::string ObtenirNomDirection();
	//void ChangerNomDirection(std::string strNomDirection);
	
	const unsigned int ObtenirNumero() const; 
	std::string ObtenirExtraInfoDirection();
	std::string ObtenirExtraInfoCircuit();
	void ChangerExtraInfoDirection(const std::string& strExtraInfo);
	void ChangerExtraInfoCircuit(const std::string& strExtraInfo);
	void ChangerNom(const std::string& nom);
	void AjouterArret(Arret* oArret);
	std::string ObtenirHoraireArret(std::string arretId, unsigned int strHoraire);// Retourne l'horaire de l'arrêt
	std::string ObtenirListeArret(std::string strSeparator=";");//Retourne une liste d'arrêt formaté
	Arret* ObtenirArret( std::string _nomIntersection1, std::string _nomIntersection2);
	Arret* ObtenirArret(std::string arretId);
	std::vector<Arret*>::iterator ObtenirArretIterator(const std::string& arretId);
	void DefinirCouleur(const std::string& couleur) { m_couleurCircuit=couleur; }
	std::string ObtenirCouleur() { return m_couleurCircuit; }
	std::vector<Arret*>* ObtenirVecteurArret(); //Retourne un vecteur d'arrêt
	virtual ~Circuit();	

private:
	std::string m_busName;
	eDirection m_eDirection;
	unsigned int m_nNumero;
	std::string m_strExtraInfoDirection;
	std::string m_strExtraInfoCircuit;
	std::string m_couleurCircuit;
protected:
	std::vector<Arret*> m_vArret;

};

///
/// \brief Obtenir le nom du circuit.
///	\return						String qui contient le nom du circuit.
///
inline const std::string& Circuit::ObtenirNom()
{
	return m_busName;
}
///
/// \brief Obtenir la direction du circuit.
///	\return						La eDirection du circuit.
///
inline const eDirection Circuit::ObtenirDirection() const
{
	return m_eDirection;
}
///
/// \brief Obtenir le numéro du circuit.
///	\return						Numéro du circuit.
///
inline const unsigned int Circuit::ObtenirNumero() const
{
	return m_nNumero;
}
///
/// \brief Obtenir l'info d'extra de direction
///	\return						L'information d'extra sur la direction du circuit
///
inline std::string Circuit::ObtenirExtraInfoDirection()
{
	return m_strExtraInfoDirection;
}

///
/// \brief Obtenir l'info d'extra du circuit
///	\return						L'information d'extra sur le circuit.
///
inline std::string Circuit::ObtenirExtraInfoCircuit()
{
	return m_strExtraInfoCircuit;
}


///
/// \brief Changer l'info d'extra.
/// \param[in]		strExtraInfo			String contenant l'extra info.
///
inline void Circuit::ChangerExtraInfoDirection(const std::string& strExtraInfo)
{
	m_strExtraInfoDirection=strExtraInfo;
}

inline void Circuit::ChangerExtraInfoCircuit(const std::string& strExtraInfo)
{
	m_strExtraInfoCircuit=strExtraInfo;
}

inline void Circuit::ChangerNom(const std::string& nom)
{
	m_busName = nom;
}
///
/// \brief Changer la direction.
/// \param[in]		Direction			Nouvelle direction.
///
inline void Circuit::ChangerDirection(eDirection Direction)
{
	m_eDirection=Direction;
}


#endif //CIRCUIT




///
/// \file Arret.h
/// \brief Déclaration de la classe Arret.
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef ARRET_H
#define ARRET_H

#include "Exports.h"

//Librairies
#ifndef HORAIRE
#include "Horaire.h"
#endif

#include "StringHelpers.h"

//STL
#include <vector>
#include <string>

///
/// \brief Classe qui contient les arrets.
/// 
class RHATECCOREDLL_API Arret
{

public:
	Arret(std::string arretId, std::string strIntersection1, std::string strIntersection2);
	void AjouterHoraire(Horaire* oHoraire);
	void SupprimerTousLesHoraires();
	std::string ObtenirNomIntersection1();
	std::string ObtenirNomIntersection2();
	
	unsigned int ObtenirNombreOctet();
	void DefinirNombreOctet();

	const std::string&  ObtenirArretId() const;
	std::string ObtenirHoraire(unsigned int strNom); //à remplacer par type
	
	std::vector<Horaire*> ObtenirHoraireParNom(unsigned int nomHoraire);



	std::vector<Horaire*>* ObtenirVecteurHoraire();
	std::string ObtenirStationTrainEtMetro();

	std::string GetStationTrain() const;
	void SetStationTrain(bool val);
	std::string GetStationMetro() const;
	void SetStationMetro(bool val);
	virtual ~Arret();	
	void SetLatitudeAndLongitude(const std::string& lat, const std::string& lon)
	{
		double latd, lond;
		if(Utils::String::StringToType(lat,latd) && Utils::String::StringToType(lon,lond))
		{
			int lati = latd*1E6;
			int lonti = lond*1E6;
			m_latitude = Utils::String::IntToString(lati);
			m_longitude = Utils::String::IntToString(lonti);	
		}
		
	}

	std::string GetLat();
	std::string GetLong();

protected:
	std::vector<Horaire*> m_VectorHoraire;
private:
	std::string m_strIntersection1;
	std::string m_strIntersection2;
	std::string m_arretId;
	
	bool m_nStationTrain;
	bool m_nStationMetro;
	std::string m_latitude; //Not used yet
	std::string m_longitude; //Not used yet

};


///
/// \brief Obtenir le nom de la première rue.
///	\return					Retourne le nom de la première rue.
///
inline std::string Arret::ObtenirNomIntersection1()
{
	return m_strIntersection1;
}
///
/// \brief Obtenir le nom de la seconde rue..
///	\return					Retourne le nom de la seconde rue..
///
inline std::string Arret::ObtenirNomIntersection2()
{
	return m_strIntersection2;
}
///
/// \brief Obtenir le numéro de l'arret.
///	\return					Retourne le numéro de l'arret.
///
inline const std::string& Arret::ObtenirArretId() const
{
	return m_arretId;
}
///
/// \brief Obtenir le vecteur des arrets.
///	\return					Retourne un pointeur vers le vecteur des arrets.
///
inline std::vector<Horaire*>* Arret::ObtenirVecteurHoraire()
{
	return &m_VectorHoraire;
}

inline std::string Arret::ObtenirStationTrainEtMetro()
{
	int number=0;
	if(m_nStationTrain)
		number++;
	if(m_nStationMetro)
		number+=2;
	return Utils::String::IntToString(number);
}

inline std::string Arret::GetStationTrain() const
{
	std::string strReturn("0");
	if(m_nStationTrain)
		strReturn="1";

	return strReturn;
}

inline std::string Arret::GetLat()
{
	return m_latitude;
}

inline std::string Arret::GetLong()
{
	return m_longitude;
}

inline void Arret::SetStationTrain( bool val )
{
	m_nStationTrain = val;
}

inline std::string Arret::GetStationMetro() const
{
	std::string strReturn("0");
	if(m_nStationMetro)
		strReturn="1";

	return strReturn;
}

inline void Arret::SetStationMetro( bool val )
{
	m_nStationMetro = val;
}
#endif //ARRET




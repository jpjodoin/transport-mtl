#include "DatabaseCreator.h"
#include "ExtraInfoHelpers.h"
#include "TransportService.h"
#include "Circuit.h"
#include "Horaire.h"
#include <string>
#include <iostream>
#include "StringHelpers.h"
#include <fstream>
#include "DateHelpers.h"
#include "FichierHelpers.h"
#include "boost/filesystem.hpp"   // includes all needed Boost.Filesystem declarations
#include <map>
#include "HoraireHelpers.h"


#include "Logger.h"

DatabaseCreator::DatabaseCreator(Information transitInformation)
: m_eState(PARSING_STATE_UNKNOWN)
{
	m_ExtraInfoCircuitHelpers = new ExtraInfoHelpers(100);
	m_ExtraInfoDirectionHelpers = new ExtraInfoHelpers(100);
	m_ExtraInfoHoraireHelpers = new ExtraInfoHelpers(0);
	m_TransportService = new TransportService(transitInformation);
}

DatabaseCreator::~DatabaseCreator()
{
	delete m_ExtraInfoCircuitHelpers;
	delete m_ExtraInfoDirectionHelpers;
	delete m_ExtraInfoHoraireHelpers;
	delete m_TransportService;
}

void DatabaseCreator::Initialize()
{
	LOGINFO("Loading extra info direction");
	m_ExtraInfoDirectionHelpers->LoadExtraInfoList("extrainfodb/"+m_TransportService->ObtenirNomCourt()+"extrainfocode.dba");
}



void DatabaseCreator::SaveDatabase()
{
	//todo: Ajouter log de perf (temps d'exécution des sections) http://www.cplusplus.com/reference/clibrary/ctime/
	///////////////////////////Initialisation///////////////////////////
	//Déclaration des constantes
	std::string TransportServicePath("transportdb/");

	//Création des répertoires
	boost::filesystem::create_directory(TransportServicePath);


	//Création du module de conversion entre les jours fériés et les codes
	Utils::HoraireHelpers horaireStringToCode("db/fetedb.txt");


	///////////////////////////Prétraitement///////////////////////////
	//On Classe le vecteur par # d'autobus
	LOGDEBUG("Sorting bus vector by number...");
	std::sort(m_TransportService->ObtenirVecteurCircuit()->begin(), m_TransportService->ObtenirVecteurCircuit()->end(), AscendingCircuitSort());
	LOGDEBUG("Sorting bus vector by number done");

	//Vérification des données
	LOGDEBUG("Verification des donnees en cours...");
	//Supprimer les autobus sans horaire !!! (et logger l'incident pour valider)
	std::vector<Circuit*>* vCircuit=m_TransportService->ObtenirVecteurCircuit();
	std::vector<Circuit*>::iterator iter_circuit=vCircuit->begin();
	std::vector<Arret*>* vArret;
	std::vector<Arret*>::iterator iter_arret;
	std::vector<Horaire*>* vHoraire;
	std::vector<Horaire*>::iterator iter_horaire;


	for(; iter_circuit != vCircuit->end(); ++iter_circuit)
	{
		Circuit* currentCircuit=(*iter_circuit);
		vArret = currentCircuit->ObtenirVecteurArret();

		//On ne veut pas de extra info "" car ça fuck les splits
		if(currentCircuit->ObtenirExtraInfoDirection() == "")
			currentCircuit->ChangerExtraInfoDirection("null");

		if(currentCircuit->ObtenirExtraInfoCircuit() == "")
			currentCircuit->ChangerExtraInfoCircuit("null");



		if(vArret->empty() || currentCircuit->ObtenirDirection() == UNKNOWN)
		{		
			LOGWARNING("Suppression du circuit: " << currentCircuit->ObtenirNumero() << " " << Utils::String::DirectionToChar(currentCircuit->ObtenirDirection()) << " car il n'y a pas d'arrêt !\n");
	
			iter_circuit = vCircuit->erase(iter_circuit);
			if(iter_circuit != vCircuit->begin())
				iter_circuit--;
		}	
	}

	LOGDEBUG("Verification des donnees termine!");
	
	for (unsigned int vIter = 0; vIter < m_TransportService->m_intervallesHoraires.size(); ++vIter)
	{
		const std::pair<unsigned int, unsigned int>& validityIntervalle = m_TransportService->m_intervallesHoraires.at(vIter);
		unsigned int startDate = validityIntervalle.first;
		unsigned int endDate = validityIntervalle.second;
		std::stringstream ss;
		ss << TransportServicePath+"/"+m_TransportService->ObtenirNomCourt() << "-" << startDate << "-" << endDate;
		std::string transportFolderPath(ss.str());
		boost::filesystem::create_directory(transportFolderPath);
		boost::filesystem::create_directory(transportFolderPath+"/listearret/");
		boost::filesystem::create_directory(transportFolderPath+"/listecircuit/");
		horaireStringToCode.ResetUsedHolidayMap();

		//Création de la map d'horaire par arrêt
		LOGDEBUG("Création de la map des horaires de chaque arrêt en cours...  !");
		std::map<std::string, std::string> arretMapNoPos;	///> Map contenant la correspondance # d'arrêt et les horaires
		std::map<std::string, std::string>::iterator arretMapNoPosIter; ///> Iterateur de la map contenant la correspondance # d'arrêt et les horaires
		std::pair<std::map<std::string, std::string>::iterator,bool> arretMapNoPosPair; ///> Pair contenant la correspondance # d'arrêt et les horaires
		std::map<std::string, std::string> arretMapNoText;	///> Map contenant la correspondance # d'arrêt et les horaires
		std::map<std::string, std::string>::iterator arretMapNoTextIter; ///> Iterateur de la map contenant la correspondance # d'arrêt et les horaires
		std::pair<std::map<std::string, std::string>::iterator,bool> ret; ///> Pair contenant la correspondance # d'arrêt et les horaires
		for(iter_circuit = vCircuit->begin(); iter_circuit < vCircuit->end(); ++iter_circuit)
		{
			Circuit* iCircuit = (*iter_circuit);
			vArret = iCircuit->ObtenirVecteurArret();
			iter_arret = vArret->begin();
			for(; iter_arret < vArret->end(); ++iter_arret)
			{
				Arret* iArret = (*iter_arret);
				arretMapNoTextIter = arretMapNoText.find(iArret->ObtenirArretId());
				if(arretMapNoTextIter == arretMapNoText.end()) //Si l'arrêt n'existe pas, on rajoute les informations à propos de l'arrêt
				{
					//Ce if rajouter la partie # d'arrêt, intersection etc :
					std::string strDescription("a;"+iArret->ObtenirArretId()+";"+iArret->ObtenirNomIntersection1()+";"+iArret->ObtenirNomIntersection2()+";"+iArret->GetLat()+";"+iArret->GetLong()+";"+iArret->ObtenirStationTrainEtMetro()+";\n");
					ret=arretMapNoText.insert(std::make_pair(iArret->ObtenirArretId(), strDescription));
					if(!ret.second)
						assert(false);
					arretMapNoTextIter=ret.first;
				}

				vHoraire=iArret->ObtenirVecteurHoraire();

				//We verify that the date is in our db and we create the entry for the output database
				iter_horaire=vHoraire->begin();
				for(; iter_horaire < vHoraire->end(); ++iter_horaire)
				{
					if(((*iter_horaire)->m_start_date>= startDate && (*iter_horaire)->m_end_date <=endDate)) //Is Part of the Current Validity
						horaireStringToCode.VerifyDateExist((*iter_horaire)->ObtenirNom());
				}

				//On sort le vecteur d'horaire pour avoir semaine, samedi, dimanche, jour fériés dans l'ordre
				std::sort(vHoraire->begin(), vHoraire->end(), AscendingHoraireDateSort());
				iter_horaire=vHoraire->begin();
				for(; iter_horaire < vHoraire->end(); ++iter_horaire)
				{
					if(((*iter_horaire)->m_start_date>= startDate && (*iter_horaire)->m_end_date <=endDate)) //Is Part of the Current Validity
					{
						//Cette boucle rajouter les horaires correspondants
						Horaire* iHoraire=(*iter_horaire);
						(*arretMapNoTextIter).second=(*arretMapNoTextIter).second
							+Utils::String::UIntToString(iCircuit->ObtenirNumero())+";"
							+ iCircuit->ObtenirExtraInfoCircuit()+";"
							+Utils::String::DirectionToChar(iCircuit->ObtenirDirection())+";"
							+iCircuit->ObtenirExtraInfoDirection()+";"
							+Utils::String::UIntToString(iHoraire->ObtenirNom())+";"
							+iHoraire->ObtenirExtraInfoHoraire()+";"
							+iHoraire->ObtenirHeures(";")+"\n";
					}
				}
			}
		}
		LOGDEBUG("Création de la map des horaires de chaque arrêt en cours... Terminé !");


		///////////////////////////Enregistrement des fichiers///////////////////////////
		LOGDEBUG("Enregistrement de la base de donnees en cours...");
		//Sauvegarde du fichier d'horaire
		std::fstream fHoraireFeries;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser(std::string("feries.dba"), std::string(transportFolderPath+"/"),fHoraireFeries);
		fHoraireFeries<<horaireStringToCode.GetHoraireNameToCodeList();
		fHoraireFeries.close();

		//Sauvegarde de la liste des circuits
		//File Name:ListeCircuit.dba
		LOGDEBUG("Sauvegarde de la liste des circuits (listecircuit.dba)");
		std::fstream fListeCircuit;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser(std::string("listecircuit.dba"), std::string(transportFolderPath+"/"),fListeCircuit);
		fListeCircuit<<m_TransportService->ObtenirListeCircuit(";");
		fListeCircuit.close();
		LOGDEBUG("Sauvegarde de la liste des circuits (listecircuit.dba) termine !");

		//Sauvegarde des informations
		//File Name:info.dba
		LOGDEBUG("Sauvegarde des infos du circuit(info.dba)");
		std::fstream fInfoStream;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser(std::string("info.dba"), std::string(transportFolderPath+"/"),fInfoStream);
		fInfoStream<</*Utils::Date::ObtenirNumeroVersion()*/startDate<<";"<<m_TransportService->ObtenirInfo().serialize();
		fInfoStream.close();
		LOGDEBUG("Fin Sauvegarde des infos du circuit(info.dba)");

		//Sauvegarde de la liste des fichiers d'horaire
		//File Name:50000.dba etc.
		LOGDEBUG("Sauvegarde des fichiers d'horaire en cours..... (50000.dba etc)!");
		//Sauvegarder les fichiers d'arrêts ici (changer le modulo pour 500 à voir selon les performances ?)
		std::fstream fListeArret;
		std::string strPosInFile="";
		Utils::IO::Fichier::OuvrirEcritureLectureFin("listearret.dba",transportFolderPath+"/",fListeArret);
		for(arretMapNoTextIter = arretMapNoText.begin(); arretMapNoTextIter != arretMapNoText.end(); ++arretMapNoTextIter)
		{
			std::fstream fListeHoraire;
			Utils::IO::Fichier::OuvrirEcritureLectureFin(/*Utils::String::UIntToString((*arretMapNoTextIter).first-((*arretMapNoTextIter).first%1000))+*/"horaire.dba",transportFolderPath+"/listearret/",fListeHoraire);
			fListeHoraire.seekp(0, std::ios::end);	//On déplace le pointeur de fichier à la fin
			strPosInFile=Utils::String::IntToString(fListeHoraire.tellp());	//À quelle position somme nous ?
			fListeHoraire<<(*arretMapNoTextIter).second;		
			arretMapNoPos.insert(std::make_pair((*arretMapNoTextIter).first, strPosInFile)); //Ajout à la map d'arrêt/position dans fichier

			fListeArret<<Utils::String::GetBetween((*arretMapNoTextIter).second, "a;", "\n")+strPosInFile+";\n";

		}
		fListeArret.close();
		LOGDEBUG("Sauvegarde des fichiers d'horaire en cours..... Termine!");

		//Sauvegarde de la liste des circuits
		//File Name:20E.dba etc.
		LOGDEBUG("Sauvegarde de la liste des circuits en cours... (20E.dba etc)");
		iter_circuit=vCircuit->begin();
		for(; iter_circuit < vCircuit->end(); ++iter_circuit)
		{
			std::fstream fListeCircuit;
			Circuit* iCircuit= (*iter_circuit);

			std::string strNumber(Utils::String::UIntToString(iCircuit->ObtenirNumero()));
			Utils::IO::Fichier::OuvrirEcritureLectureEcraser(strNumber+iCircuit->ObtenirExtraInfoCircuit()+Utils::String::DirectionToChar(iCircuit->ObtenirDirection())+iCircuit->ObtenirExtraInfoDirection()+".dba", transportFolderPath+"/listecircuit/", fListeCircuit);

			vArret=iCircuit->ObtenirVecteurArret();
			iter_arret=vArret->begin();
			for(; iter_arret < vArret->end(); ++iter_arret)
			{
				Arret* iArret= (*iter_arret);
				arretMapNoPosIter=arretMapNoPos.find(iArret->ObtenirArretId());
				if(arretMapNoPosIter!=arretMapNoPos.end())
				{
					strPosInFile=arretMapNoPosIter->second;
				}
				else
				{
					LOGERROR("Erreur weird ! Ne devrais pas arriver");
					strPosInFile="";
					assert(false);
				}
				fListeCircuit<<iArret->ObtenirArretId()<<";"<<iArret->ObtenirNomIntersection1()<<";"<<iArret->ObtenirNomIntersection2()<<";"<<iArret->ObtenirStationTrainEtMetro()<<";"<<iArret->GetLat() << ";" << iArret->GetLong()<< ";" <<strPosInFile<<";\n";
				//fListeCircuit<<Utils::String::UIntToString(iArret->ObtenirNumeroArret())<<";"<<iArret->ObtenirNomIntersection1()<<";"<<iArret->ObtenirNomIntersection2()<<";"<<iArret->ObtenirStationTrainEtMetro()<<";"<<strPosInFile<<";\n";
			}
			fListeCircuit.close();
		}
		LOGDEBUG("Sauvegarde de la liste des circuits en cours... Termine !");

		//Sauvegarde du fichier extra info
		//File Name:amtlisteextrainfodirection.dba
		LOGDEBUG("Enregistrement du fichier extra info direction en cours... ");
		std::fstream fExtraInfo;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser("listeextrainfodirection.dba", transportFolderPath+"/",fExtraInfo);
		fExtraInfo<<m_ExtraInfoDirectionHelpers->GetExtraInfoList(false);
		fExtraInfo.close();
		LOGDEBUG("Enregistrement du fichier extra info direction en cours... Termine !");

		//Sauvegarde du fichier extra info
		//File Name:amtlisteextrainfodirection.dba
		LOGDEBUG("Enregistrement du fichier extra info circuit en cours... ");
		std::fstream fExtraInfoCircuit;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser("listeextrainfocircuit.dba", transportFolderPath+"/",fExtraInfoCircuit);
		fExtraInfoCircuit<<m_ExtraInfoCircuitHelpers->GetExtraInfoList(false);
		fExtraInfoCircuit.close();
		LOGDEBUG("Enregistrement du fichier extra info circuit en cours... Termine !");


		//Sauvegarde du fichier extra info d'horaire
		//File Name:amtlisteextrainfohoraire.dba
		std::fstream fExtraInfoHoraire;
		Utils::IO::Fichier::OuvrirEcritureLectureEcraser("listeextrainfohoraire.dba", transportFolderPath+"/",fExtraInfoHoraire);
		fExtraInfoHoraire<<m_ExtraInfoHoraireHelpers->GetExtraInfoList(false);
		fExtraInfoHoraire.close();
		LOGDEBUG("Enregistrement du fichier extra info d'horaire en cours... Termine !");
	}

	std::fstream fExtraInfoPersistanceDb;
	Utils::IO::Fichier::OuvrirEcritureLectureEcraser(m_TransportService->ObtenirNomCourt()+"extrainfocode.dba", "extrainfodb/",fExtraInfoPersistanceDb);
	fExtraInfoPersistanceDb<<m_ExtraInfoDirectionHelpers->GetExtraInfoList(true);
	fExtraInfoPersistanceDb.close();




}
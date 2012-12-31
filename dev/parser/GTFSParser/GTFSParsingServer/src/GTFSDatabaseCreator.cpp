#include "GTFSDatabaseCreator.h"
#include "TransportService.h"
#include "TransportStruct.h"
#include "StringHelpers.h"
#include "CircuitGTFS.h"
#include "Arret.h"
#include "Horaire.h"
#include "TimeHelpers.h"
#include <iostream>
#include "ExtraInfoHelpers.h"
#include "Logger.h"
#include "HoraireHelpers.h"
#include "ArretGTFS.h"
#include "HoraireGTFS.h"

/*DatabaseCreator* getNewDatabaseCreator(std::map<std::string, std::string> configMap) 
{	
int type;
bool affExtraInfo, affRecherche, affCarte, affExtraInfoCircuit;
bool success = Utils::String::StringToType(configMap["TYPE"], type);
success &= Utils::String::StringToType(configMap["AFFICHAGE_EXTRAINFO"], affExtraInfo);
success &= Utils::String::StringToType(configMap["AFFICHAGE_RECHERCHE"], affRecherche);
success &= Utils::String::StringToType(configMap["AFFICHAGE_CARTE"], affCarte);
success &= Utils::String::StringToType(configMap["AFFICHAGE_EXTRAINFOCIRCUIT"], affExtraInfoCircuit);



return success ? new GTFSDatabaseCreator(Information(configMap["SHORT"],configMap["LONG"],
(TransportTypeAffichage)type, affExtraInfo, affRecherche, affCarte, 
affExtraInfoCircuit), configMap["EMPLACEMENT_FICHIER"]) : 0;

}*/



GTFSDatabaseCreator::GTFSDatabaseCreator(Information info, const std::string& folderName)
	: FileDatabaseCreator(info, folderName)
	, m_parsingState(NONE)
{

}


GTFSDatabaseCreator::~GTFSDatabaseCreator()
{

}

void GTFSDatabaseCreator::GetData()
{
	m_parsingState = TRANSITINFORMATION;
	ReadFile("agency.txt");

	//Spécialisation Post Processing
	/*if(m_TransportService->ObtenirNomCourt() == "stm")
	{
		m_parsingState = FREQUENCE;
		ReadFile("frequencies.txt");
	}*/

	m_parsingState = JOURFERIES;
	ReadFile("calendar_dates.txt");

	m_parsingState = TYPEHORAIRE;
	ReadFile("calendar.txt");

	m_parsingState = LISTEARRET;
	ReadFile("stops.txt");

	m_parsingState = LISTECIRCUIT;
	ReadFile("routes.txt");

	m_parsingState = LISTETRIPS;
	ReadFile("trips.txt");

	m_parsingState = LISTEHORAIRE;
	ReadFile("stop_times.txt");
	//TODO: Sort schedule

	std::vector<Circuit*>* vCircuit = m_TransportService->ObtenirVecteurCircuit();
	LOGINFO("Sorting schedule");
	unsigned int i = 0;
	for (std::vector<Circuit*>::iterator circuitIt = vCircuit->begin(); circuitIt != vCircuit->end(); ++circuitIt)
	{
		std::cout << i++ << std::endl;
		Circuit* circuitCourant = (*circuitIt);
		std::vector<Arret*>* vArret = circuitCourant->ObtenirVecteurArret();
		std::string nomDernierArret;
		for (std::vector<Arret*>::iterator arretIt = vArret->begin(); arretIt != vArret->end(); ++arretIt)
		{
			Arret* arretCourant = (*arretIt);
			std::vector<Horaire*>* vHoraire = arretCourant->ObtenirVecteurHoraire();
			for (std::vector<Horaire*>::iterator horaireIt = vHoraire->begin(); horaireIt != vHoraire->end(); ++horaireIt)
			{
				std::vector<unsigned int>& vHeure = (*horaireIt)->ObtenirVecteurHeure();
				std::sort(vHeure.begin(), vHeure.end(), AscendingHoraireTimeSort());
				for (unsigned t=0; t<vHeure.size(); ++t)
				{
					vHeure.at(t) = vHeure.at(t)%2400; //We do not support our flags at the moment
				}

			}
			nomDernierArret = arretCourant->ObtenirNomIntersection1() + ((arretCourant->ObtenirNomIntersection2() == "" )? "" : (" / " + arretCourant->ObtenirNomIntersection2()));
		}


		std::string extraInfoDirection = circuitCourant->ObtenirExtraInfoDirection();
		if(extraInfoDirection == "")
			extraInfoDirection = nomDernierArret;
		//Hack temporaire pour STL (il va falloir faire des mini spécialisation pour ce genre de chose là...
		if(m_TransportService->ObtenirNomCourt() == "stl")
		{
			extraInfoDirection = circuitCourant->ObtenirNom();
		}
		std::string strCodeDirection = m_ExtraInfoDirectionHelpers->ExtraInfoStringToCode(extraInfoDirection);
		circuitCourant->ChangerExtraInfoDirection(strCodeDirection);
		circuitCourant->ChangerDirection(EXTRAINFO);
	}


	//std::map<unsigned int, std::vector<std::string>>
	//Hack temporaire pour STL (il va falloir faire des mini spécialisation pour ce genre de chose là...
	if(m_TransportService->ObtenirNomCourt() == "stl")
	{
		std::map<unsigned int, std::vector<std::string>> numberToNameList;
		for (std::vector<Circuit*>::iterator circuitIt = vCircuit->begin(); circuitIt != vCircuit->end(); ++circuitIt)
		{
			Circuit* circuitCourant = (*circuitIt);
			numberToNameList[circuitCourant->ObtenirNumero()].push_back(Utils::String::EnleveEspaceDebutFin(Utils::String::RemoveString(circuitCourant->ObtenirNom(), "Direction")));
		}
		for (std::vector<Circuit*>::iterator circuitIt = vCircuit->begin(); circuitIt != vCircuit->end(); ++circuitIt)
		{
			Circuit* circuitCourant = (*circuitIt);
			const std::vector<std::string>& vName = numberToNameList[circuitCourant->ObtenirNumero()];
			std::string name("");
			for (unsigned int i = 0; i < vName.size(); ++i)
			{
				name+=vName.at(i)+" / ";
			}
			name=name.substr(0, name.size()-3);
			circuitCourant->ChangerNom(name);
		}
	}
	//Spécialisation Post Processing
	if(m_TransportService->ObtenirNomCourt() == "stm")
	{
		m_parsingState = POSTPROCESS;
		ReadFile("metrodebfin.txt");
	}



}


void GTFSDatabaseCreator::RxData(const std::string& strBuf)
{

	if(!strBuf.empty())
	{
		switch(m_parsingState)
		{
		case TRANSITINFORMATION:
			ParseAgency(strBuf);
			break;
		case LISTECIRCUIT:
			ParseRoutes(strBuf);
			break;
		case TYPEHORAIRE:
			ParseCalendar(strBuf);
			break;

		case JOURFERIES:
			ParseCalendarDates(strBuf);
			break;

		case LISTEARRET:
			ParseStops(strBuf);
			break;

		case LISTETRIPS:
			ParseTrips(strBuf);
			break;
		case LISTEHORAIRE:
			ParseStopTimes(strBuf);
			break;

		case POSTPROCESS: //This should be a specialisation
			ParsePostProcess(strBuf);
			break;


		/*case FREQUENCE:
			ParseFrequence(strBuf);
			break;*/

		case NONE:
		default:
			LOGERROR("Unknown parsing state...");
		}
	}
	else
		LOGWARNING("Current file is empty, processing function was not called");


}
//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#agency_txt___Field_Definitions
void GTFSDatabaseCreator::ParseAgency(const std::string& strBuf)
{
	//On va devoir réviser pcq je suis pas sûr que l'autre est tjs le même... et agency_id est optionnel...
	//agency_id,agency_name,agency_url,agency_timezone,agency_phone,agency_lang
	//RTL,Réseau de transport de Longueuil,http://www.rtl-longueuil.qc.ca,America/Montreal,(450) 463-0131,fr

	StringArray lines = Utils::String::SplitInLines(strBuf);
	std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
	if(lines.size() == 2)
	{
		StringArray infoArray = Utils::String::Split(lines.at(1) , ",");
		if(infoArray.size() >= 4)
		{			
			Information info = m_TransportService->ObtenirInfo();
			//We want the short name to stay the same once we assign it...
			LOGINFO("Parsing GTFS for " << infoArray.at(fieldNameToIndex["agency_name"]));
			//info.m_ShortName = infoArray.at(fieldNameToIndex["agency_id"]);
			//info.m_LongName = infoArray.at(fieldNameToIndex["agency_name"]);
			m_TransportService->DefinirInfo(info);
			//TODO: Rest is unused at the moment.
		}
	}

}


//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#calendar_dates_txt___Field_Definitions
void GTFSDatabaseCreator::ParseCalendarDates(const std::string& strBuf)
{
	//Todo: On doit détecter quand toute les journées ne sont pas des jours fériées
	StringArray lines = Utils::String::SplitInLines(strBuf);	
	if(!lines.empty())
	{
		std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
		for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
		{
			if(!(*it).empty())
			{
				StringArray datesParticuliere = Utils::String::Split(Utils::String::EnleveEspaceDebutFin(*it) , ",");
				unsigned int date, exception_type;
				if(Utils::String::StringToUInt(datesParticuliere[fieldNameToIndex["date"]], date) && Utils::String::StringToUInt(datesParticuliere[fieldNameToIndex["exception_type"]], exception_type))
				{
					if(exception_type == 1)
					{
						HoraireInfo info;
						info.horaireDate = date;
						info.startDate = date;
						info.endDate = date;
						m_serviceIdToHoraireInfo[datesParticuliere[fieldNameToIndex["service_id"]]].push_back(info);
					}
					m_dateAvecHoraireParticulier[datesParticuliere[fieldNameToIndex["service_id"]]] = std::pair<unsigned int, unsigned int>(date,exception_type);
				}
				else
					LOGERROR("Could not convert " << datesParticuliere[fieldNameToIndex["date"]] << " or " << datesParticuliere[fieldNameToIndex["exception_type"]] << " to a number.");
			}		
		}
	}
}


//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#calendar_txt___Field_Definitions
void GTFSDatabaseCreator::ParseCalendar(const std::string& strBuf)
{
	if(strBuf != "")
	{
		StringArray lines = Utils::String::SplitInLines(strBuf);
		std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
		//TODO: put the code directly
		//service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date

		const int service_id = fieldNameToIndex["service_id"], monday = fieldNameToIndex["monday"], tuesday = fieldNameToIndex["tuesday"], wednesday = fieldNameToIndex["wednesday"], thursday = fieldNameToIndex["thursday"],
			friday = fieldNameToIndex["friday"], saturday = fieldNameToIndex["saturday"], sunday = fieldNameToIndex["sunday"], start_date = fieldNameToIndex["start_date"], end_date = fieldNameToIndex["end_date"]; 
		for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
		{
			if(!(*it).empty())
			{
				StringArray horaireArray = Utils::String::Split(*it , ",");
				const std::string& key = horaireArray.at(service_id);

				/*std::map<std::string, std::pair<unsigned int, unsigned int>>::iterator it2 =m_horaireMap.find(key);
				if(it2 != m_horaireMap.end() && (*it2)->second == 1)
				{
				LOGWARNING("Not implemented yet...");
				//This case is for when a date has the same schedule of another day 
				//We will have to create a schedule for all those date when the existing service id exist for a bus
				}*/
				HoraireInfo info;
				if(!(Utils::String::StringToType(horaireArray.at(start_date), info.startDate) &&  Utils::String::StringToType(horaireArray.at(end_date), info.endDate)))
					LOGERROR("start_date " << horaireArray.at(start_date) << " and end_date " << horaireArray.at(end_date));


				bool intervalleExistant = false;
				for (unsigned int i = 0; i < m_TransportService->m_intervallesHoraires.size(); ++i)
				{
					const std::pair<unsigned int, unsigned int>& intervalle = m_TransportService->m_intervallesHoraires.at(i);
					unsigned int firstDate = intervalle.first;
					unsigned int endDate = intervalle.second;
					if((info.startDate >= firstDate && info.endDate <= endDate))
						intervalleExistant = true;
				}
				if(!intervalleExistant && info.startDate!= info.endDate)
					m_TransportService->m_intervallesHoraires.push_back(std::pair<unsigned int, unsigned int>(info.startDate, info.endDate));



				//This case is for when we have a special schedule for a day
				if(horaireArray.at(start_date) == horaireArray.at(end_date)) 
				{
					unsigned int date;
					if(!Utils::String::StringToUInt(horaireArray.at(start_date), date))
						LOGERROR("Unable to convert date \'" << start_date << "\' to number.");
					info.horaireDate = date;
					m_serviceIdToHoraireInfo[key].push_back(info);
					//LOGDEBUG("Creating the type of schedule for " << date);
				}
				else if(horaireArray.at(monday) == "0" && horaireArray.at(tuesday) == "0" && horaireArray.at(wednesday) == "0" && horaireArray.at(thursday) == "0" && horaireArray.at(friday) == "0" && horaireArray.at(saturday) == "0" && horaireArray.at(sunday) == "0")
				{
					std::map<std::string, std::pair<unsigned int, unsigned int>>::iterator it2 =m_dateAvecHoraireParticulier.find(key);
					if((it2 != m_dateAvecHoraireParticulier.end()) && ((*it2).second.second == 1))
					{
						info.horaireDate = (*it2).second.first;
						m_serviceIdToHoraireInfo[key].push_back(info);
					}
					else
					{
						LOGERROR("We will not be able to know the date for " << key);
					}
				}
				else
				{
					int horaireId = 0;
					horaireId += horaireArray.at(monday) == "1" ? (unsigned int) Utils::HoraireHelpers::LUNDI : 0;
					horaireId += horaireArray.at(tuesday) == "1" ? (unsigned int) Utils::HoraireHelpers::MARDI : 0;
					horaireId += horaireArray.at(wednesday) == "1" ? (unsigned int) Utils::HoraireHelpers::MERCREDI : 0;
					horaireId += horaireArray.at(thursday) == "1" ? (unsigned int) Utils::HoraireHelpers::JEUDI : 0;
					horaireId += horaireArray.at(friday) == "1" ? (unsigned int) Utils::HoraireHelpers::VENDREDI : 0;
					horaireId += horaireArray.at(saturday) == "1" ? (unsigned int) Utils::HoraireHelpers::SAMEDI : 0;
					horaireId += horaireArray.at(sunday) == "1" ? (unsigned int) Utils::HoraireHelpers::DIMANCHE : 0;
					info.horaireDate = horaireId; 
					m_serviceIdToHoraireInfo[key].push_back(info);
				}
				
				/*else if(horaireArray.at(monday) == "1" && horaireArray.at(tuesday) == "1" && horaireArray.at(wednesday) == "1" && horaireArray.at(thursday) == "1" && horaireArray.at(friday) == "1" && horaireArray.at(saturday) == "0" && horaireArray.at(sunday) == "0")
				{
					info.horaireDate = (unsigned int)Utils::HoraireHelpers::SEMAINE;
					m_serviceIdToHoraireInfo[key].push_back(info);
				}
				else if(horaireArray.at(monday) == "0" && horaireArray.at(tuesday) == "0" && horaireArray.at(wednesday) == "0" && horaireArray.at(thursday) == "0" && horaireArray.at(friday) == "0" && horaireArray.at(saturday) == "1" && horaireArray.at(sunday) == "0")
				{
					info.horaireDate = (unsigned int)Utils::HoraireHelpers::SAMEDI;
					m_serviceIdToHoraireInfo[key].push_back(info);
				}
				else if(horaireArray.at(monday) == "0" && horaireArray.at(tuesday) == "0" && horaireArray.at(wednesday) == "0" && horaireArray.at(thursday) == "0" && horaireArray.at(friday) == "0" && horaireArray.at(saturday) == "0" && horaireArray.at(sunday) == "1")
				{
					info.horaireDate = (unsigned int)Utils::HoraireHelpers::DIMANCHE;
					m_serviceIdToHoraireInfo[key].push_back(info);
				}
				
				else
				{
					LOGERROR("Unknown schedule for "  << key);
				}*/
			}
		}
	}
}
//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#routes_txt___Field_Definitions
void GTFSDatabaseCreator::ParseRoutes(const std::string& strBuf)
{
	//std::map<std::string, RoutesInfo> m_circuitIdToRoutes; //RoutesInfo shall contains all the pertinent information in routes.txt for a route_id.
	StringArray lines = Utils::String::SplitInLines(strBuf);
	std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
	std::map<std::string, unsigned int>::iterator fieldIt;
	fieldIt = fieldNameToIndex.find("route_desc");
	bool hasRouteDesc = fieldIt != fieldNameToIndex.end();
	fieldIt = fieldNameToIndex.find("route_color");
	bool hasRouteColor = fieldIt != fieldNameToIndex.end();
	bool useColor = m_TransportService->ObtenirInfo().m_useColor;
	int routeCount = 0;
	for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
	{
		StringArray circuitArray = Utils::String::Split(*it , ",");
		Routes* r = new Routes();
		r->route_id = circuitArray.at(fieldNameToIndex["route_id"]);
		r->route_short_name = circuitArray.at(fieldNameToIndex["route_short_name"]);
		r->route_long_name = circuitArray.at(fieldNameToIndex["route_long_name"]);

		fieldIt = fieldNameToIndex.find("route_desc");
		if(hasRouteDesc)
			r->route_desc = circuitArray.at(fieldNameToIndex["route_desc"]);
		else
			r->route_desc = "";

		if(hasRouteColor &&  useColor)
		{
			r->route_color = circuitArray.at(fieldNameToIndex["route_color"]);
			r->route_color = r->route_color == "" ? "000000" : r->route_color;
		}
		else
			r->route_color = "000000";

		m_routeidToRoutes[r->route_id] = r;
		++routeCount;
	}
	LOGINFO("Adding " << routeCount << " routes to database.");
}


//wheelchair_accessible
//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#trips_txt___Field_Definitions
void GTFSDatabaseCreator::ParseTrips(const std::string& strBuf)
{
	//route_id,service_id,trip_i,direction_id,block_id,shape_id
	//1,DI,1_1_R_DI_1601_07_45,0,1601_2,1_1_R
	StringArray lines = Utils::String::SplitInLines(strBuf);
	std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
	std::map<std::string, unsigned int>::iterator fieldIterator;
	fieldIterator = fieldNameToIndex.find("direction_id");
	bool hasDirectionId = fieldIterator != fieldNameToIndex.end();
	fieldIterator = fieldNameToIndex.find("trip_short_name");
	bool hasShortName = fieldIterator != fieldNameToIndex.end();
	fieldIterator = fieldNameToIndex.find("trip_headsign");
	bool hasTripHeadsign = fieldIterator != fieldNameToIndex.end();
	fieldIterator = fieldNameToIndex.find("wheelchair_accessible");
	bool hasWheelChairInfo = fieldIterator != fieldNameToIndex.end();


	int tripCount = 0;
	for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
	{
		StringArray tripArray = Utils::String::Split(*it , ",");
		Trips* t = new Trips();
		t->trip_id = tripArray.at(fieldNameToIndex["trip_id"]);
		t->service_id = tripArray.at(fieldNameToIndex["service_id"]);		
		if(hasDirectionId)
			t->direction_id = tripArray.at(fieldNameToIndex["direction_id"]);
		else
			t->direction_id = "";

		if(hasShortName)
			t->trip_short_name = tripArray.at(fieldNameToIndex["trip_short_name"]);
		else
			t->trip_short_name = "";

		if(hasTripHeadsign)
			t->trip_headsign = tripArray.at(fieldNameToIndex["trip_headsign"]);
		else
			t->trip_headsign = "";

		if(hasWheelChairInfo)
			t->wheelchair_accessible = tripArray.at(fieldNameToIndex["wheelchair_accessible"]);
		else
			t->wheelchair_accessible = "0"; //Info inconnu


		Routes* r = m_routeidToRoutes[tripArray.at(fieldNameToIndex["route_id"])];
		assert(r);
		t->parentRoute = r;
		m_tripIdToTrips[t->trip_id] = t;
		r->m_trips.push_back(t);
		++tripCount;
	}
	LOGINFO("Adding " << tripCount << " trips to database.");
}


//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#stops_txt___Field_Definitions
void GTFSDatabaseCreator::ParseStops(const std::string& strBuf)
{	
	StringArray lines = Utils::String::SplitInLines(strBuf);
	std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));
	std::map<std::string, unsigned int>::iterator fieldIterator;
	fieldIterator = fieldNameToIndex.find("stop_code");
	bool hasStopCode = fieldIterator != fieldNameToIndex.end() && !m_TransportService->ObtenirInfo().m_useStopId;
	fieldIterator = fieldNameToIndex.find("stop_desc");
	bool hasStopDesc = fieldIterator != fieldNameToIndex.end();
	int stopCount = 0;
	for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
	{
		StringArray arretArray = Utils::String::Split(*it , ",");
		if(!arretArray.empty())
		{
			Stops* s     = new Stops();		
			s->stop_code = hasStopCode? arretArray[fieldNameToIndex["stop_code"]] : "";
			s->stop_desc = hasStopDesc? arretArray[fieldNameToIndex["stop_desc"]] : "";
			s->stop_id   = arretArray[fieldNameToIndex["stop_id"]];
			s->stop_lat  = arretArray[fieldNameToIndex["stop_lat"]];
			s->stop_lon  = arretArray[fieldNameToIndex["stop_lon"]];
			s->stop_name  = arretArray[fieldNameToIndex["stop_name"]];
			size_t posS = s->stop_name.find('[');
			size_t posE = s->stop_name.find(']');
			if(posS != std::string::npos && posE != std::string::npos)
			{
				//Remove Stop_code from STL bus stop name 
				s->stop_name = Utils::String::EnleveEspaceDebutFin(s->stop_name.substr(0, posS)+ s->stop_name.substr(posE+1));
			}

			m_stopidToStops[s->stop_id] = s;
			++stopCount;
		}

	}
	LOGINFO("Adding " << stopCount << " stops to database.");

}

std::string GTFSDatabaseCreator::createKey(Routes* r, Trips* t)
{
	return r->route_short_name+t->direction_id+r->route_long_name+t->trip_headsign;
}

bool sortStopTimeSequence (const StopTime &lhs, const StopTime &rhs){
	return lhs.stop_sequence < rhs.stop_sequence;
}

//http://code.google.com/intl/fr/transit/spec/transit_feed_specification.html#stop_times_txt___Field_Definitions
void GTFSDatabaseCreator::ParseStopTimes(const std::string& strBuf)
{
	
	//Create Circuit
	//route_id+directionid //key for circuit
	LOGINFO("Creating circuits.");
	unsigned int nbCircuit = 0;
	for(std::map<std::string, Routes*>::const_iterator it = m_routeidToRoutes.begin(); it != m_routeidToRoutes.end(); ++it) 
	{
		Routes* r = (*it).second;
		for(unsigned int i = 0; i < r->m_trips.size(); ++i)
		{		

			Trips* t = r->m_trips.at(i);
			std::string key = createKey(r,t);//(r->route_short_name+t->direction_id+r->route_long_name+t->trip_headsign); //TODO: Verify the key...
			std::map<std::string, Circuit*>::iterator circuitIt = mRouteDirectionToCircuit.find(key);
			CircuitGTFS* c = NULL;
			if(circuitIt == mRouteDirectionToCircuit.end())
			{
				std::string name = r->route_long_name;
				unsigned int number;
				if(!Utils::String::StringToType(r->route_short_name, number)) //If the short name is not a number, we will try the route_id
				{
					if(!Utils::String::StringToType(r->route_id, number))
						LOGERROR("Unable to find a valid number for circuit " << r->route_short_name << " " << r->route_long_name);
				}
				if(r->route_desc != "")
					LOGWARNING("Not yet using route_desc which contains: " << r->route_desc);

				//We have to create the Circuit
				c = new CircuitGTFS(name, number, EXTRAINFO, t->trip_headsign);
				++nbCircuit;
				c->DefinirCouleur(r->route_color);
				mRouteDirectionToCircuit[key] = c;

		
				m_TransportService->AjouterCircuit(c);
				LOGINFO("Creating " <<  number << " " << name << " " << t->trip_headsign);
			}
			else
			{
				c = (CircuitGTFS*) (*circuitIt).second;
			}
			c->m_tripsList.push_back(t->trip_id);
		}

	}
	LOGINFO("Created " << nbCircuit << " circuits done.");

	StringArray lines = Utils::String::SplitInLines(strBuf);
	std::map<std::string, unsigned int> fieldNameToIndex = getIndexList(lines.at(0));

	LOGINFO("Creating passage list for each trip");
	//Pour chaque trip, la liste des passage
	unsigned int i = 0;
	unsigned int count = lines.size();
	std::map<std::string, std::vector<StopTime>> tripIdToSequence;
	for(StringArray::const_iterator it = ++lines.begin(); it != lines.end(); ++it) //We want to skip the first line
	{
		if(i % 100000 == 0)
			std::cout << i << "/" << count << std::endl;
		StringArray dataArray = Utils::String::Split(*it , ",");
		StopTime passage;
		const std::string& tripID = dataArray[fieldNameToIndex["trip_id"]];
		if(Utils::String::StringToUInt(Utils::Time::StringToInternalFormat(dataArray[fieldNameToIndex["departure_time"]]), passage.passageTime))
		{
			passage.stop_id = Utils::String::EnleveEspaceDebutFin(dataArray[fieldNameToIndex["stop_id"]]);
			unsigned int stop_sequence;
			if(!Utils::String::StringToUInt(dataArray[fieldNameToIndex["stop_sequence"]], stop_sequence))
				LOGERROR("Unable to convert stop_sequence to a number !");
			passage.stop_sequence = stop_sequence;
			tripIdToSequence[tripID].push_back(passage);
		}
		else
		{
			passage.passageTime = -1; //Temps de passage à interpoler !
			passage.stop_id = Utils::String::EnleveEspaceDebutFin(dataArray[fieldNameToIndex["stop_id"]]);
			unsigned int stop_sequence;
			if(!Utils::String::StringToUInt(dataArray[fieldNameToIndex["stop_sequence"]], stop_sequence))
				LOGERROR("Unable to convert stop_sequence to a number !");
			passage.stop_sequence = stop_sequence;
			tripIdToSequence[tripID].push_back(passage);
			//We need to interpolate between the last stop with an
			//Some GTFS do not respect spec and do not put departure_time if the bus doesn't stop...??
			//LOGERROR("Unable to convert " << dataArray[fieldNameToIndex["departure_time"]] << " to internal time format");
		}
		++i;
	}
	LOGINFO("Creating passage list for each trip done.");

	//Interpolating data
	for(auto it = std::begin(tripIdToSequence); it != std::end(tripIdToSequence); ++it)
	{
		auto tripId = (*it).first;
		std::vector<StopTime>& timeVec = (*it).second;
		std::sort(std::begin(timeVec), std::end(timeVec), sortStopTimeSequence);
		for (unsigned int i = 0; i <timeVec.size(); ++i)
		{
			if(timeVec[i].passageTime == -1) // We need to interpolate
			{
				unsigned int endIdx = -1;
				//Trouver le prochain passage avec un passage et interpoler pour tous les autres
				for(unsigned int j = i; j < timeVec.size() && endIdx == -1; ++j)
				{
					if(timeVec[j].passageTime != -1)
					{
						endIdx = j;
					}
				}
				unsigned int beginIdx = i - 1;
				if(beginIdx >= 0 && endIdx < timeVec.size())
				{
					auto startInterval = Utils::Time::StringToPtime(timeVec[beginIdx].passageTime);
					auto endInterval  = Utils::Time::StringToPtime(timeVec[endIdx].passageTime); 
					auto deltaTime = endInterval-startInterval;
					auto deltaStopSeq = timeVec[endIdx].stop_sequence-timeVec[beginIdx].stop_sequence;
					auto timeInc = deltaTime/deltaStopSeq;
					auto newTime = startInterval;
					for (; i < endIdx; ++i)
					{
						newTime+=timeInc;
						boost::posix_time::time_duration td = newTime.time_of_day();
						std::string strMin=Utils::String::IntToString(td.minutes());
						if(strMin.size()==1)
							strMin="0"+strMin;
						std::string strTime=Utils::String::IntToString(td.hours())+strMin;
						unsigned int nPassage;
						if(Utils::String::StringToUInt(strTime, nPassage))
						{
							timeVec[i].passageTime = nPassage;
						}
						else
						{
							LOGERROR("Temps non valide " << strTime);
							assert(false);
						}
					}
				}
				else
				{
					assert(false);
					LOGERROR("We cannot interpolate without a end an a start point !");
				}
				

			}
		}
	}
	

	//TODO: Ajouter du code de sorting d'arrêt en fonction du # de séquence en prenant en compte le fait que certains trips ont plus d'arrêt

	std::vector<Circuit*>* vCircuit = m_TransportService->ObtenirVecteurCircuit();
	for(unsigned int i = 0; i < vCircuit->size(); ++i)
	{
		LOGINFO(i << "/"<<vCircuit->size());
		CircuitGTFS* c = (CircuitGTFS*) vCircuit->at(i);
		const std::vector<std::string>& vTrips = c->m_tripsList;
		for(unsigned int j = 0; j < vTrips.size(); ++j)
		{
			const std::string& tripId = vTrips.at(j);
			const std::vector<StopTime>& vStopTimes = tripIdToSequence[tripId];
			for (unsigned int s=0; s < vStopTimes.size(); ++s)
			{
				const StopTime& time = vStopTimes.at(s);
				std::map<std::string, Stops*>::iterator stopIt =m_stopidToStops.find(time.stop_id);
				if(stopIt != m_stopidToStops.end())
				{	
					const Stops* stop = stopIt->second;
					std::string stopId;
					if(m_TransportService->ObtenirInfo().m_useStopId)
						stopId = stop->stop_id;
					else 
						stopId = stop->stop_code;

					ArretGTFS* a = (ArretGTFS*) c->ObtenirArret(stopId);
					if(a == NULL)
					{
						a = new ArretGTFS(stopId, stop->stop_name, time.stop_sequence);
						a->SetLatitudeAndLongitude(stop->stop_lat, stop->stop_lon);
						c->AjouterArret(a);
					}
					
					
					a->setStopSequence(time.stop_sequence);

					std::map<std::string, std::vector<HoraireInfo>>::iterator serviceIt = m_serviceIdToHoraireInfo.find(m_tripIdToTrips[tripId]->service_id);
					if(serviceIt != m_serviceIdToHoraireInfo.end())
					{
						const std::vector<HoraireInfo>& horaireList = (*serviceIt).second;
						for (unsigned int horListIt = 0; horListIt < horaireList.size(); ++horListIt)
						{
							const HoraireInfo& horaire = horaireList.at(horListIt);
							//TODO: Régler Bogue des temps "double" dû au fait qu'on met les temps des 2 types d'horaires dans les même à cause qu'on cherche par "date"
							//horaire
							//trip_id->horaire
							HoraireGTFS* h = a->ObtenirHoraireParServiceId(horaire.horaireDate, m_tripIdToTrips[tripId]->service_id);
							//Créer Classe HoraireGTFS avec id GTFS
							if(h == NULL)
							{								
								h = new HoraireGTFS(horaire.horaireDate, m_tripIdToTrips[tripId]->service_id);
								h->m_start_date = horaire.startDate;
								h->m_end_date = horaire.endDate;
								a->AjouterHoraire(h);

							}
							TimeCode tc;
							//tc.fHandicape = m_tripIdToTrips[tripId]->wheelchair_accessible == "1";
							//TODO: Fix this bug Gives wrong schedule
							h->AjouterHeure(Utils::Time::TimeToInternalFormatWithCode(time.passageTime, tc));
						}

					}
					else
					{
						LOGERROR("Unknown schedule serviceid" << m_tripIdToTrips[tripId]->service_id);
					}			
				}
				else
					LOGERROR("Error, can't find " << time.stop_id << " for circuit " << c->ObtenirNom() << " - "  <<c->ObtenirNumero() );

			}
		}
		// We sort the bus stop by stop_sequence
		c->sortStopList();
	}
	LOGINFO("Done !");
}

void GTFSDatabaseCreator::ParsePostProcess(const std::string& strBuf)
{
	StringArray lines = Utils::String::SplitInLines(strBuf);	
	for (unsigned int i = 0; i < lines.size(); ++i )
	{
		StringArray fields = Utils::String::Split(lines.at(i), ";");
		if(fields.size() == 9)
		{
			unsigned int noCircuit;
			if(Utils::String::StringToUInt(fields[0], noCircuit))
			{
				std::string directionName = "Station " + fields[2];
				std::string strCodeDirection = m_ExtraInfoDirectionHelpers->ExtraInfoStringToCode(directionName);
				Circuit* c = m_TransportService->ObtenirCircuit(noCircuit, strCodeDirection);
				if(c)
				{
					const std::string nomArret = "Station " + fields[1];
					Arret* a = c->ObtenirArret(nomArret, "");
					if(a)
					{
						Horaire* h = NULL;
						std::vector<Horaire*> vHoraire = a->ObtenirHoraireParNom(Utils::HoraireHelpers::SEMAINE);
						for (unsigned int i = 0; i <vHoraire.size(); ++i)
						{
							h = vHoraire.at(i);
							if(TruncateSchedule(h, fields[3], fields[4])) 
								LOGINFO("Schedule truncated for " << noCircuit << " " << directionName << " " << a->ObtenirNomIntersection1() << " Semaine");
						}

						vHoraire = a->ObtenirHoraireParNom(Utils::HoraireHelpers::SAMEDI);
						for (unsigned int i = 0; i <vHoraire.size(); ++i)
						{
							h = vHoraire.at(i);
							if(TruncateSchedule(h, fields[5], fields[6])) 
								LOGINFO("Schedule truncated for " << noCircuit << " " << directionName << " " << a->ObtenirNomIntersection1() << " Samedi");
						}
						
						vHoraire = a->ObtenirHoraireParNom(Utils::HoraireHelpers::DIMANCHE);
						for (unsigned int i = 0; i <vHoraire.size(); ++i)
						{
							h = vHoraire.at(i);
							if(TruncateSchedule(h, fields[7], fields[8])) 
								LOGINFO("Schedule truncated for " << noCircuit << " " << directionName << " " << a->ObtenirNomIntersection1() << " Dimanche");
						}						
					}
					else
					{
						LOGERROR(noCircuit << " " << directionName << " arret " << nomArret<< " introuvable." );
						assert(false);
					}
				}
				else
				{
					LOGERROR("Cannot find " << noCircuit << " " << directionName);
					assert(false);
				}
					//Get Stop By Name
					//Get Direction By Name
					// Get Schedule
					//fieldNameToIndex[indexList[i]] = i;
			}
			
		}
		
	}
}


bool GTFSDatabaseCreator::TruncateSchedule(Horaire* h, const std::string& startTime, const std::string& endTime)
{
	bool truncated = false;
	unsigned int startTimeNb, endTimeNb;
	std::string sTime = Utils::Time::StringToInternalFormat(startTime);
	std::string eTime = Utils::Time::StringToInternalFormat(endTime);
	if(Utils::String::StringToUInt(sTime, startTimeNb) && Utils::String::StringToUInt(eTime, endTimeNb))
	{
		std::vector<unsigned int>& vH = h->ObtenirVecteurHeure();	
		unsigned int startIdx = 0;
		unsigned int endIdx = vH.size()-1;
		while(vH[startIdx] < startTimeNb )
		{
			LOGINFO("Removing " << vH[startIdx] << " from " << h->ObtenirNom());
			++startIdx;
		}
		if(startIdx != 0 && vH[startIdx] != startTimeNb)
		{
			vH[--startIdx] = startTimeNb;
			LOGINFO("Added start time " << startTimeNb);
		}
			
		
		while(vH[endIdx] > endTimeNb )
		{
			LOGINFO("Removing " << vH[endIdx] << " from " << h->ObtenirNom());
			--endIdx;
		}
		if(endIdx != vH.size() -1 && vH[endIdx] != endTimeNb)
		{
			vH[++endIdx] = endTimeNb;
			LOGINFO("Added end time " << endTimeNb);
		}

		if(startIdx < vH.size() && endIdx > 0)
		{
			if(startIdx != 0 || endIdx != vH.size() -1)
			{
				LOGINFO("Truncating vector");
				std::vector<unsigned int>::const_iterator newStart = vH.begin()+startIdx;
				std::vector<unsigned int>::const_iterator newEnd = vH.begin()+endIdx;
				vH = std::vector<unsigned int>(newStart, newEnd+1);
				truncated = true;
			}
		}
		else
			assert(false);
		h->DefinirVecteurHeure(vH);		//Probablement inutile
	}
	return truncated;
	
	
}


std::map<std::string, unsigned int> GTFSDatabaseCreator::getIndexList(const std::string& firstLineOfFile)
{
	std::map<std::string, unsigned int> fieldNameToIndex;
	StringArray indexList = Utils::String::Split(firstLineOfFile, ",");
	for (unsigned int i = 0; i < indexList.size(); ++i )
	{
		fieldNameToIndex[indexList[i]] = i;
	}
	return fieldNameToIndex;
}
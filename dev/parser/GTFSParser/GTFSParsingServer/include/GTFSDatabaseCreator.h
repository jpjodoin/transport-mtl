#ifndef GTFS_DATABASE_CREATOR_H
#define GTFS_DATABASE_CREATOR_H


#include "FileDatabaseCreator.h"


#include <map>
#include "TimeHelpers.h"
class HoraireGTFS;
//DatabaseCreator* getNewDatabaseCreator(std::map<std::string, std::string> configMap);

struct AscendingHoraireTimeSort
{
	bool operator()(unsigned int heure1, unsigned int heure2)
	{
		return ( Utils::Time::TimeWithCodeToTime(heure1) < Utils::Time::TimeWithCodeToTime(heure2) );
	}
};


struct HoraireInfo
{
	unsigned int horaireDate;
	unsigned int startDate;
	unsigned int endDate;
};

struct Trips;

struct Routes
{
	
	std::string route_id;
	std::string route_short_name;
	std::string route_long_name;
	std::string route_desc; //opt.
	std::string route_color; //opt
	
	std::vector<Trips*> m_trips;
};


struct Trips
{
	Routes* parentRoute;
	std::string service_id;
	std::string trip_id;
	std::string trip_headsign; //opt.
	std::string trip_short_name; //opt.
	std::string direction_id; //opt. 0 ou 1 (aller et retour)
	std::string wheelchair_accessible; //opt 0 1 ou 2  ( 1 == accessible)
};

struct Stops
{
	std::string stop_id;
	std::string stop_code; //opt.
	std::string stop_name;
	std::string stop_desc; //opt.
	std::string stop_lat; 
	std::string stop_lon;
};

struct StopTime
{
	unsigned int passageTime;
	std::string stop_id;
	unsigned int stop_sequence;
};





class  GTFSDatabaseCreator: public FileDatabaseCreator
{
	enum GTFSParsingState
	{
		NONE,
		TRANSITINFORMATION,
		LISTECIRCUIT,
		TYPEHORAIRE,
		JOURFERIES,
		LISTEARRET,
		LISTETRIPS,
		LISTEHORAIRE,
		FREQUENCE,
		POSTPROCESS

	};

public:
	GTFSDatabaseCreator(Information info, const std::string& folderName);
	virtual ~GTFSDatabaseCreator();

	virtual void RxData(const std::string& strBuf);
	virtual void GetData();
private:
	void ParseAgency(const std::string& strBuf);
	void ParseRoutes(const std::string& strBuf);
	void ParseCalendar(const std::string& strBuf);
	void ParseCalendarDates(const std::string& strBuf);
	void ParseStops(const std::string& strBuf);
	void ParseTrips(const std::string& strBuf);
	void ParseStopTimes(const std::string& strBuf);
	virtual void ParsePostProcess(const std::string& strBuf);
	std::string createKey(Routes* r, Trips* t);
	bool TruncateSchedule(Horaire* h, const std::string& startTime, const std::string& endTime);
	
	std::map<std::string, unsigned int> getIndexList(const std::string& firstLineOfFile);
	GTFSParsingState m_parsingState;
	std::map<std::string, Circuit*> mRouteDirectionToCircuit;
	std::map<std::string, Routes*> m_routeidToRoutes;
	std::map<std::string, Stops*> m_stopidToStops;
	//std::map<std::string, HoraireInfo*> m_serviceIdToHoraireInfo;
	std::map<std::string, std::vector<HoraireInfo>> m_serviceIdToHoraireInfo;
	std::map<std::string, Trips*> m_tripIdToTrips;
	std::map<std::string, std::pair<unsigned int, unsigned int>> m_dateAvecHoraireParticulier;
	std::map<std::string, HoraireGTFS*> m_serviceIdToHoraire;

};

#endif



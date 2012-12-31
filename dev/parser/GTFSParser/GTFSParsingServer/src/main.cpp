#include <iostream>
#include "Logger.h"
#include "GTFSDatabaseCreator.h"
#include "Information.h"
#include "StringHelpers.h"

int main(int argc, char* argv[])
{
	//Rajouter l'argument de la couleur

	if(argc>=10)
	{
		std::string verbosity = argv[9];
		if(verbosity == "-v")
			Logger::getInstance()->setLoggingLevel(eDEBUG);
		else if(verbosity == "-i")
			Logger::getInstance()->setLoggingLevel(eINFO);
		else if(verbosity == "-w")
			Logger::getInstance()->setLoggingLevel(eWARNING);
		else if(verbosity == "-e")
			Logger::getInstance()->setLoggingLevel(eERROR);
		else
			Logger::getInstance()->setLoggingLevel(eINFO);
	}


	if(argc >= 9)
	{
		std::string pathToGtfs = argv[1];
		std::string shortName  = argv[2];
		std::string longName   = argv[3];
		
		TransportTypeAffichage type = UNKNOWNTYPE;
		std::string transportType = argv[4];
		if(transportType == "AUTOBUS")
			type = AUTOBUS;
		else if(transportType == "TRAIN")
			type = TRAIN;
		else if(transportType == "METRO")
			type = METRO;
		else
			LOGERROR("Unknown type" << transportType);

		bool displayMap = false;
		std::string mapAvailable = argv[5];
		if(mapAvailable == "display-map")
			displayMap = true;
		else if(mapAvailable == "nodisplay-map")
			displayMap = false;
		else
			LOGERROR("Unknown argument: " << mapAvailable << " Correct argument are display-map or nodisplay-map");


		bool displaySearch = false;
		std::string searchAvailable = argv[6];
		if(searchAvailable == "display-search")
			displaySearch = true;
		else if(searchAvailable == "nodisplay-search")
			displaySearch = false;
		else
			LOGERROR("Unknown argument: " << searchAvailable << " Correct argument are display-search or nodisplay-search");

		LOGINFO("Converting GTFS for " << shortName << " also known as " << longName);


		
		std::string stopIdentifier = argv[7];
		bool useStopId = false;
		if(stopIdentifier == "stop_id")
			useStopId = true;
		else if(stopIdentifier == "stop_code")
			useStopId = false;
		else
			LOGERROR("Unknown argument should be stop_id or stop_code");


		std::string useColorStr = argv[8];
		bool useColor = true;
		if(useColorStr == "use-color")
			useColor = true;
		else if(useColorStr == "nouse-color")
			useColor = false;
		else
			LOGERROR("Unknown argument should be use-color or nouse-color");

		Information inf(shortName, longName, type, true, displaySearch, displayMap, false, useStopId, useColor);

		//GTFS block
		//std::string pathToGtfs = "F:\\svn\\Projet_Transport\\trunk\\dev\\GTFSParser\\gtfs\\rtl\\";
		//Information inf("rtl", "Réseau de transport de Longueuil", AUTOBUS, false, true, false, false);
		//std::string pathToGtfs = "M:\\svn\\rhatec\\trunk\\dev\\GTFSParser\\gtfs\\stl\\";
		//Information inf("stl", "Société de transport de Laval", AUTOBUS, true, true, false, false);
		//std::string pathToGtfs = "M:\\svn\\rhatec\\trunk\\dev\\GTFSParser\\gtfs\\train-amt\\";
		//Information inf("amt", "Agence métropolitaine de transport", TRAIN, true, false, true, false);



		GTFSDatabaseCreator dbCreator(inf, pathToGtfs);
		dbCreator.Initialize();
		dbCreator.GetData();
		dbCreator.SaveDatabase();

	}
	else
	{
		LOGERROR("Not enough argument. Argument should be in the order: " 
			<< "Path to GTFS \n" << "Short name\n" << "Long name\n" << "Type: AUTOBUS, TRAIN or METRO\n"
			<< "If there is a map to display: nodisplay-map or display-map"
			<< "If we should display a search box: display-search or nodisplay-search"
			<< "Optionnally, the level of logging, either -v for verbose, -i for info, -w for warnings and -e for errors only");
	}

	
	//Clean Up Logger
	//LOGDEBUG("Cleaning Logger");
	Logger::getInstance()->cleanUp();

	return 0;
}
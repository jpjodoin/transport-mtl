#include "LogHelpers.h"
#include "boost/format.hpp"
#include "StringHelpers.h"

namespace Utils
{
namespace Logging
{

Utils::Logging::CLog::CLog()
{
}

CLog &Utils::Logging::CLog::Get()
{
   static CLog log;
   return log;
}

bool Utils::Logging::CLog::Init()
{
   log.open("log.txt");
   httprequestLog.open("loghttp.txt");
   parsingLog.open("logparsing.txt");
   savingLog.open("logsaving.txt");
   if(!LoadStrings())return false;
   return true;
}

void Utils::Logging::CLog::Write(int target,unsigned int msgID)
{
   
   if(target&LOG_HTTPREQUEST)
   {
      httprequestLog<< logStrings[msgID]<<"\n";
      httprequestLog.flush();
   }
   if(target&LOG_PARSING)
   {
      parsingLog<< logStrings[msgID]<<"\n";
      parsingLog.flush();
   }
   if(target&LOG_SAVING)
   {
      savingLog<< logStrings[msgID]<<"\n";
      savingLog.flush();
   }
   if(target&LOG)
   {
      savingLog<< logStrings[msgID]<<"\n";
      savingLog.flush();
   }
}
void Utils::Logging::CLog::Write(int target, std::string& msg)
{
   
   if(target&LOG_HTTPREQUEST)
   {
      httprequestLog<< msg <<"\n";
      httprequestLog.flush();
   }
   if(target&LOG_PARSING)
   {
      parsingLog<< msg <<"\n";
      parsingLog.flush();
   }
   if(target&LOG_SAVING)
   {
      savingLog<< msg <<"\n";
      savingLog.flush();
   }
   if(target&LOG)
   {
      savingLog<< msg <<"\n";
      savingLog.flush();
   }
}
void Utils::Logging::CLog::Write(int target, std::string& msg, std::string& test)
{
   
   if(target&LOG_HTTPREQUEST)
   {
      httprequestLog<< boost::format(msg) % test<<"\n";
      httprequestLog.flush();
   }
   if(target&LOG_PARSING)
   {
      parsingLog<< boost::format(msg) % test<<"\n";
      parsingLog.flush();
   }
   if(target&LOG_SAVING)
   {
      savingLog<< boost::format(msg) % test<<"\n";
      savingLog.flush();
   }
   if(target&LOG)
   {
      savingLog<< boost::format(msg) % test<<"\n";
      savingLog.flush();
   }
}

void Utils::Logging::CLog::Write(int target, unsigned int msgID, std::string& test)
{
   Write(target, logStrings[msgID], test);
}

bool Utils::Logging::CLog::LoadStrings()
{
	std::ifstream in("strings");
	if(!in.is_open())return false;

	unsigned long index=101;

	while(!in.eof())
	{
		char szBuf[1024];
		in.getline(szBuf,1024);
		logStrings[index]=szBuf;
		logStrings[index]=Utils::String::Replace(szBuf, "\\n", "\n");
		index++;
	}

	return true;
}


}
}

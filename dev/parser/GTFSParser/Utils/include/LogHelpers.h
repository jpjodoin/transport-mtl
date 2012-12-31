///
/// \file LogHelpers.h
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef CLOG_H
#define CLOG_H
#include <fstream>


namespace Utils
{
namespace Logging
{
const unsigned int LOG_HTTPREQUEST=1;
const unsigned int LOG_PARSING=2;
const unsigned int LOG_SAVING=4;
const unsigned int LOG=8;
const unsigned int LOG_USER=16;
const unsigned int MAX_LOG_STRINGS = 256;



class CLog
{
   protected:
      CLog();

	  std::ofstream log;
      std::ofstream httprequestLog;
      std::ofstream parsingLog;
      std::ofstream savingLog;

      std::string logStrings[MAX_LOG_STRINGS];
      bool LoadStrings();

   public:
      static CLog &Get();

      bool Init();

	  void Write(int target, std::string& msg);
      void Write(int target, std::string& msg, std::string& test);
      void Write(int target, unsigned int msgID, std::string& test);
	  void Write(int target,unsigned int msgID);
};

}
}
#endif
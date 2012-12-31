#ifndef LOGGER_H
#define LOGGER_H

#include "Exports.h"
#include <string>
#include <fstream>
#include <sstream>


enum ELoggingLevel
{
	eDEBUG = 0,
	eINFO,
	eWARNING,
	eERROR,
	eNONE
};

class RHATECCOREDLL_API Logger
{
private:
	Logger();
	~Logger();	
public:
	static Logger* getInstance();
	static void cleanUp();
	void logMsg(ELoggingLevel level, const std::stringstream& error, long line, const std::string& function);
	void logMsg(ELoggingLevel level, const std::string& error, long line, const std::string& function);
	void setLoggingLevel(ELoggingLevel level) { m_level = level;}
	bool errorOccured() { return m_errorOccured;}
	ELoggingLevel getLoggingLevel() { return m_level; }

private:
	ELoggingLevel m_level;
	static Logger* m_instance;
	std::ofstream m_fstream;
	bool m_errorOccured;
	bool m_noFileLog;
	std::string m_logName;
};


//Beware, if the logging level is not good, the statement executed in the message part will never be executed
//Logging macros
#define LOGDEBUG(MSG)\
	do { \
	if(eDEBUG < Logger::getInstance()->getLoggingLevel()){}\
	else {\
	std::stringstream ss; ss << MSG; Logger::getInstance()->logMsg(eDEBUG, ss, __LINE__, __FUNCTION__);}\
	__pragma(warning(push))\
	__pragma(warning(disable:4127))\
	} while (0)\
	__pragma(warning(pop))


#define LOGINFO(MSG)\
	do { \
	if(eINFO < Logger::getInstance()->getLoggingLevel()){}\
	else {\
	std::stringstream ss; ss << MSG; Logger::getInstance()->logMsg(eINFO, ss, __LINE__, __FUNCTION__);}\
	__pragma(warning(push))\
	__pragma(warning(disable:4127))\
	} while (0)\
	__pragma(warning(pop))


#define LOGWARNING(MSG)\
	do { \
	if(eWARNING < Logger::getInstance()->getLoggingLevel()){}\
	else {\
	std::stringstream ss; ss << MSG; Logger::getInstance()->logMsg(eWARNING, ss, __LINE__, __FUNCTION__);}\
	__pragma(warning(push))\
	__pragma(warning(disable:4127))\
	} while (0)\
	__pragma(warning(pop))

#define LOGERROR(MSG)\
	do { \
	if(eERROR < Logger::getInstance()->getLoggingLevel()){}\
	else {\
	std::stringstream ss; ss << MSG; Logger::getInstance()->logMsg(eERROR, ss, __LINE__, __FUNCTION__);}\
	__pragma(warning(push))\
	__pragma(warning(disable:4127))\
	} while (0)\
	__pragma(warning(pop))





#endif
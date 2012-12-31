#include "Logger.h"
#include <iostream>
#include <fstream>
#include <time.h>
#include <Windows.h>
Logger* Logger::m_instance = 0;

//Code to add color in Windows
inline std::ostream& blue(std::ostream &s)
{
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
	SetConsoleTextAttribute(hStdout, FOREGROUND_BLUE
		|FOREGROUND_GREEN|FOREGROUND_INTENSITY);
	return s;
}

inline std::ostream& red(std::ostream &s)
{
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
	SetConsoleTextAttribute(hStdout, 
		FOREGROUND_RED|FOREGROUND_INTENSITY);
	return s;
}

inline std::ostream& green(std::ostream &s)
{
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
	SetConsoleTextAttribute(hStdout, 
		FOREGROUND_GREEN|FOREGROUND_INTENSITY);
	return s;
}

inline std::ostream& yellow(std::ostream &s)
{
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
	SetConsoleTextAttribute(hStdout, 
		FOREGROUND_GREEN|FOREGROUND_RED|FOREGROUND_INTENSITY);
	return s;
}

inline std::ostream& white(std::ostream &s)
{
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
	SetConsoleTextAttribute(hStdout, 
		FOREGROUND_RED|FOREGROUND_GREEN|FOREGROUND_BLUE);
	return s;
}



Logger::Logger()
: m_level(eINFO)
, m_noFileLog(false)
, m_errorOccured(false)
{
	//Get current time formatted to generate log name
	time_t rawtime;	
	time(&rawtime);
	struct tm * timeinfo=localtime(&rawtime);
	char timebuffer[22];
	int size = sizeof("run-20110908-2412.log");
	strftime (timebuffer,sizeof(timebuffer)/sizeof(char),"run-%Y%m%d-%H%M.log",timeinfo);
	m_logName = std::string(timebuffer);
	m_fstream.open(m_logName, std::ofstream::app | std::ofstream::out);
	
}

Logger::~Logger()
{
	//Close File
	m_fstream.close();
}

Logger* Logger::getInstance()
{
	if(!m_instance)
		m_instance = new Logger();
	return m_instance;
}

void Logger::cleanUp()
{
	delete m_instance;
	m_instance = 0;
}

//TODO: Separate FileLogger, ConsoleLogger to different class when we decide to add cooler logger like SendEmailOnerror and WebViewLogger

void Logger::logMsg(ELoggingLevel level, const std::stringstream& msgStream, long line, const std::string& function)
{
	logMsg(level, msgStream.str(), line, function);
}

void Logger::logMsg(ELoggingLevel level, const std::string& msg, long line, const std::string& function)
{
	if(level == eERROR)
		m_errorOccured = true;
	if(level >= m_level)
	{
		//Log Console
		{
			switch(level)
			{
			case eDEBUG:
				std::cout << white;
				break;
			case  eINFO:
				std::cout << green;
				break;
			case eWARNING:
				std::cout << yellow;
				break;
			case  eERROR:
				std::cout << red;
				break;
			}
			std::cout << msg << std::endl << white; 
		}

		//Log File
		if(!m_noFileLog)
		{
			time_t rawtime;	
			time(&rawtime);
			struct tm * timeinfo=localtime(&rawtime);
			char timebuffer[23];
			strftime (timebuffer,sizeof(timebuffer)/sizeof(char),"[%Y.%m.%d@%H:%M:%S] ",timeinfo);
			m_fstream << timebuffer << function << ":" <<  line << " " << msg << std::endl; 			
		}
	}
}
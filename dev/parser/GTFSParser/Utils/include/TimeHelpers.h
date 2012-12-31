///
/// \file TimeHelpers.h
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef TIMEHELPERS
#define TIMEHELPERS



#include <string>

#include "StringHelpers.h"



#pragma warning (push)
#pragma warning (disable: 4244)

#include <boost/date_time/posix_time/posix_time.hpp>

#pragma warning (pop)


struct TimeCode
{
	TimeCode()
	:fLundi(false)
	,fMardi(false)
	,fMercredi(false)
	,fJeudi(false)
	,fVendredi(false)
	,fSamedi(false)
	,fDimanche(false)
	,fHandicape(false)
	,fExtraInfoHoraire(false)
	{
	}
	//Tel que documenter dans la section Code d’heures de Documentation
	bool fHandicape;
	bool fExtraInfoHoraire;
	bool fLundi;
	bool fMardi;
	bool fMercredi;
	bool fJeudi;
	bool fVendredi;
	bool fSamedi;
	bool fDimanche;

	const TimeCode operator+(const TimeCode &other) const 
	{
		TimeCode result = *this;     // Make a copy of myself.  Same as MyClass result(*this);
		result.fHandicape|=other.fHandicape;
		result.fExtraInfoHoraire|=other.fExtraInfoHoraire;
		result.fLundi|=other.fLundi;
		result.fMardi|=other.fMardi;
		result.fMercredi|=other.fMercredi;
		result.fJeudi|=other.fJeudi;
		result.fSamedi|=other.fSamedi;
		result.fDimanche|=other.fDimanche;
		return result;              
	}

};

namespace Utils
{

/// \brief Permet d'aider avec les strings/int de temps
namespace Time
{

	unsigned int TwelveHourToInternational(unsigned int nTime,bool fIsPM);
	unsigned int TimeToInternalFormatWithCode(unsigned int nTime, TimeCode nTimeCode);
	inline unsigned int TimeWithCodeToTime(unsigned int nTimeWithCode)
	{
		return nTimeWithCode&4095; //12 premiers bits à 1
	}
	boost::posix_time::ptime StringToPtime(unsigned int time);
	boost::posix_time::ptime StringToPtime(std::string strTime);
	std::string StringToInternalFormat(std::string strTime);


}
}




#endif //TIMEHELPERS
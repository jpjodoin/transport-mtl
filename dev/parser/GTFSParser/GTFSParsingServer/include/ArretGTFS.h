#pragma once
#include "Arret.h"
class HoraireGTFS;
class ArretGTFS : public Arret
{
public:
	ArretGTFS(const std::string& stopId, const std::string& intersection, unsigned int stopSequence);
	virtual ~ArretGTFS(void);
	void setStopSequence(unsigned int stopSequence);
	HoraireGTFS* ObtenirHoraireParServiceId(unsigned int date,const std::string& serviceId);


	unsigned int m_stop_sequence;
};


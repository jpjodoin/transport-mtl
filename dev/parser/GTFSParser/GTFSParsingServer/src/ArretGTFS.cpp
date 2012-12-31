#include "ArretGTFS.h"
#include "Logger.h"
#include "HoraireGTFS.h"

ArretGTFS::ArretGTFS(const std::string& stopId, const std::string& intersection, unsigned int stopSequence)
: Arret(stopId, intersection, "")
, m_stop_sequence(stopSequence)
{
}


ArretGTFS::~ArretGTFS(void)
{
}


void ArretGTFS::setStopSequence(unsigned int stopSequence)
{
	if(stopSequence != m_stop_sequence)
	{
		m_stop_sequence = std::max(m_stop_sequence, stopSequence);
		//LOGWARNING("Bus have multiple routes, stop might not be in the perfect sequence");
	}
	
}


HoraireGTFS* ArretGTFS::ObtenirHoraireParServiceId(unsigned int date, const std::string& serviceId)
{
	HoraireGTFS* horaire = NULL;
	std::vector<Horaire*>::iterator iter = m_VectorHoraire.begin();

	while(iter!=m_VectorHoraire.end() && horaire == NULL)
	{
		HoraireGTFS* buf =dynamic_cast<HoraireGTFS*>(*iter);
		if(buf)
		{
			if(buf->getServiceId() == serviceId && buf->ObtenirNom() == date)
			{
				horaire = buf;
			}
			else
			{
				++iter;
			}
		}
		else
			LOGERROR("Dynamic cast fail from Horaire to HoraireGTFS.");

		
	}	
	return horaire;
}
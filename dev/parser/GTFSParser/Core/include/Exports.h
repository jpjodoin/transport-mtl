#ifndef _RHATECCOREDLL_EXPORTS_ 
#define _RHATECCOREDLL_EXPORTS_
#pragma warning( disable: 4251 )



#ifdef RHATECCOREDLL_EXPORTS
	#define RHATECCOREDLL_API __declspec(dllexport) 
#else 
	#define RHATECCOREDLL_API __declspec(dllimport) 
#endif 



#endif
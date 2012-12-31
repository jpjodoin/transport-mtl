# -*- coding: utf-8 -*-

import os
import datetime
from schedule import Schedule
from schedulegroup import ScheduleGroup

def getIndexList(header):
    hsplit = header.split(',');
    d = dict();
    i = 0
    for h in hsplit:
        d[h] = i
        i += 1
    return d

    
def Overlap(a, b):
    return (a[0] <= b[1]) and (a[1] >= b[0])


#def createCalendarFile():
    # Créer fichier avec hearder

#def modifyCalendarDateFile():

def ChangeCalendarFromDayToDayCode(path):    
    # TODO: Create fonction and add it to .py
    #This must be executed on latin converted files
    if not os.path.exists(path+'/'+'calendar.txt'):
        #GTFS data non séparé en semaine, samedi, dimanche...
        print 'Conversion en cours...';
        f = open(path+'/'+'calendar_dates.txt');
        line = f.readline().rstrip(); #Header    
        idxDict = getIndexList(line)
        serviceIdIdx = idxDict['service_id']
        dateIdx = idxDict['date']
        line = f.readline();
        scheduleToDate = dict()
        while line != "":
            content = line.rstrip().split(',')
            if not scheduleToDate.has_key(content[serviceIdIdx]):
                scheduleToDate[content[serviceIdIdx]] = list();
            scheduleToDate[content[serviceIdIdx]].append(content[dateIdx])
            line = f.readline();  
        f.close();
        scheduleList = list()
        for d in scheduleToDate:
            scheduleList.append(Schedule(d, scheduleToDate[d]))

        scheduleGroupList = list();
        # Correct intervals to have the full validity list
        for s in scheduleList:
            if(len(scheduleGroupList)==0):
                sg = ScheduleGroup(s.ValidityStart, s.ValidityEnd)
                sg.addSchedule(s, s.ValidityStart, s.ValidityEnd)
                scheduleGroupList.append(sg)            
            else:
                found = False
                for sg in scheduleGroupList:
                    if(Overlap([s.ValidityStart, s.ValidityEnd], [sg.ValidityStart, sg.ValidityEnd])):
                        sg.addSchedule(s, s.ValidityStart, s.ValidityEnd)
                        found = True
                if not found:
                    sg = ScheduleGroup(s.ValidityStart, s.ValidityEnd)
                    sg.addSchedule(s, s.ValidityStart, s.ValidityEnd)
                    scheduleGroupList.append(sg) 
        fcaldateout = open(path+'/'+'calendar_dates.txt', 'w')
        fcaldateout.writelines('service_id,date,exception_type\n');
        fcalout = open(path+'/'+'calendar.txt', 'w');
        fcalout.writelines('service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date\n');       
        for sg in scheduleGroupList:
              for s in sg.ScheduleList:
                  if not s.IsHoliday:
                      line = s.KeyName + ',' + str(int(s.IsWeekday)) + ',' + str(int(s.IsWeekday)) + ',' + str(int(s.IsWeekday)) + ',' + str(int(s.IsWeekday)) + ',' + str(int(s.IsWeekday)) + ',' + str(int(s.IsSathurday)) + ',' + str(int(s.IsSunday)) + ',' + str(sg.ValidityStart) + ',' + str(sg.ValidityEnd) +'\n'
                      print line;
                      fcalout.writelines(line);
                  else:
                      fcaldateout.writelines(s.KeyName + ',' + str(s.ValidityStart) + ',1\n');
                      fcalout.writelines(s.KeyName + ',0,0,0,0,0,0,0,'+ str(sg.ValidityStart) + ',' + str(sg.ValidityEnd) +'\n')
                      print 'calendar_dates.txt: '+ s.KeyName + ',' + str(s.ValidityStart) + ',1\n'
                  print str(sg.ValidityStart) + " " + str(sg.ValidityEnd)

        fcalout.close();
        fcaldateout.close();


    
    
        




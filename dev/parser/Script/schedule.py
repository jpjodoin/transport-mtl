# -*- coding: utf-8 -*-
import os
import datetime
#TODO: Regarder héritage en python pour avoir jour férié et horaire régulier

class Schedule:
    def __init__(self, keyname, dayList):        
        self.KeyName = keyname;
        self.IsWeekday = False
        self.IsSathurday = False
        self.IsSunday = False
        self.IsHoliday = False
        if len(dayList) == 1:
            self.IsHoliday = True;
            self.ValidityStart = int(dayList[0]);
            self.ValidityEnd = int(dayList[0]); 
           #print d + ' jour ferie : ' + str(scheduleToDate[d][0])
        else:
            Weekday = 0;
            Sathurday = 0;
            Sunday = 0;
            lowestdate = 999999999;
            biggestdate = 0;
            for day in dayList:
                dayint = int(day)
                if dayint > biggestdate:
                    biggestdate = dayint
                if dayint < lowestdate:
                    lowestdate = dayint
                
                dstr = str(day)
                year = int(day[0:4]);
                month = int(day[4:6]);
                day = int(day[6:8]);
                date = datetime.date(year, month, day)
                dayofweek = date.isoweekday()
                if dayofweek == 7:
                    Sunday +=1
                elif dayofweek == 6:
                    Sathurday +=1;
                else:
                    Weekday+=1;
                    
            if(Weekday == 0 and Sathurday == 0):
                self.IsSunday = True; 
            elif(Weekday == 0 and Sunday == 0):
                self.IsSathurday = True;
            elif(Sathurday==0 and Sunday == 0):
                self.IsWeekday = True;
            else:
                print 'Erreur: '+  keyname + " Sem: " + str(Weekday) + ' Sam: ' + str(Sathurday) + ' Dim: ' + str(Sunday)
            self.ValidityStart = int(lowestdate);
            self.ValidityEnd = int(biggestdate);


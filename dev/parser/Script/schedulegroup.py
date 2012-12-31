# -*- coding: utf-8 -*-
import os
import datetime

class ScheduleGroup:
    def __init__(self, lowestdate, biggestdate):        
            self.ValidityStart = lowestdate;
            self.ValidityEnd = biggestdate;
            self.ScheduleList = list();

    def addSchedule(self, id, low, high):
        if low < self.ValidityStart:
            self.ValidityStart = low
        if high > self.ValidityEnd:
            self.ValidityEnd = high
        self.ScheduleList.append(id)
            


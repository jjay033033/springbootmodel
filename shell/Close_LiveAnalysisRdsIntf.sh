#!/bin/sh
ps aux |grep LiveAnalysisRdsIntf-0.0.1.jar |grep -v grep |awk '{print $2}' |xargs kill -9

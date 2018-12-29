#!/bin/sh
ps aux |grep springbootmodel-0.0.1.jar |grep -v grep |awk '{print $2}' |xargs kill -9

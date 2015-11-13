#!/bin/bash

DEST=~/Downloads/soumaisaventura/dump
NEW_PATH=$DEST/soumaisaventura-$(date +"%Y%m%d_%H%M").dump
CUR_PATH=$DEST/soumaisaventura-atual.dump

mkdir -p $DEST

scp 5638df857628e1c07d000044@jbossas-soumaisaventura.rhcloud.com:~/git/jbossas.git/temp_files/db.dump $NEW_PATH

rm -rf $CUR_PATH
mv $NEW_PATH $CUR_PATH
cp $CUR_PATH $NEW_PATH

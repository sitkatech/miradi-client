#!/bin/sh
ssh staging.miradi.org cat deployment/PASSWD
rsync --verbose --times --sparse --compress \
	../README.txt \
	../dist/miradi.jar \
	../dist/MiradiSetup.exe \
	../dist/Miradi-Linux.zip \
	../dist/MarineExample*.mpz \
	../dist/Miradi-Thirdparty-Source.zip \
	../dist/Miradi-Source.zip \
	../dist/MiradiContent*.jar \
	../dist/Miradi.dmg \
	../dist/miradi.version.txt \
	../dist/miradi.timestamp.txt \
	miradi-deployment@staging.miradi.org:/var/www/domains/miradi.org/downloadable_files/untested
	
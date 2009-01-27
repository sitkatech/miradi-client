#!/bin/sh
ssh staging.miradi.org cat deployment/PASSWD
rsync --verbose --times --sparse --compress \
	installer/BuildFiles/README.txt \
	../../binaries-miradi/dist/miradi.jar \
	../../binaries-miradi/dist/MiradiSetup.exe \
	../../binaries-miradi/dist/Miradi-Linux.zip \
	../../binaries-miradi/dist/MarineExample*.mpz \
	../../binaries-miradi/dist/Miradi-Thirdparty-Source.zip \
	../../binaries-miradi/dist/Miradi-Source.zip \
	../../binaries-miradi/dist/MiradiContent*.jar \
	../../binaries-miradi/dist/Miradi.dmg \
	../../binaries-miradi/dist/miradi.version.txt \
	../../binaries-miradi/dist/miradi.timestamp.txt \
	miradi-deployment@staging.miradi.org:/var/www/domains/miradi.org/downloadable_files/untested
	
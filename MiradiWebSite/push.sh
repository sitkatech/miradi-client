rsync -vHSzrlgoD -r --exclude=tmp/ --exclude=log/ --exclude=CVS/ . miradi.org:/var/www/miradi.org/
rsync -vHSzrlgoD -r ../README miradi.org:/var/www/miradi.org/private/
rsync -vHSzrlgoD -r ../dist/MiradiSetup.exe miradi.org:/var/www/miradi.org/private/
rsync -vHSzrlgoD -r ../dist/Miradi-Mac.zip miradi.org:/var/www/miradi.org/private/
rsync -vHSzrlgoD -r ../dist/Miradi-Linux.zip miradi.org:/var/www/miradi.org/private/
rsync -vHSzrlgoD -r ../dist/MarineExample.zip miradi.org:/var/www/miradi.org/private/

rsync -vHSzrlgoDC -r --exclude=tmp/ --exclude=log/ . cmp.benetech.org:/var/www/miradi.org/
rsync -vHSzrlgoDC -r ../README cmp.benetech.org:/var/www/miradi.org/private/
rsync -vHSzrlgoDC -r ../dist/MiradiSetup.exe cmp.benetech.org:/var/www/miradi.org/private/
rsync -vHSzrlgoDC -r ../dist/Miradi-Mac.zip cmp.benetech.org:/var/www/miradi.org/private/
rsync -vHSzrlgoDC -r ../dist/Miradi-Linux.zip cmp.benetech.org:/var/www/miradi.org/private/
rsync -vHSzrlgoDC -r ../dist/MarineExample.zip cmp.benetech.org:/var/www/miradi.org/private/

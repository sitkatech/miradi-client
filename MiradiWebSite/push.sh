rsync -vHSzrlgoDC -r --exclude=tmp/ . cmp.benetech.org:/var/www/miradi.org/
rsync -vHSzrlgoDC -r ../README cmp.benetech.org:/var/www/miradi.org/private/
rsync -vHSzrlgoDC -r ../dist/MiradiSetup.exe cmp.benetech.org:/var/www/miradi.org/private/

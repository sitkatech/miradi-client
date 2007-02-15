rsync --verbose --times --sparse --compress --recursive --exclude=tmp/ --exclude=log/ --exclude=CVS/ . miradi.org:/var/www/miradi.org/
echo NOW ssh to miradi.org, go to /var/www/miradi.org, and run rake migrate
echo then restart lighty: sudo /etc/init.d/lighttpd restart

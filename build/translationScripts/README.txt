The script replaceFromGlossary.py is to be used to build customer specific .po files, it expects a CSV glossary from http://www.transifex.com as well as a template .po file.  Requires:

* Was built with python 2.7 so should be run with that
* expects to be run with this directory being the current working directory

There is a limited auto-generated help available with -h, but it should be run with commands like this:

>python replaceFromGlossary.py "/location/of/po/files/miradi_msgid_and_msgstr_equal.po" "/location/of/glossary/someLang.csv" "../../translations/miradi_someLang.po"
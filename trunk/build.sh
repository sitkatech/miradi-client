date "+%Y-%m-%d %X" > bin/version.txt

rm ../eam.zip
zip ../eam README LICENSE.jgraph cmp.ico "e-Adaptive Management.lnk"
zip -j ../eam third-party/jgraph/jgraph.jar
zip -r ../eam icons -x CVS/
zip -r ../eam translations -x CVS/
cd ..

cd cmp-eam/bin
zip -r ../../eam . -i "*.class"
zip -r ../../eam . -i "version.txt"
cd ../..

cd martus-swing/bin
zip -r ../../eam . -i "*.class"
cd ../..

cd martus-utils/bin
zip -r ../../eam . -i "*.class"
cd ../..

echo REMEMBER TO TAG THIS RELEASE!

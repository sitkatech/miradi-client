rm ../eam.zip
zip ../eam README LICENSE.jgraph
zip -j ../eam third-party/jgraph/jgraph.jar
zip -r ../eam icons -x CVS/
zip -r ../eam translations -x CVS/
cd ..

cd cmp-eam/bin
zip -r ../../eam . -i "*.class"
cd ../..

cd martus-swing/bin
zip -r ../../eam . -i "*.class"
cd ../..

cd martus-utils/bin
zip -r ../../eam . -i "*.class"
cd ../..

cd /opt/ht/source/eng-sandbox
#clear env settings for prev build not to have unstash changes
git checkout *
git fetch --all
git rebase origin/master
#put for env ip of health tool machine
export HEALTH_HOME_SOURCE=/opt/ht/source/eng-sandbox/cluster-health-checker-tool/
/bin/cp -rf /opt/ht/js_conf/* $HEALTH_HOME_SOURCE/
#build everything including frontend
cd $HEALTH_HOME_SOURCE
mvn clean install -Pall
export HEALTH_HOME_BUILD=/opt/ht/build/current
rm -r $HEALTH_HOME_BUILD/*
unzip $HEALTH_HOME_SOURCE/controller/target/health-checker-tool-controller-*.zip -d $HEALTH_HOME_BUILD
mkdir $HEALTH_HOME_BUILD/upload
#put env host
/bin/cp -rf /opt/ht/prod_conf/* $HEALTH_HOME_BUILD
cd $HEALTH_HOME_BUILD
chmod a+rwx *.sh 

#mvn -e exec:java -Dexec.mainClass="com.epam.health.tool.model.ddl.Dataload" -Dexec.classpathScope="test"


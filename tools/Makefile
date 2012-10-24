clean:
	rm -rf server/

prepare:
	mkdir -p server/plugins/

download-vanilla:
	wget -O server/plugins/Vanilla.jar http://build.spout.org/job/Vanilla/lastSuccessfulBuild/artifact/target/vanilla-1.3.1-SNAPSHOT.jar

download-spout:
	wget -O server/spout.jar http://build.spout.org/job/Spout/lastSuccessfulBuild/artifact/target/spout-dev-SNAPSHOT.jar

download-politics:
	wget -O server/plugins/Politics.jar http://ci.massiveminecraft.com/view/Spout%20Plugins/job/Politics/lastSuccessfulBuild/artifact/target/politics-0.0.1-SNAPSHOT.jar

exec:
	cd server; java -Xmx16G -jar spout.jar -p server

clean:
	rm -rf server/

download:
	mkdir -p server/plugins/

	wget -O server/plugins/Vanilla.jar http://build.spout.org/job/Vanilla/lastSuccessfulBuild/artifact/target/vanilla-1.3.2-SNAPSHOT.jar
	wget -O server/spout.jar http://build.spout.org/job/Spout/lastSuccessfulBuild/artifact/target/spout-dev-SNAPSHOT.jar
	wget -O server/plugins/Politics.jar http://ci.massiveminecraft.com/view/Spout%20Plugins/job/Politics/lastSuccessfulBuild/artifact/target/politics-0.0.1-SNAPSHOT.jar

exec:
	java -jar server/spout.jar -Xmx16G

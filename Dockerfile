FROM centos

ENV HOME=/softwork
ENV JAVA_HOME=$HOME/jdk1.8.0_421
ENV JAVA=$JAVA_HOME/bin/java

WORKDIR $HOME/canal

RUN mkdir $JAVA_HOME


COPY ./canal.deployer-1.1.6.tar.gz $HOME/canal
COPY ./jdk-8u421-linux-x64.tar.gz $HOME

RUN tar -zxvf $HOME/canal/canal.deployer-1.1.6.tar.gz -C $HOME/canal
RUN tar -zxvf $HOME/jdk-8u421-linux-x64.tar.gz -C $HOME

EXPOSE 11111

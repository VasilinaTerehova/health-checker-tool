@echo off

IF "%TEMP%"=="" (
  ECHO No temp dir, will use %BASEDIR%/upload
  set OPT="-Djava.io.tmpdir=%BASEDIR%/upload"
) ELSE (
  ECHO Temp dir - %TEMP%
)

java -jar %OPT% health-checker-tool-controller-0.0.1-SNAPSHOT.jar
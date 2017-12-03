@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  server startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and SERVER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\server-1.0-server.jar;%APP_HOME%\lib\jersey-container-grizzly2-http-2.26.jar;%APP_HOME%\lib\jersey-hk2-2.26.jar;%APP_HOME%\lib\hibernate-core-5.2.12.Final.jar;%APP_HOME%\lib\hibernate-validator-annotation-processor-6.0.3.Final.jar;%APP_HOME%\lib\jersey-mvc-freemarker-2.26.jar;%APP_HOME%\lib\javax.el-2.2.6.jar;%APP_HOME%\lib\javax.el-api-3.0.0.jar;%APP_HOME%\lib\aws-iot-device-sdk-java-1.1.1.jar;%APP_HOME%\lib\commons-dbcp-1.4.jar;%APP_HOME%\lib\mysql-connector-java-5.1.37.jar;%APP_HOME%\lib\gson-2.8.2.jar;%APP_HOME%\lib\commons-cli-1.4.jar;%APP_HOME%\lib\springfox-swagger-ui-2.7.0.jar;%APP_HOME%\lib\swagger-jersey2-jaxrs-1.5.17.jar;%APP_HOME%\lib\jersey-media-json-jackson-2.26.jar;%APP_HOME%\lib\aws-java-sdk-iot-1.11.221.jar;%APP_HOME%\lib\aws-java-sdk-events-1.11.238.jar;%APP_HOME%\lib\javax.inject-2.5.0-b42.jar;%APP_HOME%\lib\grizzly-http-server-2.4.0.jar;%APP_HOME%\lib\jersey-common-2.26.jar;%APP_HOME%\lib\jersey-server-2.26.jar;%APP_HOME%\lib\javax.ws.rs-api-2.1.jar;%APP_HOME%\lib\hk2-locator-2.5.0-b42.jar;%APP_HOME%\lib\jboss-logging-3.3.0.Final.jar;%APP_HOME%\lib\hibernate-jpa-2.1-api-1.0.0.Final.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jboss-transaction-api_1.2_spec-1.0.1.Final.jar;%APP_HOME%\lib\jandex-2.0.3.Final.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\hibernate-commons-annotations-5.0.1.Final.jar;%APP_HOME%\lib\hibernate-validator-annotation-processor-6.0.3.Final.jar;%APP_HOME%\lib\jersey-mvc-2.26.jar;%APP_HOME%\lib\freemarker-2.3.23.jar;%APP_HOME%\lib\org.eclipse.paho.client.mqttv3-1.1.0.jar;%APP_HOME%\lib\commons-pool-1.5.4.jar;%APP_HOME%\lib\springfox-spring-web-2.7.0.jar;%APP_HOME%\lib\swagger-jaxrs-1.5.17.jar;%APP_HOME%\lib\jersey-container-servlet-core-2.25.1.jar;%APP_HOME%\lib\jersey-media-multipart-2.25.1.jar;%APP_HOME%\lib\jersey-entity-filtering-2.26.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.8.4.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\ion-java-1.0.2.jar;%APP_HOME%\lib\jackson-dataformat-cbor-2.6.7.jar;%APP_HOME%\lib\joda-time-2.8.1.jar;%APP_HOME%\lib\grizzly-http-2.4.0.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.1.jar;%APP_HOME%\lib\jersey-client-2.26.jar;%APP_HOME%\lib\jersey-media-jaxb-2.26.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\aopalliance-repackaged-2.5.0-b42.jar;%APP_HOME%\lib\hk2-api-2.5.0-b42.jar;%APP_HOME%\lib\hk2-utils-2.5.0-b42.jar;%APP_HOME%\lib\servlet-api-2.4.jar;%APP_HOME%\lib\reflections-0.9.11.jar;%APP_HOME%\lib\slf4j-api-1.7.24.jar;%APP_HOME%\lib\spring-plugin-core-1.2.0.RELEASE.jar;%APP_HOME%\lib\spring-plugin-metadata-1.2.0.RELEASE.jar;%APP_HOME%\lib\springfox-spi-2.7.0.jar;%APP_HOME%\lib\swagger-core-1.5.17.jar;%APP_HOME%\lib\mimepull-1.9.6.jar;%APP_HOME%\lib\grizzly-framework-2.4.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\spring-beans-4.0.9.RELEASE.jar;%APP_HOME%\lib\spring-context-4.0.9.RELEASE.jar;%APP_HOME%\lib\spring-aop-4.0.9.RELEASE.jar;%APP_HOME%\lib\springfox-core-2.7.0.jar;%APP_HOME%\lib\commons-lang3-3.2.1.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.8.9.jar;%APP_HOME%\lib\swagger-models-1.5.17.jar;%APP_HOME%\lib\spring-core-4.0.9.RELEASE.jar;%APP_HOME%\lib\spring-expression-4.0.9.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\byte-buddy-1.6.14.jar;%APP_HOME%\lib\snakeyaml-1.17.jar;%APP_HOME%\lib\swagger-annotations-1.5.17.jar;%APP_HOME%\lib\jackson-databind-2.8.9.jar;%APP_HOME%\lib\aws-java-sdk-core-1.11.238.jar;%APP_HOME%\lib\httpcore-4.4.4.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\jmespath-java-1.11.238.jar;%APP_HOME%\lib\javassist-3.22.0-CR2.jar;%APP_HOME%\lib\classmate-1.3.3.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\jackson-core-2.8.9.jar;%APP_HOME%\lib\jackson-annotations-2.8.9.jar;%APP_HOME%\lib\commons-logging-1.2.jar

@rem Execute server
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SERVER_OPTS%  -classpath "%CLASSPATH%" in.bookstruck.api.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SERVER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SERVER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
